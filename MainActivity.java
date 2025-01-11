package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding; // Utilisation du View Binding pour accéder aux vues
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // État des cases
    private int playerTurn = 1; // 1 pour le joueur 1, 2 pour l'IA
    private int totalSelectedBoxes = 0;

    private String playerSymbol = "X"; // Symbole choisi par le joueur
    private String aiSymbol = "O"; // Symbole attribué à l'IA

    // Scores des joueurs
    private int playerOneScore = 0;
    private int aiScore = 0;

    private DatabaseManager dbManager; // Instance pour gérer la base de données
    private String playerName; // Nom du joueur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation du binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialisation du DatabaseManager
        dbManager = new DatabaseManager(this);

        // Initialisation des combinaisons gagnantes
        setupWinningCombinations();

        // Récupération des données transmises par l'intent
        playerName = getIntent().getStringExtra("playerOne");
        playerSymbol = getIntent().getStringExtra("playerSymbol");
        aiSymbol = playerSymbol.equals("X") ? "O" : "X"; // Attribution du symbole opposé à l'IA

        // Vérification et affichage du joueur depuis la base de données
        if (!dbManager.playerExists(playerName)) {
            dbManager.insertPlayer(playerName, 0, 0, 0); // Ajout du joueur à la base si inexistant
        }

        // Mise à jour des noms des joueurs
        binding.playerOneName.setText(playerName != null ? playerName : "Player 1");
        binding.playerTwoName.setText("AI");

        // Configuration du menu
        setupMenu();

        // Configuration des cases du jeu
        configureGameGrid();

        // Mise à jour initiale des scores
        updateScoreDisplay();
    }

    /**
     * Initialise les combinaisons gagnantes.
     */
    private void setupWinningCombinations() {
        combinationList.add(new int[]{0, 1, 2});
        combinationList.add(new int[]{3, 4, 5});
        combinationList.add(new int[]{6, 7, 8});
        combinationList.add(new int[]{0, 3, 6});
        combinationList.add(new int[]{1, 4, 7});
        combinationList.add(new int[]{2, 5, 8});
        combinationList.add(new int[]{0, 4, 8});
        combinationList.add(new int[]{2, 4, 6});
    }

    /**
     * Configure le menu contextuel.
     */
    private void setupMenu() {
        binding.menuButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, binding.menuButton);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.showScores) {
                    displayScores();
                    return true;
                } else if (itemId == R.id.returnHome) {
                    startActivity(new Intent(MainActivity.this, AddPlayers.class));
                    finish();
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }

    /**
     * Affiche les scores des joueurs dans un Toast.
     */
    private void displayScores() {
        String scores = dbManager.getFormattedScores();
        showToast(scores);
    }

    /**
     * Configure les clics sur les cases du jeu.
     */
    private void configureGameGrid() {
        binding.image1.setOnClickListener(view -> selectBox(view, 0));
        binding.image2.setOnClickListener(view -> selectBox(view, 1));
        binding.image3.setOnClickListener(view -> selectBox(view, 2));
        binding.image4.setOnClickListener(view -> selectBox(view, 3));
        binding.image5.setOnClickListener(view -> selectBox(view, 4));
        binding.image6.setOnClickListener(view -> selectBox(view, 5));
        binding.image7.setOnClickListener(view -> selectBox(view, 6));
        binding.image8.setOnClickListener(view -> selectBox(view, 7));
        binding.image9.setOnClickListener(view -> selectBox(view, 8));
    }

    /**
     * Vérifie si une case est sélectionnable.
     */
    private boolean isBoxSelectable(int position) {
        return boxPositions[position] == 0;
    }

    /**
     * Gère la sélection d'une case.
     */
    private void selectBox(View view, int position) {
        if (isBoxSelectable(position)) {
            performAction((ImageView) view, position);
        }
    }

    /**
     * Effectue l'action pour une case sélectionnée.
     */
    private void performAction(ImageView view, int position) {
        boxPositions[position] = playerTurn;

        if (playerTurn == 1) {
            view.setImageResource(playerSymbol.equals("X") ? R.drawable.ic_xicon : R.drawable.ic_oicon);
            if (checkWinner()) {
                playerOneScore++;
                dbManager.updatePlayerStatistics(playerName, 1, 0, 0); // Mise à jour des stats dans la base
                showToast(playerName + " wins!");
                resetGame();
                return;
            }
            playerTurn = 2; // Passer le tour à l'IA
            playAITurn();
        } else {
            view.setImageResource(aiSymbol.equals("X") ? R.drawable.ic_xicon : R.drawable.ic_oicon);
            if (checkWinner()) {
                aiScore++;
                showToast("AI wins!");
                resetGame();
                return;
            }
            playerTurn = 1; // Retour au joueur
        }

        totalSelectedBoxes++;
        if (totalSelectedBoxes == 9) {
            showToast("It's a draw!");
            dbManager.updatePlayerStatistics(playerName, 0, 0, 1); // Match nul
            resetGame();
        }
    }

    /**
     * Joue le tour de l'IA.
     */
    private void playAITurn() {
        Random random = new Random();
        int position;

        do {
            position = random.nextInt(9); // Choisit une case aléatoire
        } while (!isBoxSelectable(position));

        selectBox(getImageViewByPosition(position), position);
    }

    /**
     * Vérifie si un joueur a gagné.
     */
    private boolean checkWinner() {
        for (int[] combination : combinationList) {
            if (boxPositions[combination[0]] == playerTurn &&
                    boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                return true;
            }
        }
        return false;
    }

    /**
     * Réinitialise le jeu après un tour terminé.
     */
    private void resetGame() {
        boxPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        totalSelectedBoxes = 0;
        playerTurn = 1;

        binding.image1.setImageResource(0);
        binding.image2.setImageResource(0);
        binding.image3.setImageResource(0);
        binding.image4.setImageResource(0);
        binding.image5.setImageResource(0);
        binding.image6.setImageResource(0);
        binding.image7.setImageResource(0);
        binding.image8.setImageResource(0);
        binding.image9.setImageResource(0);

        updateScoreDisplay();
    }

    /**
     * Met à jour les scores affichés.
     */
    private void updateScoreDisplay() {
        binding.playerOneScore.setText(String.valueOf(playerOneScore));
        binding.playerTwoScore.setText(String.valueOf(aiScore));
    }

    /**
     * Affiche un message Toast.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Récupère l'ImageView correspondant à une position.
     */
    private ImageView getImageViewByPosition(int position) {
        switch (position) {
            case 0:
                return binding.image1;
            case 1:
                return binding.image2;
            case 2:
                return binding.image3;
            case 3:
                return binding.image4;
            case 4:
                return binding.image5;
            case 5:
                return binding.image6;
            case 6:
                return binding.image7;
            case 7:
                return binding.image8;
            case 8:
                return binding.image9;
            default:
                return null;
        }
    }
}

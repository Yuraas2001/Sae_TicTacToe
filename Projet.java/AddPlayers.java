package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddPlayers extends AppCompatActivity {

    private DatabaseManager dbManager; // Instance de DatabaseManager
    private String playerSymbol = "X"; // Symbole par défaut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);

        // Initialisation du DatabaseManager
        dbManager = new DatabaseManager(this);

        // Récupération des vues
        EditText playerOneInput = findViewById(R.id.player_one_name);
        RadioGroup symbolGroup = findViewById(R.id.player_one_symbol_group); // Groupe de boutons radio
        Button startGameButton = findViewById(R.id.startGameButton);

        // Gestion du choix du symbole via le groupe de boutons radio
        symbolGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.player_one_x) {
                playerSymbol = "X"; // Le joueur choisit "X"
            } else if (checkedId == R.id.player_one_o) {
                playerSymbol = "O"; // Le joueur choisit "O"
            }
        });

        // Bouton pour démarrer le jeu avec le joueur ajouté
        startGameButton.setOnClickListener(v -> {
            String playerName = playerOneInput.getText().toString().trim();

            if (playerName.isEmpty()) {
                Toast.makeText(AddPlayers.this, "Veuillez entrer un nom de joueur avant de commencer", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier si le joueur existe déjà
            if (dbManager.playerExists(playerName)) {
                Toast.makeText(AddPlayers.this, "Le joueur existe déjà. Chargement...", Toast.LENGTH_SHORT).show();
            } else {
                // Ajouter le joueur à la base de données
                addPlayerToDatabase(playerName);
            }

            // Passer à l'activité principale avec le nom du joueur et le symbole choisi
            Intent intent = new Intent(AddPlayers.this, MainActivity.class);
            intent.putExtra("playerOne", playerName);
            intent.putExtra("playerSymbol", playerSymbol);
            intent.putExtra("aiSymbol", playerSymbol.equals("X") ? "O" : "X"); // IA prend le symbole opposé
            startActivity(intent);
        });
    }

    /**
     * Ajoute un joueur à la base de données.
     * @param playerName Nom du joueur.
     */
    private void addPlayerToDatabase(String playerName) {
        long playerId = dbManager.insertPlayer(playerName, 0, 0, 0); // Initialisation des scores à 0
        if (playerId != -1) {
            Toast.makeText(this, "Joueur ajouté avec succès !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout du joueur.", Toast.LENGTH_SHORT).show();
        }
        if (playerId == -1) {
            Toast.makeText(this, "Erreur : le joueur n'a pas pu être ajouté. Vérifiez les logs.", Toast.LENGTH_LONG).show();
        }

    }

}


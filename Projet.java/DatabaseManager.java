package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";
    private final DatabaseHelper dbHelper; // Instance de DatabaseHelper pour gérer la base de données

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Insère un joueur dans la table Players.
     * @param name Nom du joueur.
     * @param wins Nombre de victoires.
     * @param losses Nombre de défaites.
     * @param draws Nombre de matchs nuls.
     * @return L'ID du joueur inséré ou -1 en cas d'échec.
     */
    public long insertPlayer(String name, int wins, int losses, int draws) {
        SQLiteDatabase db = null;
        long playerId = -1;

        try {
            db = dbHelper.openWritableDatabase(); // Ouvre la base en mode écriture

            // Vérifie si le joueur existe déjà
            if (playerExists(name)) {
                Log.d(TAG, "Le joueur " + name + " existe déjà.");
                return playerId;
            }

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("wins", wins);
            values.put("losses", losses);
            values.put("draws", draws);
            values.put("score", (wins * 3 + draws)); // Calcul du score initial

            Log.d(TAG, "Insertion des données : " + values.toString());
            playerId = db.insert("Players", null, values);

            if (playerId != -1) {
                Log.d(TAG, "Joueur inséré avec succès ! ID: " + playerId);
            } else {
                Log.e(TAG, "Erreur d'insertion : joueur non inséré.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'insertion du joueur : " + e.getMessage());
        } finally {
            if (db != null) db.close(); // Toujours fermer la base après utilisation
        }

        return playerId;
    }

    /**
     * Vérifie si un joueur existe dans la base de données.
     * @param name Nom du joueur.
     * @return true si le joueur existe, false sinon.
     */
    public boolean playerExists(String name) {
        boolean exists = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.openDatabase();
            cursor = db.rawQuery("SELECT COUNT(*) FROM Players WHERE name = ?", new String[]{name});
            if (cursor != null && cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la vérification de l'existence du joueur : " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        Log.d(TAG, "Le joueur \"" + name + "\" existe déjà : " + exists);
        return exists;
    }

    /**
     * Met à jour les statistiques et le score d'un joueur.
     * @param playerName Nom du joueur.
     * @param wins Nombre de victoires à ajouter.
     * @param losses Nombre de défaites à ajouter.
     * @param draws Nombre de matchs nuls à ajouter.
     */
    public void updatePlayerStatistics(String playerName, int wins, int losses, int draws) {
        SQLiteDatabase db = null;

        try {
            db = dbHelper.openWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("wins", wins);
            values.put("losses", losses);
            values.put("draws", draws);
            values.put("score", (wins * 3 + draws));

            Log.d(TAG, "Mise à jour des statistiques pour le joueur : " + playerName);
            int rowsAffected = db.update("Players", values, "name = ?", new String[]{playerName});
            if (rowsAffected > 0) {
                Log.d(TAG, "Statistiques mises à jour avec succès pour le joueur : " + playerName);
            } else {
                Log.e(TAG, "Aucune ligne mise à jour pour le joueur : " + playerName);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la mise à jour des statistiques : " + e.getMessage());
        } finally {
            if (db != null) db.close(); // Toujours fermer la base de données
        }
    }

    /**
     * Récupère les scores des joueurs sous une forme formatée.
     * @return Chaîne contenant les scores formatés.
     */
    public String getFormattedScores() {
        StringBuilder scores = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.openDatabase();
            cursor = getPlayedPlayers();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    int wins = cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
                    int losses = cursor.getInt(cursor.getColumnIndexOrThrow("losses"));
                    int draws = cursor.getInt(cursor.getColumnIndexOrThrow("draws"));
                    int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));

                    scores.append(name)
                            .append(" - Wins: ").append(wins)
                            .append(", Losses: ").append(losses)
                            .append(", Draws: ").append(draws)
                            .append(", Score: ").append(score)
                            .append("\n");
                } while (cursor.moveToNext());
            } else {
                scores.append("Aucun joueur trouvé.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la récupération des scores : " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return scores.toString();
    }

    /**
     * Récupère les joueurs ayant joué et leurs scores.
     * @return Curseur contenant les joueurs ayant joué.
     */
    public Cursor getPlayedPlayers() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.openDatabase();
            cursor = db.rawQuery("SELECT name, wins, losses, draws, score FROM Players WHERE wins > 0 OR losses > 0 OR draws > 0 ORDER BY score DESC", null);
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la récupération des joueurs ayant joué : " + e.getMessage());
        }

        return cursor;
    }
}

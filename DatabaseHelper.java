package com.example.tictactoe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "sae32test.db"; // Nom de la base de données
    private static final int DATABASE_VERSION = 1; // Version de la base de données

    public DatabaseHelper(Context context) {
        // Initialise SQLiteAssetHelper avec le nom et la version de la base de données
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Vérifie si la base de données est accessible.
     *
     * @return `true` si la base de données est accessible, sinon `false`.
     */
    public boolean isDatabaseAccessible() {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase(); // Ouvre la base de données en lecture
            if (db != null) {
                db.close();
                Log.d(TAG, "Base de données accessible.");
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'accès à la base de données : " + e.getMessage());
        }
        return false;
    }

    /**
     * Ouvre la base de données en mode lecture seule.
     *
     * @return Une instance de `SQLiteDatabase` en mode lecture seule.
     */
    public SQLiteDatabase openDatabase() {
        try {
            return getReadableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'ouverture de la base de données en lecture : " + e.getMessage());
            return null;
        }
    }

    /**
     * Ouvre la base de données en mode écriture.
     *
     * @return Une instance de `SQLiteDatabase` en mode écriture.
     */
    public SQLiteDatabase openWritableDatabase() {
        try {
            return getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'ouverture de la base de données en écriture : " + e.getMessage());
            return null;
        }
    }
}

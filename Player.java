package com.example.tictactoe;

public class Player {
    private int id; // ID du joueur
    private String name; // Nom du joueur
    private int score; // Score du joueur

    // Constructeur par défaut (obligatoire pour Retrofit)
    public Player() {
    }

    // Constructeur avec paramètres
    public Player(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Méthode pour afficher les informations du joueur
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}

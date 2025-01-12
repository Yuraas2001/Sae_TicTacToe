package com.example.tictactoe;

public class Joueur {
    private int id;
    private String nom;
    private int wins;
    private int losses;
    private int draws;

    // Constructeur
    public Joueur(int id, String nom, int wins, int losses, int draws) {
        this.id = id;
        this.nom = nom;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    @Override
    public String toString() {
        return "Joueur {" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", wins=" + wins +
                ", losses=" + losses +
                ", draws=" + draws +
                '}';
    }
}

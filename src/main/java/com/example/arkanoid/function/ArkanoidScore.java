package com.example.arkanoid.function;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArkanoidScore {
    private int score; // Le score actuel du joueur
    private long cooldownPoint; // Temps du dernier bonus de score
    private static final String SCORE_FILE = "resources/best_score.json"; // Chemin du fichier pour enregistrer le meilleur score

    // Constructeur de la classe, initialise le score à 0 et le cooldownPoint au temps actuel
    public ArkanoidScore() {
        this.score = 0;
        this.cooldownPoint = System.currentTimeMillis();
    }

    // Incrémente le score de 5 points (utilisé lorsque le joueur touche une brique)
    public void incrementScoreOnTouch() {
        score += 5;
    }

    // Incrémente le score de 15 points (utilisé lorsque le joueur casse une brique)
    public void incrementScoreOnBreak() {
        score += 15;
    }

    // Retourner le score actuel
    public int getScore() {
        return score;
    }

    // Met à jour le score avec un bonus de temps (1 point toutes les 5 secondes)
    public void updateScoreWithTimeBonus() {
        long currentTime = System.currentTimeMillis(); // Temps actuel
        long elapsedTime = currentTime - cooldownPoint; // Temps écoulé depuis le dernier bonus

        // Si 5 secondes se sont écoulées, ajouter 1 au score et mettre à jour le temps du dernier bonus
        if (elapsedTime >= 5000) {
            score++;
            cooldownPoint = currentTime;
        }
    }

    // Sauvegarder le meilleur score dans un fichier JSON
    public void saveBestScore() {
        File file = new File(SCORE_FILE);

        try {
            // Créer le répertoire parent si nécessaire
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    System.err.println("Failed to create directories for score file.");
                    return;
                }
            }

            // Écrire le score dans le fichier JSON
            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new Gson();
                gson.toJson(score, writer); // Convertir le score en JSON et l'écrire dans le fichier
                System.out.println("Best score saved: " + score);
            }
        } catch (IOException e) {
            System.err.println("Failed to save best score: " + e.getMessage());
        }
    }

    // Charger le meilleur score à partir du fichier JSON
    public int loadBestScore() {
        File file = new File(SCORE_FILE);

        // Si le fichier n'existe pas, le créer avec un score de 0
        if (!file.exists()) {
            System.out.println("Le fichier score existe pas.");
            saveDefaultScore(file);
            return 0; // Retourner un score par défaut de 0
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Integer.class); // Lire et convertir le contenu JSON en score
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Echec de la lecture: " + e.getMessage());
            return 0; // Retourner un score de 0 en cas d'erreur
        }
    }

    // Créer un fichier de score avec un score par défaut de 0
    private void saveDefaultScore(File file) {
        try {
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("0"); // Écrire un score par défaut de 0
                    System.out.println("Fichier de score par défaut crée.");
                }
            }
        } catch (IOException e) {
            System.err.println("Echec de la création du fichier: " + e.getMessage());
        }
    }
}

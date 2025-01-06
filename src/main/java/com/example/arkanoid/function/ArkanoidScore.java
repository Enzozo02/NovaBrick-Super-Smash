package com.example.arkanoid.function;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArkanoidScore {
    private int score;
    private long cooldownPoint;
    private static final String SCORE_FILE = "resources/best_score.json";

    public ArkanoidScore() {
        this.score = 0;
        this.cooldownPoint = System.currentTimeMillis();
    }

    public void incrementScoreOnTouch() {
        score += 5;
    }

    public void incrementScoreOnBreak() {
        score += 15;
    }

    public int getScore() {
        return score;
    }

    public void updateScoreWithTimeBonus() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - cooldownPoint;

        if (elapsedTime >= 5000) {
            score++;
            cooldownPoint = currentTime;
        }
    }

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

            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new Gson();
                gson.toJson(score, writer);
                System.out.println("Best score saved: " + score);
            }
        } catch (IOException e) {
            System.err.println("Failed to save best score: " + e.getMessage());
        }
    }

    public int loadBestScore() {
        File file = new File(SCORE_FILE);

        if (!file.exists()) {
            System.out.println("Score file does not exist, creating a new one.");
            saveDefaultScore(file);
            return 0;
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Integer.class);
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Failed to load best score: " + e.getMessage());
            return 0;
        }
    }

    private void saveDefaultScore(File file) {
        try {
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("0");
                    System.out.println("Default score file created.");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to create default score file: " + e.getMessage());
        }
    }
}
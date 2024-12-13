package com.example.arkanoid.function;

public class ArkanoidScore {
    private int score;
    private long cooldownPoint;

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
}

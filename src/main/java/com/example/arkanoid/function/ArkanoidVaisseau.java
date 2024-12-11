package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ArkanoidVaisseau {
    private Rectangle vaisseau;
    private double vitesse = 7;
    private int vies = 3;
    private boolean superDashActive = false;
    private long superDashEndTime = 0;
    private static final long SUPER_DASH_DURATION = 5000000000L;
    private static final long SUPER_DASH_COOLDOWN = 30000000000L;

    public ArkanoidVaisseau() {
        vaisseau = new Rectangle(100, 20, Color.GREEN);
        vaisseau.setX(350);
        vaisseau.setY(920);
    }

    public Rectangle getVaisseau() {
        return vaisseau;
    }

    public void moveLeft() {
        if (vaisseau.getX() > 0) {
            vaisseau.setX(vaisseau.getX() - vitesse);
        }
    }

    public void moveRight(double sceneWidth) {
        if (vaisseau.getX() + vaisseau.getWidth() < sceneWidth) {
            vaisseau.setX(vaisseau.getX() + vitesse);
        }
    }

    public void activateSuperDash() {
        long currentTime = System.nanoTime();
        if (!superDashActive && (currentTime - superDashEndTime) >= SUPER_DASH_COOLDOWN) {
            superDashActive = true;
            vitesse = 14;
            superDashEndTime = currentTime + SUPER_DASH_DURATION;
        }
    }

    public void update() {
        long currentTime = System.nanoTime();
        if (superDashActive && currentTime >= superDashEndTime) {
            superDashActive = false;
            vitesse = 7;
        }
    }

    public int getVies() {
        return vies;
    }

    public void perdreVie() {
        if (vies > 0) {
            vies--;
            mettreAJourCouleur();
        }
    }

    private void mettreAJourCouleur() {
        switch (vies) {
            case 3 -> vaisseau.setFill(Color.GREEN);
            case 2 -> vaisseau.setFill(Color.YELLOW);
            case 1 -> vaisseau.setFill(Color.RED);
            default -> vaisseau.setFill(Color.GRAY);
        }
    }

    public boolean estMort() {
        return vies <= 0;
    }

    public boolean isSuperDashActive() {
        return superDashActive;
    }

    public long getSuperDashCooldownEndTime() {
        return superDashEndTime;
    }
}



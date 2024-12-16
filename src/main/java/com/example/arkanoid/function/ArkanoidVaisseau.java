package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ArkanoidVaisseau {
    private Rectangle vaisseau;
    private double vitesse = 7;
    private int vies = 4;
    private boolean superDashActive = false;
    private long superDashEndTime = 0;
    private long superDashCooldownEndTime = 0;
    private boolean isExpanded = false;
    private long expandEndTime = 0;
    private static final long SUPER_DASH_DURATION = 5000000000L;
    private static final long SUPER_DASH_COOLDOWN = 30000000000L;
    private static final long EXPAND_DURATION = 10000000000L;

    public ArkanoidVaisseau() {
        vaisseau = new Rectangle(100, 20, Color.WHITE);
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
        if (!superDashActive && currentTime >= superDashCooldownEndTime) {
            superDashActive = true;
            vitesse = 14;
            superDashEndTime = currentTime + SUPER_DASH_DURATION;
            superDashCooldownEndTime = superDashEndTime + SUPER_DASH_COOLDOWN;
        }
    }

    public void activateExpand() {
        isExpanded = true;
        expandEndTime = System.nanoTime() + EXPAND_DURATION;
        vaisseau.setWidth(400);
    }

    public void update() {
        long currentTime = System.nanoTime();
        if (superDashActive && currentTime >= superDashEndTime) {
            superDashActive = false;
            vitesse = 7;
        }
        if (isExpanded && currentTime >= expandEndTime) {
            isExpanded = false;
            vaisseau.setWidth(100);
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

    public void ajouterVie() {
        if (vies < 5) {
            vies++;
            mettreAJourCouleur();
        }
    }

    private void mettreAJourCouleur() {
        switch (vies) {
            case 5 -> vaisseau.setFill(Color.BLUE);
            case 4 -> vaisseau.setFill(Color.WHITE);
            case 3 -> vaisseau.setFill(Color.GREEN);
            case 2 -> vaisseau.setFill(Color.YELLOW);
            case 1 -> vaisseau.setFill(Color.RED);
        }
    }

    public boolean estMort() {
        return vies <= 0;
    }

    public boolean isSuperDashActive() {
        return superDashActive;
    }

    public long getSuperDashCooldownEndTime() {
        return superDashCooldownEndTime;
    }

    public long getSuperDashEndTime() {
        return superDashEndTime;
    }
}
package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ArkanoidVaisseau {
    private Rectangle vaisseau;
    private double vitesse = 7;
    private int vies = 3;

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
}

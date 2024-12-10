package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ArkanoidVaisseau {
    private Rectangle vaisseau;
    private double vitesse = 7;

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
}

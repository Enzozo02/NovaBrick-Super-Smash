package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ArkanoidBall {
    private Circle ball;
    private double vitesseX = 1;
    private double vitesseY = -1;
    private double radius = 10;
    private double acceleration = 0.05;
    private double maxSpeed = 8;

    private boolean isAccelerated = false;

    public ArkanoidBall() {
        ball = new Circle(radius, Color.RED);
        // Position initiale de la balle centrée
        ball.setCenterX(400);
        ball.setCenterY(500);
    }

    public Circle getBall() {
        return ball;
    }

    public void centerBall(double sceneWidth, double sceneHeight) {
        ball.setCenterX(sceneWidth / 2);
        ball.setCenterY(sceneHeight / 2);
    }

    public void move() {
        ball.setCenterX(ball.getCenterX() + vitesseX);
        ball.setCenterY(ball.getCenterY() + vitesseY);

        if (isAccelerated) {
            if (Math.abs(vitesseX) < maxSpeed) {
                vitesseX += (vitesseX > 0) ? acceleration : -acceleration;
            }
            if (Math.abs(vitesseY) < maxSpeed) {
                vitesseY += (vitesseY > 0) ? acceleration : -acceleration;
            }
            isAccelerated = false;
        }
    }

    public void checkWallCollision(double sceneWidth, double sceneHeight) {
        if (ball.getCenterX() - radius <= 0 || ball.getCenterX() + radius >= sceneWidth) {
            vitesseX = -vitesseX;
            isAccelerated = true;
        }
        if (ball.getCenterY() - radius <= 0) {
            vitesseY = -vitesseY;
            isAccelerated = true;
        }
    }

    public void checkVaisseauCollision(double vaisseauX, double vaisseauY, double vaisseauWidth, double vaisseauHeight) {
        if (ball.getCenterY() + radius >= vaisseauY && ball.getCenterY() - radius <= vaisseauY + vaisseauHeight &&
                ball.getCenterX() + radius >= vaisseauX && ball.getCenterX() - radius <= vaisseauX + vaisseauWidth) {
            vitesseY = -vitesseY;
            isAccelerated = true;
        }
    }
}

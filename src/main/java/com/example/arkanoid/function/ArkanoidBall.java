package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ArkanoidBall {
    private Circle ball;
    private double vitesseX = 2;
    private double vitesseY = -2;
    private double radius = 10;

    public ArkanoidBall() {
        ball = new Circle(radius, Color.WHITE);
        ball.setCenterX(400);
        ball.setCenterY(300);
    }

    public Circle getBall() {
        return ball;
    }

    public void move() {
        ball.setCenterX(ball.getCenterX() + vitesseX);
        ball.setCenterY(ball.getCenterY() + vitesseY);
    }

    public void checkWallCollision(double sceneWidth, double sceneHeight) {
        if (ball.getCenterX() - radius <= 0 || ball.getCenterX() + radius >= sceneWidth) {
            vitesseX = -vitesseX;
        }
        if (ball.getCenterY() - radius <= 0) {
            vitesseY = -vitesseY;
        }
    }

    public boolean checkVaisseauCollision(double vaisseauX, double vaisseauY, double vaisseauWidth, double vaisseauHeight) {
        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();

        boolean collision = ballX + radius >= vaisseauX && ballX - radius <= vaisseauX + vaisseauWidth &&
                ballY + radius >= vaisseauY && ballY - radius <= vaisseauY + vaisseauHeight;

        if (collision) {
            double relativeX = ballX - (vaisseauX + vaisseauWidth / 2);
            double reboundFactor = relativeX / (vaisseauWidth / 2);
            vitesseY = -Math.abs(vitesseY);
            vitesseX += reboundFactor * 2;
        }

        return collision;
    }



    public boolean checkBrickCollision(Rectangle brick) {
        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickWidth = brick.getWidth();
        double brickHeight = brick.getHeight();

        boolean collision = ballX + radius >= brickX && ballX - radius <= brickX + brickWidth &&
                ballY + radius >= brickY && ballY - radius <= brickY + brickHeight;

        if (collision) {
            vitesseY = -vitesseY;
        }

        return collision;
    }

    public void resetPosition(double sceneWidth, double sceneHeight) {

        ball.setCenterX(sceneWidth / 2);
        ball.setCenterY(sceneHeight / 2);

        vitesseX = Math.random() > 0.5 ? 2 : -2;
        vitesseY = -2;
    }
}

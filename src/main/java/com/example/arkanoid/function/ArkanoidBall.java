package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class ArkanoidBall {
    private Circle ball;
    private double vitesseX = 2;
    private double vitesseY = -2;
    private double radius = 10;
    private Media collisionSound;
    private MediaPlayer mediaPlayer;

    public ArkanoidBall() {
        ball = new Circle(radius, Color.WHITE);
        ball.setCenterX(400);
        ball.setCenterY(300);
        loadCollisionSound();
    }

    public ArkanoidBall(double startX, double startY) {
        ball = new Circle(radius, Color.GREEN);
        ball.setCenterX(startX);
        ball.setCenterY(startY);
        loadCollisionSound();
    }

    private void loadCollisionSound() {
        collisionSound = new Media(Paths.get("resources/audio/impact.mp3").toUri().toString());
        mediaPlayer = new MediaPlayer(collisionSound);

        mediaPlayer.setVolume(0.4);
    }

    private void playCollisionSound() {
        mediaPlayer.stop();
        mediaPlayer.play();
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
            playCollisionSound();
        }
        if (ball.getCenterY() - radius <= 0) {
            vitesseY = -vitesseY;
            playCollisionSound();
        }
    }

    public boolean checkBrickCollision(Rectangle brick) {
        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickWidth = brick.getWidth();
        double brickHeight = brick.getHeight();

        if (ballX + radius >= brickX && ballX - radius <= brickX + brickWidth &&
                ballY + radius >= brickY && ballY - radius <= brickY + brickHeight) {

            double overlapLeft = Math.abs(ballX + radius - brickX);
            double overlapRight = Math.abs(ballX - radius - (brickX + brickWidth));
            double overlapTop = Math.abs(ballY + radius - brickY);
            double overlapBottom = Math.abs(ballY - radius - (brickY + brickHeight));

            double minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

            if (minOverlap == overlapTop || minOverlap == overlapBottom) {
                vitesseY = -vitesseY;
            } else {
                vitesseX = -vitesseX;
            }

            playCollisionSound();
            return true;
        }
        return false;
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
            playCollisionSound();
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

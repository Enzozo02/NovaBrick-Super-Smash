package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class ArkanoidBall {
    private Circle ball; // Représente l'objet graphique de la balle
    private double vitesseX = 2; // Vitesse de la balle sur l'axe X
    private double vitesseY = -2; // Vitesse de la balle sur l'axe Y
    private double radius = 10; // Rayon de la balle
    private Media collisionSound; // Fichier audio pour les collisions
    private MediaPlayer mediaPlayer; // Lecteur multimédia pour jouer le son

    // Constructeur par défaut : la balle est placée au centre de l'écran
    public ArkanoidBall() {
        ball = new Circle(radius, Color.WHITE); // Crée une balle blanche
        ball.setCenterX(400); // Position initiale en X
        ball.setCenterY(300); // Position initiale en Y
        loadCollisionSound(); // Charge le son de collision
    }

    // Constructeur personnalisé : la balle est placée à des coordonnées spécifiques
    public ArkanoidBall(double startX, double startY) {
        ball = new Circle(radius, Color.GREEN); // Crée une balle verte
        ball.setCenterX(startX); // Position initiale en X
        ball.setCenterY(startY); // Position initiale en Y
        loadCollisionSound(); // Charge le son de collision
    }

    // Charge le fichier audio pour les collisions
    private void loadCollisionSound() {
        collisionSound = new Media(Paths.get("resources/audio/impact.mp3").toUri().toString());
        mediaPlayer = new MediaPlayer(collisionSound);

        mediaPlayer.setVolume(0.4); // Réduit le volume du son à 40%
    }

    // Joue le son de collision
    private void playCollisionSound() {
        mediaPlayer.stop(); // Arrête la lecture précédente, si elle est en cours
        mediaPlayer.play(); // Joue le son de la collision
    }

    // Retourne l'objet graphique représentant la balle
    public Circle getBall() {
        return ball;
    }

    // Déplace la balle en fonction de sa vitesse actuelle
    public void move() {
        ball.setCenterX(ball.getCenterX() + vitesseX); // Déplace la balle sur l'axe X
        ball.setCenterY(ball.getCenterY() + vitesseY); // Déplace la balle sur l'axe Y
    }

    // Vérifie les collisions avec les murs (haut, bas, gauche, droite)
    public void checkWallCollision(double sceneWidth, double sceneHeight) {
        // Collision avec les murs gauche et droit
        if (ball.getCenterX() - radius <= 0 || ball.getCenterX() + radius >= sceneWidth) {
            vitesseX = -vitesseX; // Inverse la direction sur l'axe X
            playCollisionSound(); // Joue le son de la collision
        }

        // Collision avec le mur du haut
        if (ball.getCenterY() - radius <= 0) {
            vitesseY = -vitesseY; // Inverse la direction sur l'axe Y
            playCollisionSound(); // Joue le son de la collision
        }
    }

    // Vérifie les collisions avec les briques
    public boolean checkBrickCollision(Rectangle brick) {
        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickWidth = brick.getWidth();
        double brickHeight = brick.getHeight();

        // Vérifie si la balle entre en collision avec la brique
        if (ballX + radius >= brickX && ballX - radius <= brickX + brickWidth &&
                ballY + radius >= brickY && ballY - radius <= brickY + brickHeight) {

            // Détermine quelle partie de la brique la balle touche (haut, bas, gauche, droite)
            double overlapLeft = Math.abs(ballX + radius - brickX);
            double overlapRight = Math.abs(ballX - radius - (brickX + brickWidth));
            double overlapTop = Math.abs(ballY + radius - brickY);
            double overlapBottom = Math.abs(ballY - radius - (brickY + brickHeight));

            double minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

            // Inverse la direction de la balle en fonction de la partie de la brique touchée
            if (minOverlap == overlapTop || minOverlap == overlapBottom) {
                vitesseY = -vitesseY; // Collision avec le haut ou le bas
            } else {
                vitesseX = -vitesseX; // Collision avec les côtés
            }

            playCollisionSound(); // Joue le son de la collision
            return true; // Indique qu'une collision a eu lieu
        }
        return false; // Pas de collision
    }

    // Vérifie les collisions avec le vaisseau
    public boolean checkVaisseauCollision(double vaisseauX, double vaisseauY, double vaisseauWidth, double vaisseauHeight) {
        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();

        // Vérifie si la balle entre en collision avec le vaisseau
        boolean collision = ballX + radius >= vaisseauX && ballX - radius <= vaisseauX + vaisseauWidth &&
                ballY + radius >= vaisseauY && ballY - radius <= vaisseauY + vaisseauHeight;

        // Si une collision se produit, inverse la direction de la balle
        if (collision) {
            double relativeX = ballX - (vaisseauX + vaisseauWidth / 2); // Calcul du point de collision relatif
            double reboundFactor = relativeX / (vaisseauWidth / 2); // Calcul du facteur de rebond
            vitesseY = -Math.abs(vitesseY); // La balle rebondit toujours vers le haut
            vitesseX += reboundFactor * 2; // La balle rebondit plus ou moins fort en fonction de l'endroit du vaisseau qu'elle a frappé
            playCollisionSound(); // Joue le son de la collision
        }

        return collision; // Retourne si une collision a eu lieu
    }

    // Réinitialise la position de la balle au centre de l'écran
    public void resetPosition(double sceneWidth, double sceneHeight) {
        ball.setCenterX(sceneWidth / 2); // Positionne la balle au centre en X
        ball.setCenterY(sceneHeight / 2); // Positionne la balle au centre en Y

        // Donne une vitesse aléatoire sur l'axe X et une vitesse fixe sur l'axe Y
        vitesseX = Math.random() > 0.5 ? 2 : -2; // Donne une vitesse aléatoire pour un mouvement de départ
        vitesseY = -2; // La balle commence toujours à se déplacer vers le haut
    }
}

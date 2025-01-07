package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class ArkanoidBonus {
    private Circle bonus; // Représente l'élément graphique du bonus
    private double speed = 3; // Vitesse du bonus (en fonction de sa descente)
    private String type; // Type de bonus (par exemple, "expand", "life", "multiball")
    private boolean active = true; // Détermine si le bonus est actif ou non

    private Media bonusSound; // Fichier sonore du bonus
    private MediaPlayer bonusPlayer; // Lecteur multimédia pour jouer le son

    // Méthode pour charger le son du bonus
    private void loadBonusSound() {
        // Charge le fichier audio pour le bonus
        bonusSound = new Media(Paths.get("resources/audio/bonus.mp3").toUri().toString());
        bonusPlayer = new MediaPlayer(bonusSound);

        bonusPlayer.setVolume(0.4); // Définit le volume du son du bonus
    }

    // Méthode pour jouer le son du bonus
    public void playBonusSound() {
        bonusPlayer.stop(); // Arrête la lecture précédente si elle était en cours
        bonusPlayer.play(); // Joue le son du bonus
    }

    // Constructeur pour créer un bonus avec une position (x, y) et un type donné
    public ArkanoidBonus(double x, double y, String type) {
        this.type = type;
        bonus = new Circle(10); // Crée un cercle représentant le bonus avec un rayon de 10
        bonus.setCenterX(x); // Positionne le cercle à la coordonnée X spécifiée
        bonus.setCenterY(y); // Positionne le cercle à la coordonnée Y spécifiée
        loadBonusSound(); // Charge le son du bonus

        // Définit la couleur du bonus en fonction de son type
        switch (type) {
            case "expand":
                bonus.setFill(Color.BLUE); // Bonus d'expansion, couleur bleue
                break;
            case "life":
                bonus.setFill(Color.GREEN); // Bonus de vie, couleur verte
                break;
            case "multiball":
                bonus.setFill(Color.ORANGE); // Bonus multiball, couleur orange
                break;
        }
    }

    // Retourne l'objet graphique (cercle) du bonus
    public Circle getBonus() {
        return bonus;
    }

    // Méthode pour déplacer le bonus vers le bas
    public void move() {
        bonus.setCenterY(bonus.getCenterY() + speed); // Déplace le bonus vers le bas
        if (bonus.getCenterY() > 1080) {
            active = false; // Si le bonus dépasse la fenêtre, il devient inactif
        }
    }

    // Vérifie la collision entre le bonus et le vaisseau
    public boolean checkCollision(Rectangle vaisseau) {
        return vaisseau.getBoundsInParent().intersects(bonus.getBoundsInParent()); // Vérifie si les zones de collision du vaisseau et du bonus se croisent
    }

    // Retourne si le bonus est actif (si il est encore dans la fenêtre)
    public boolean isActive() {
        return active;
    }

    // Retourne le type de bonus (par exemple "expand", "life", "multiball")
    public String getType() {
        return type;
    }
}

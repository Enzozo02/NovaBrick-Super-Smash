package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;


public class ArkanoidBonus {
    private Circle bonus;
    private double speed = 3;
    private String type;
    private boolean active = true;


    private Media bonusSound;
    private MediaPlayer bonusPlayer;

    private void loadBonusSound() {
        bonusSound = new Media(Paths.get("resources/audio/bonus.mp3").toUri().toString());
        bonusPlayer = new MediaPlayer(bonusSound);

        bonusPlayer.setVolume(0.4);
    }

    public void playBonusSound() {
        bonusPlayer.stop();
        bonusPlayer.play();
    }

    public ArkanoidBonus(double x, double y, String type) {
        this.type = type;
        bonus = new Circle(10);
        bonus.setCenterX(x);
        bonus.setCenterY(y);
        loadBonusSound();

        switch (type) {
            case "expand":
                bonus.setFill(Color.BLUE);
                break;
            case "life":
                bonus.setFill(Color.GREEN);
                break;
            case "multiball":
                bonus.setFill(Color.ORANGE);
                break;
        }
    }

    public Circle getBonus() {
        return bonus;
    }

    public void move() {
        bonus.setCenterY(bonus.getCenterY() + speed);
        if (bonus.getCenterY() > 1080) {
            active = false;
        }
    }

    public boolean checkCollision(Rectangle vaisseau) {
        return vaisseau.getBoundsInParent().intersects(bonus.getBoundsInParent());
    }

    public boolean isActive() {
        return active;
    }

    public String getType() {
        return type;
    }
}

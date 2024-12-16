package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ArkanoidBonus {
    private Circle bonus;
    private double speed = 3;
    private String type;
    private boolean active = true;

    public ArkanoidBonus(double x, double y, String type) {
        this.type = type;
        bonus = new Circle(10);
        bonus.setCenterX(x);
        bonus.setCenterY(y);

        if (type.equals("expand")) {
            bonus.setFill(Color.BLUE);
        } else if (type.equals("life")) {
            bonus.setFill(Color.GREEN);
        }
    }

    public Circle getBonus() {
        return bonus;
    }

    public void move() {
        bonus.setCenterY(bonus.getCenterY() + speed);
        if (bonus.getCenterY() > 1000) {
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

package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ArkanoidBrick {
    private Rectangle brick;
    private int durability;

    public ArkanoidBrick(double x, double y, double width, double height, int durability) {
        this.durability = durability;

        brick = new Rectangle(width, height);
        brick.setX(x);
        brick.setY(y);

        updateColor();
    }

    public Rectangle getBrick() {
        return brick;
    }

    public void hit() {
        if (durability > 0) {
            durability--;
            updateColor();
        }
    }

    public boolean isBroken() {
        return durability <= 0;
    }

    private void updateColor() {
        switch (durability) {
            case 3:
                brick.setFill(Color.RED);
                break;
            case 2:
                brick.setFill(Color.ORANGE);
                break;
            case 1:
                brick.setFill(Color.YELLOW);
                break;
            default:
                brick.setFill(Color.TRANSPARENT);
                break;
        }
    }
}

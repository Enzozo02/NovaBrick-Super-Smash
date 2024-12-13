package com.example.arkanoid.function;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

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

    public static List<ArkanoidBrick> generateBricks(Pane gameLayout, int rows, int cols, double brickWidth, double brickHeight) {
        List<ArkanoidBrick> bricks = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = col * (brickWidth + 5) + 50;
                double y = row * (brickHeight + 5) + 50;
                ArkanoidBrick brick = new ArkanoidBrick(x, y, brickWidth, brickHeight, 3);
                bricks.add(brick);
                gameLayout.getChildren().add(brick.getBrick());
            }
        }
        return bricks;
    }
}

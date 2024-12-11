package com.example.arkanoid;

import com.example.arkanoid.function.ArkanoidVaisseau;
import com.example.arkanoid.function.ArkanoidBall;
import com.example.arkanoid.function.ArkanoidBrick;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class JeuArkanoid {

    public Scene createGameScene(Stage primaryStage, ArkanoidMenu menu) {

        ArkanoidVaisseau vaisseau = new ArkanoidVaisseau();
        ArkanoidBall ball = new ArkanoidBall();
        List<ArkanoidBrick> bricks = new ArrayList<>();

        Pane gameLayout = new Pane();
        gameLayout.setStyle("-fx-background-color: #000;");

        gameLayout.getChildren().addAll(vaisseau.getVaisseau(), ball.getBall());

        int rows = 4;
        int cols = 21;
        double brickWidth = 80;
        double brickHeight = 30;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = col * (brickWidth + 5) + 50;
                double y = row * (brickHeight + 5) + 50;
                ArkanoidBrick brick = new ArkanoidBrick(x, y, brickWidth, brickHeight, 3);
                bricks.add(brick);
                gameLayout.getChildren().add(brick.getBrick());
            }
        }

        Scene gameScene = new Scene(gameLayout, 800, 600);

        boolean[] keys = new boolean[256];

        gameScene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.Q) {
                keys[KeyCode.Q.getCode()] = true;
            } else if (code == KeyCode.D) {
                keys[KeyCode.D.getCode()] = true;
            }
        });

        gameScene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.Q) {
                keys[KeyCode.Q.getCode()] = false;
            } else if (code == KeyCode.D) {
                keys[KeyCode.D.getCode()] = false;
            }
        });

        gameLayout.requestFocus();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (keys[KeyCode.Q.getCode()]) {
                    vaisseau.moveLeft();
                }
                if (keys[KeyCode.D.getCode()]) {
                    vaisseau.moveRight(gameScene.getWidth());
                }

                ball.move();
                ball.checkWallCollision(gameScene.getWidth(), gameScene.getHeight());
                ball.checkVaisseauCollision(vaisseau.getVaisseau().getX(), vaisseau.getVaisseau().getY(),
                        vaisseau.getVaisseau().getWidth(), vaisseau.getVaisseau().getHeight());

                for (ArkanoidBrick brick : new ArrayList<>(bricks)) {
                    if (!brick.isBroken() && ball.checkBrickCollision(brick.getBrick())) {
                        brick.hit();
                        if (brick.isBroken()) {
                            gameLayout.getChildren().remove(brick.getBrick());
                            bricks.remove(brick);
                        }
                        break;
                    }
                }


                if (ball.getBall().getCenterY() >= gameScene.getHeight()) {
                    vaisseau.perdreVie();
                    if (vaisseau.estMort()) {
                        stopGame(primaryStage, menu);
                        this.stop();
                    } else {

                        ball.resetPosition(gameScene.getWidth(), gameScene.getHeight());
                    }
                }
            }
        };
        gameLoop.start();

        return gameScene;
    }

    private void stopGame(Stage primaryStage, ArkanoidMenu menu) {

        primaryStage.setScene(menu.getMainMenuScene());
        primaryStage.setFullScreen(true);
    }
}


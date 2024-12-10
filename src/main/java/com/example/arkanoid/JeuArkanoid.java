package com.example.arkanoid;

import com.example.arkanoid.function.ArkanoidVaisseau;
import com.example.arkanoid.function.ArkanoidBall;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class JeuArkanoid {

    public Scene createGameScene(Stage primaryStage) {

        ArkanoidVaisseau vaisseau = new ArkanoidVaisseau();
        ArkanoidBall ball = new ArkanoidBall();

        // Créer le layout de la scène
        Pane gameLayout = new Pane();
        gameLayout.setStyle("-fx-background-color: #000;");

        gameLayout.getChildren().addAll(vaisseau.getVaisseau(), ball.getBall());

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

        new AnimationTimer() {
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
            }
        }.start();

        return gameScene;
    }
}

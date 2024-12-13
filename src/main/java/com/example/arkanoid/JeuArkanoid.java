package com.example.arkanoid;

import com.example.arkanoid.function.ArkanoidVaisseau;
import com.example.arkanoid.function.ArkanoidBall;
import com.example.arkanoid.function.ArkanoidBrick;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

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

        Text timerText = new Text();
        timerText.setFont(Font.font(20));
        timerText.setFill(Color.WHITE);
        gameLayout.getChildren().add(timerText);

        gameLayout.widthProperty().addListener((observable, oldValue, newValue) -> {
            timerText.setX(newValue.doubleValue() - 200);
        });
        gameLayout.heightProperty().addListener((observable, oldValue, newValue) -> {
            timerText.setY(newValue.doubleValue() - 20);
        });

        Scene gameScene = new Scene(gameLayout, 800, 600);

        boolean[] keys = new boolean[256];

        gameScene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.Q) {
                keys[KeyCode.Q.getCode()] = true;
            } else if (code == KeyCode.D) {
                keys[KeyCode.D.getCode()] = true;
            } else if (code == KeyCode.SPACE) {
                vaisseau.activateSuperDash();
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
                vaisseau.update();

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

                long currentTime = System.nanoTime();
                long cooldownRemaining = (vaisseau.getSuperDashCooldownEndTime() - currentTime) / 1_000_000_000L;

                if (vaisseau.isSuperDashActive()) {
                    long dashRemaining = Math.max(0, (vaisseau.getSuperDashEndTime() - currentTime) / 1_000_000_000L);
                    timerText.setText("Super Dash actif: " + dashRemaining + "s");
                } else {

                    if (cooldownRemaining > 0) {

                        timerText.setText("Rechargement: " + cooldownRemaining + "s");
                    } else {

                        timerText.setText("Super Dash prêt !");
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

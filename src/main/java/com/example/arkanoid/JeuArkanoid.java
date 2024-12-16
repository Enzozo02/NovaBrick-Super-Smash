package com.example.arkanoid;

import com.example.arkanoid.function.ArkanoidVaisseau;
import com.example.arkanoid.function.ArkanoidBall;
import com.example.arkanoid.function.ArkanoidBrick;
import com.example.arkanoid.function.ArkanoidScore;
import com.example.arkanoid.function.ArkanoidGameover;
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

    private ArkanoidScore score;
    private boolean isGameOver = false;

    public Scene createGameScene(Stage primaryStage, ArkanoidMenu menuPrincipal) {

        ArkanoidVaisseau vaisseau = new ArkanoidVaisseau();
        ArkanoidBall ball = new ArkanoidBall();
        score = new ArkanoidScore();

        Pane gameLayout = new Pane();
        gameLayout.setStyle("-fx-background-color: #000;");
        gameLayout.getChildren().addAll(vaisseau.getVaisseau(), ball.getBall());

        List<ArkanoidBrick> bricks = ArkanoidBrick.generateBricks(gameLayout, 4, 21, 80, 30);

        Text scoreText = new Text("Score: " + score.getScore());
        scoreText.setFont(Font.font(20));
        scoreText.setFill(Color.WHITE);
        scoreText.setX(20);
        scoreText.setY(gameLayout.getHeight() - 20);
        gameLayout.getChildren().add(scoreText);

        Text timerText = new Text();
        timerText.setFont(Font.font(20));
        timerText.setFill(Color.WHITE);
        gameLayout.getChildren().add(timerText);

        Text livesText = new Text("Vies restantes: " + vaisseau.getVies());
        livesText.setFont(Font.font(20));
        livesText.setFill(Color.WHITE);
        livesText.setX(20);
        livesText.setY(gameLayout.getHeight() - 20);
        gameLayout.getChildren().add(livesText);

        Text startText = new Text("Appuyez sur Entrée pour commencer");
        startText.setFont(Font.font(30));
        startText.setFill(Color.YELLOW);
        gameLayout.getChildren().add(startText);

        gameLayout.widthProperty().addListener((observable, oldValue, newValue) -> {
            timerText.setX(newValue.doubleValue() - 190);
            scoreText.setX(newValue.doubleValue() / 2.1);
            startText.setX((newValue.doubleValue() - startText.getBoundsInLocal().getWidth()) / 2);
        });
        gameLayout.heightProperty().addListener((observable, oldValue, newValue) -> {
            timerText.setY(newValue.doubleValue() - 20);
            livesText.setY(newValue.doubleValue() - 20);
            scoreText.setY(newValue.doubleValue() - 20);
            startText.setY((newValue.doubleValue() - startText.getBoundsInLocal().getHeight()) / 2);
        });

        Scene gameScene = new Scene(gameLayout, 800, 600);

        boolean[] keys = new boolean[256];
        final boolean[] gameStarted = {false};

        gameScene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.Q) {
                keys[KeyCode.Q.getCode()] = true;
            } else if (code == KeyCode.D) {
                keys[KeyCode.D.getCode()] = true;
            } else if (code == KeyCode.SPACE) {
                vaisseau.activateSuperDash();
            } else if (code == KeyCode.ENTER) {
                if (!gameStarted[0]) {
                    gameStarted[0] = true;
                    gameLayout.getChildren().remove(startText);
                }
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
                if (!gameStarted[0] || isGameOver) {
                    return;
                }

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
                        score.incrementScoreOnTouch();
                        if (brick.isBroken()) {
                            gameLayout.getChildren().remove(brick.getBrick());
                            bricks.remove(brick);
                            score.incrementScoreOnBreak();
                        }
                        break;
                    }
                }

                if (bricks.isEmpty()) {
                    bricks.addAll(ArkanoidBrick.generateBricks(gameLayout, 4, 21, 80, 30));
                }

                score.updateScoreWithTimeBonus();

                scoreText.setText("Score: " + score.getScore());

                if (ball.getBall().getCenterY() >= gameScene.getHeight()) {
                    vaisseau.perdreVie();
                    livesText.setText("Vies restantes: " + vaisseau.getVies());

                    if (vaisseau.estMort()) {
                        triggerGameOver(primaryStage, menuPrincipal);
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

    private void triggerGameOver(Stage primaryStage, ArkanoidMenu menuPrincipal) {
        if (!isGameOver) {
            isGameOver = true;
            ArkanoidGameover gameOver = new ArkanoidGameover();
            gameOver.showGameOverScene(primaryStage, menuPrincipal.getMainMenuScene(), score);
        }
    }
}

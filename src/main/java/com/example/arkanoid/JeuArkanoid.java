package com.example.arkanoid;

import com.example.arkanoid.function.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JeuArkanoid {

    private ArkanoidScore score;
    private boolean isGameOver = false;

    private Media musicSound;
    private MediaPlayer musicPlayer;

    public Scene createGameScene(Stage primaryStage, ArkanoidMenu menuPrincipal) {

        ArkanoidVaisseau vaisseau = new ArkanoidVaisseau();
        ArkanoidBall mainBall = new ArkanoidBall();
        score = new ArkanoidScore();

        Pane gameLayout = new Pane();

        Media video = new Media(Paths.get("resources/video/background.mp4").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(video);

        javafx.scene.media.MediaView mediaView = new javafx.scene.media.MediaView(mediaPlayer);

        mediaView.setFitWidth(1920);
        mediaView.setFitHeight(1080);
        mediaView.setPreserveRatio(false);

        gameLayout.getChildren().add(mediaView);
        gameLayout.getChildren().addAll(vaisseau.getVaisseau(), mainBall.getBall());

        List<ArkanoidBrick> bricks = ArkanoidBrick.generateBricks(gameLayout, 4, 21, 80, 30);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0);
        mediaPlayer.play();

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

        musicSound = new Media(Paths.get("resources/audio/music.mp3").toUri().toString());
        musicPlayer = new MediaPlayer(musicSound);

        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.20);
        musicPlayer.play();

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
            List<ArkanoidBonus> activeBonuses = new ArrayList<>();
            List<ArkanoidBall> balls = new ArrayList<>();
            Random random = new Random();

            {
                balls.add(mainBall); // Ajouter la balle principale
            }

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

                for (ArkanoidBall ball : new ArrayList<>(balls)) {
                    ball.move();
                    ball.checkWallCollision(gameScene.getWidth(), gameScene.getHeight());
                    ball.checkVaisseauCollision(
                            vaisseau.getVaisseau().getX(),
                            vaisseau.getVaisseau().getY(),
                            vaisseau.getVaisseau().getWidth(),
                            vaisseau.getVaisseau().getHeight()
                    );

                    for (ArkanoidBrick brick : new ArrayList<>(bricks)) {
                        if (!brick.isBroken() && ball.checkBrickCollision(brick.getBrick())) {
                            brick.hit();
                            score.incrementScoreOnTouch();
                            if (brick.isBroken()) {
                                gameLayout.getChildren().remove(brick.getBrick());
                                bricks.remove(brick);
                                score.incrementScoreOnBreak();

                                if (random.nextDouble() < 0.2) {
                                    double bonusChance = random.nextDouble();
                                    String bonusType;
                                    if (bonusChance < 0.33) {
                                        bonusType = "expand";
                                    } else if (bonusChance < 0.66) {
                                        bonusType = "multiball";
                                    } else {
                                        bonusType = "life";
                                    }
                                    ArkanoidBonus bonus = new ArkanoidBonus(
                                            brick.getBrick().getX(),
                                            brick.getBrick().getY(),
                                            bonusType
                                    );
                                    activeBonuses.add(bonus);
                                    gameLayout.getChildren().add(bonus.getBonus());
                                }
                            }
                            break;
                        }
                    }

                    if (ball.getBall().getCenterY() >= gameScene.getHeight()) {
                        if (ball == mainBall) {
                            vaisseau.perdreVie();
                            livesText.setText("Vies restantes: " + vaisseau.getVies());

                            if (vaisseau.estMort()) {
                                triggerGameOver(primaryStage, menuPrincipal);
                                this.stop();
                            } else {
                                mainBall.resetPosition(gameScene.getWidth(), gameScene.getHeight());
                            }
                        } else {
                            balls.remove(ball);
                            gameLayout.getChildren().remove(ball.getBall());
                        }
                    }
                }

                for (ArkanoidBonus bonus : new ArrayList<>(activeBonuses)) {
                    if (bonus.isActive()) {
                        bonus.move();

                        // Vérifier collision avec le vaisseau
                        if (bonus.checkCollision(vaisseau.getVaisseau())) {
                            bonus.playBonusSound();
                            switch (bonus.getType()) {
                                case "expand":
                                    vaisseau.activateExpand();
                                    break;

                                case "life":
                                    vaisseau.ajouterVie();
                                    livesText.setText("Vies restantes: " + vaisseau.getVies());
                                    break;

                                case "multiball":
                                    ArkanoidBall newBall = new ArkanoidBall(
                                            mainBall.getBall().getCenterX(),
                                            mainBall.getBall().getCenterY()
                                    );
                                    balls.add(newBall);
                                    gameLayout.getChildren().add(newBall.getBall());
                                    break;

                                default:
                                    // Si le type du bonus n'est pas géré, on peut loguer un avertissement
                                    System.out.println("Type de bonus inconnu : " + bonus.getType());
                                    break;
                            }

                            // Supprimer le bonus après utilisation
                            gameLayout.getChildren().remove(bonus.getBonus());
                            activeBonuses.remove(bonus);
                        }
                        // Vérifier si le bonus est tombé hors de l'écran
                        else if (bonus.getBonus().getBoundsInParent().getMaxY() >= gameScene.getHeight()) {
                            gameLayout.getChildren().remove(bonus.getBonus());
                            activeBonuses.remove(bonus);
                        }
                    } else {
                        // Supprimer les bonus inactifs
                        gameLayout.getChildren().remove(bonus.getBonus());
                        activeBonuses.remove(bonus);
                    }
                }

                if (bricks.isEmpty()) {
                    bricks.addAll(ArkanoidBrick.generateBricks(gameLayout, 4, 21, 80, 30));
                }

                score.updateScoreWithTimeBonus();
                scoreText.setText("Score: " + score.getScore());

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

            int bestScore = score.loadBestScore();

            if (score.getScore() > bestScore) {
                score.saveBestScore();
            }

            musicPlayer.stop();

            ArkanoidGameover gameOver = new ArkanoidGameover();
            gameOver.showGameOverScene(primaryStage, menuPrincipal.getMainMenuScene(), score);
        }
    }
}

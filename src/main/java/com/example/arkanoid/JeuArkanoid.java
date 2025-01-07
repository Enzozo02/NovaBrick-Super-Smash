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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Déclaration de la classe JeuArkanoid, qui gère la logique du jeu
public class JeuArkanoid {

    // Déclaration des variables liées au score, à l'état du jeu et à la musique
    private ArkanoidScore score;
    private boolean isGameOver = false;
    private Media musicSound;
    private MediaPlayer musicPlayer;

    // Méthode pour créer la scène du jeu
    public Scene createGameScene(Stage primaryStage, ArkanoidMenu menuPrincipal) {

        // Création d'instances pour le vaisseau, la balle principale et le score
        ArkanoidVaisseau vaisseau = new ArkanoidVaisseau();
        ArkanoidBall mainBall = new ArkanoidBall();
        score = new ArkanoidScore();

        // Création de la mise en page du jeu (Pane) où tous les éléments seront ajoutés
        Pane gameLayout = new Pane();

        // Chargement et configuration de la vidéo de fond
        Media video = new Media(Paths.get("resources/video/background.mp4").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(video);

        // Création d'une vue pour la vidéo et réglage de ses dimensions
        javafx.scene.media.MediaView mediaView = new javafx.scene.media.MediaView(mediaPlayer);
        mediaView.setFitWidth(1920);
        mediaView.setFitHeight(1080);
        mediaView.setPreserveRatio(false);

        // Ajout de la vidéo de fond à la mise en page
        gameLayout.getChildren().add(mediaView);

        // Ajout du vaisseau et de la balle principale à la mise en page
        gameLayout.getChildren().addAll(vaisseau.getVaisseau(), mainBall.getBall());

        // Génération des briques et ajout à la mise en page
        List<ArkanoidBrick> bricks = ArkanoidBrick.generateBricks(gameLayout, 4, 21, 80, 30);

        // Configuration et démarrage de la vidéo en boucle sans son
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0);
        mediaPlayer.play();

        // Création du texte pour afficher le score du joueur
        Text scoreText = new Text("Score: " + score.getScore());
        scoreText.setFont(Font.font(20));
        scoreText.setFill(Color.WHITE);
        scoreText.setX(20);
        scoreText.setY(gameLayout.getHeight() - 20);
        gameLayout.getChildren().add(scoreText);

        // Création du texte pour afficher le timer du jeu
        Text timerText = new Text();
        timerText.setFont(Font.font(20));
        timerText.setFill(Color.WHITE);
        gameLayout.getChildren().add(timerText);

        // Création du texte pour afficher le nombre de vies restantes
        Text livesText = new Text("Vies restantes: " + vaisseau.getVies());
        livesText.setFont(Font.font(20));
        livesText.setFill(Color.WHITE);
        livesText.setX(20);
        livesText.setY(gameLayout.getHeight() - 20);
        gameLayout.getChildren().add(livesText);

        // Texte initial pour inviter à démarrer le jeu
        Text startText = new Text("Appuyez sur Entrée pour commencer");
        startText.setFont(Font.font(30));
        startText.setFill(Color.YELLOW);
        gameLayout.getChildren().add(startText);

        // Ajustement de la position du texte lors du redimensionnement de la scène
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

        // Chargement et configuration de la musique de fond
        musicSound = new Media(Paths.get("resources/audio/music.mp3").toUri().toString());
        musicPlayer = new MediaPlayer(musicSound);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.20);
        musicPlayer.play();

        // Création de la scène du jeu avec une taille initiale de 800x600
        Scene gameScene = new Scene(gameLayout, 800, 600);

        // Tableau pour suivre l'état des touches pressées
        boolean[] keys = new boolean[256];
        // Variable pour savoir si le jeu a commencé
        final boolean[] gameStarted = {false};

        // Détection des touches pressées
        gameScene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.Q) {
                keys[KeyCode.Q.getCode()] = true;  // Déplacement à gauche
            } else if (code == KeyCode.D) {
                keys[KeyCode.D.getCode()] = true;  // Déplacement à droite
            } else if (code == KeyCode.SPACE) {
                vaisseau.activateSuperDash();  // Activation du Super Dash
            } else if (code == KeyCode.ENTER) {
                // Démarrage du jeu si ce n'est pas déjà fait
                if (!gameStarted[0]) {
                    gameStarted[0] = true;
                    gameLayout.getChildren().remove(startText);  // Retirer le texte de démarrage
                }
            }
        });

        // Détection des touches relâchées
        gameScene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.Q) {
                keys[KeyCode.Q.getCode()] = false;  // Arrêt du mouvement gauche
            } else if (code == KeyCode.D) {
                keys[KeyCode.D.getCode()] = false;  // Arrêt du mouvement droit
            }
        });

        // Demander à la mise en page de recevoir le focus
        gameLayout.requestFocus();

        // Création de la boucle principale du jeu (game loop) à l'aide de l'AnimationTimer
                AnimationTimer gameLoop = new AnimationTimer() {
                    // Initialisation des listes de bonus actifs, de balles et d'un générateur aléatoire
                    List<ArkanoidBonus> activeBonuses = new ArrayList<>();
                    List<ArkanoidBall> balls = new ArrayList<>();
                    Random random = new Random();

                    // Ajout de la balle principale à la liste des balles
                    {
                        balls.add(mainBall);
                    }

                    // Méthode appelée à chaque image (frame) de la boucle du jeu
                    @Override
                    public void handle(long now) {
                        // Si le jeu n'a pas commencé ou si le jeu est terminé, on interrompt la boucle
                        if (!gameStarted[0] || isGameOver) {
                            return;
                        }

                        // Mise à jour du vaisseau (mouvement, etc.)
                        vaisseau.update();

                        // Gestion du mouvement du vaisseau avec les touches Q et D
                        if (keys[KeyCode.Q.getCode()]) {
                            vaisseau.moveLeft();
                        }
                        if (keys[KeyCode.D.getCode()]) {
                            vaisseau.moveRight(gameScene.getWidth());
                        }

                        // Parcours de la liste des balles pour appliquer les mouvements et les collisions
                        for (ArkanoidBall ball : new ArrayList<>(balls)) {
                            // Déplacement de la balle
                            ball.move();
                            // Vérification des collisions avec les murs
                            ball.checkWallCollision(gameScene.getWidth(), gameScene.getHeight());
                            // Vérification des collisions avec le vaisseau
                            ball.checkVaisseauCollision(
                                    vaisseau.getVaisseau().getX(),
                                    vaisseau.getVaisseau().getY(),
                                    vaisseau.getVaisseau().getWidth(),
                                    vaisseau.getVaisseau().getHeight()
                            );

                            // Vérification des collisions avec les briques
                            for (ArkanoidBrick brick : new ArrayList<>(bricks)) {
                                if (!brick.isBroken() && ball.checkBrickCollision(brick.getBrick())) {
                                    brick.hit();
                                    // Augmentation du score lorsque la brique est touchée
                                    score.incrementScoreOnTouch();
                                    if (brick.isBroken()) {
                                        // Si la brique est détruite, on la retire de l'écran et de la liste des briques
                                        gameLayout.getChildren().remove(brick.getBrick());
                                        bricks.remove(brick);
                                        // Augmentation du score pour la destruction de la brique
                                        score.incrementScoreOnBreak();

                                        // Apparition d'un bonus avec une chance de 20%
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
                                            // Création et ajout du bonus à l'écran
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

                            // Si la balle tombe en bas de l'écran, on retire une vie ou on enlève la balle
                            if (ball.getBall().getCenterY() >= gameScene.getHeight()) {
                                if (ball == mainBall) {
                                    vaisseau.perdreVie();
                                    livesText.setText("Vies restantes: " + vaisseau.getVies());

                                    // Si toutes les vies sont perdues, on déclenche la fin du jeu
                                    if (vaisseau.estMort()) {
                                        triggerGameOver(primaryStage, menuPrincipal);
                                        this.stop();
                                    } else {
                                        // Réinitialisation de la position de la balle
                                        mainBall.resetPosition(gameScene.getWidth(), gameScene.getHeight());
                                    }
                                } else {
                                    // Suppression de la balle secondaire du jeu
                                    balls.remove(ball);
                                    gameLayout.getChildren().remove(ball.getBall());
                                }
                            }
                        }

                        // Gestion des bonus actifs
                        for (ArkanoidBonus bonus : new ArrayList<>(activeBonuses)) {
                            if (bonus.isActive()) {
                                // Déplacement du bonus
                                bonus.move();

                                // Vérification de la collision avec le vaisseau
                                if (bonus.checkCollision(vaisseau.getVaisseau())) {
                                    bonus.playBonusSound();
                                    switch (bonus.getType()) {
                                        case "expand":
                                            vaisseau.activateExpand(); // Agrandit le vaisseau
                                            break;

                                        case "life":
                                            vaisseau.ajouterVie(); // Ajoute une vie au joueur
                                            livesText.setText("Vies restantes: " + vaisseau.getVies());
                                            break;

                                        case "multiball":
                                            // Crée une nouvelle balle et l'ajoute à la liste des balles
                                            ArkanoidBall newBall = new ArkanoidBall(
                                                    mainBall.getBall().getCenterX(),
                                                    mainBall.getBall().getCenterY()
                                            );
                                            balls.add(newBall);
                                            gameLayout.getChildren().add(newBall.getBall());
                                            break;

                                        default:
                                            System.out.println("Type de bonus inconnu : " + bonus.getType());
                                            break;
                                    }
                                    // Retirer le bonus après collision
                                    gameLayout.getChildren().remove(bonus.getBonus());
                                    activeBonuses.remove(bonus);
                                }
                                // Si le bonus dépasse l'écran, on le retire
                                else if (bonus.getBonus().getBoundsInParent().getMaxY() >= gameScene.getHeight()) {
                                    gameLayout.getChildren().remove(bonus.getBonus());
                                    activeBonuses.remove(bonus);
                                }
                            } else {
                                // Si le bonus n'est plus actif, on le retire
                                gameLayout.getChildren().remove(bonus.getBonus());
                                activeBonuses.remove(bonus);
                            }
                        }

                        // Si toutes les briques sont détruites, on les régénère
                        if (bricks.isEmpty()) {
                            bricks.addAll(ArkanoidBrick.generateBricks(gameLayout, 4, 21, 80, 30));
                        }

                        // Mise à jour du score avec un bonus lié au temps
                        score.updateScoreWithTimeBonus();
                        scoreText.setText("Score: " + score.getScore());

                        // Gestion du temps restant pour le Super Dash
                        long currentTime = System.nanoTime();
                        long cooldownRemaining = (vaisseau.getSuperDashCooldownEndTime() - currentTime) / 1_000_000_000L;

                        // Affichage du temps restant pour le Super Dash
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
        // Démarre la boucle de jeu
                gameLoop.start();

        // Retourne la scène du jeu
                return gameScene;
    }

    // Déclaration de la méthode triggerGameOver qui est utilisée pour gérer la fin du jeu.
    private void triggerGameOver(Stage primaryStage, ArkanoidMenu menuPrincipal) {
        // Si le jeu n'est pas déjà terminé
        if (!isGameOver) {
            // On marque le jeu comme étant terminé
            isGameOver = true;

            // Chargement du meilleur score enregistré
            int bestScore = score.loadBestScore();

            // Si le score actuel est supérieur au meilleur score enregistré
            if (score.getScore() > bestScore) {
                // On sauvegarde le nouveau meilleur score
                score.saveBestScore();
            }

            // On arrête la musique du jeu (musique de fond ou autres sons)
            musicPlayer.stop();

            // Création d'une nouvelle instance de l'écran "GameOver" (fin de jeu)
            ArkanoidGameover gameOver = new ArkanoidGameover();
            // Affichage de l'écran de fin de jeu avec la scène principale du menu et le score
            gameOver.showGameOverScene(primaryStage, menuPrincipal.getMainMenuScene(), score);
        }
    }
}

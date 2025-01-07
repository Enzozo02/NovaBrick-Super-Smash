package com.example.arkanoid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public class ArkanoidMenu extends Application {

    // Déclaration de la scène principale (menu principal)
    private Scene mainMenuScene;

    @Override
    public void start(Stage primaryStage) {

        // Création de la scène du menu principal
        mainMenuScene = createMainMenuScene(primaryStage);

        // Définir le titre de la fenêtre
        primaryStage.setTitle("NovaBrick-Super-Smash");

        // Assigner la scène principale à la fenêtre
        primaryStage.setScene(mainMenuScene);

        // Passer en plein écran
        primaryStage.setFullScreen(true);

        // Empêcher la redimension de la fenêtre
        primaryStage.setResizable(false);

        // Masquer le texte du bouton "Quitter" du mode plein écran
        primaryStage.setFullScreenExitHint("");

        // Afficher la fenêtre
        primaryStage.show();

        // Bloquer l'action lorsque la touche ESCAPE est pressée (empêche de sortir du plein écran)
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().toString().equals("ESCAPE")) {
                event.consume();
            }
        });
    }

    // Méthode pour créer la scène du menu principal
    private Scene createMainMenuScene(Stage primaryStage) {

        // Création des boutons "Jouer" et "Quitter"
        Button playButton = new Button("Jouer");
        Button quitButton = new Button("Quitter");

        // Action lorsque le bouton "Jouer" est cliqué
        playButton.setOnAction(e -> {
            JeuArkanoid jeuArkanoid = new JeuArkanoid(); // Créer une instance du jeu
            Scene gameScene = jeuArkanoid.createGameScene(primaryStage, this); // Créer la scène du jeu
            primaryStage.setScene(gameScene); // Changer la scène vers la scène du jeu
            primaryStage.setFullScreen(true); // Passer en plein écran
        });

        // Action lorsque le bouton "Quitter" est cliqué
        quitButton.setOnAction(e -> primaryStage.close()); // Fermer l'application

        // Chargement de l'image de fond
        Image backgroundImage = new Image("file:resources/fond/Fondaccueil.png");

        // Création du fond avec l'image
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        // Espace vide entre les boutons
        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Création du layout vertical (VBox) pour disposer les éléments
        VBox vbox = new VBox(20, spacer, playButton, quitButton);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 350;"); // Centrer et ajuster l'espacement
        vbox.setBackground(new Background(bgImage)); // Appliquer le fond d'écran

        // Retourner la scène du menu principal
        return new Scene(vbox, 400, 300); // Définir la taille de la scène
    }

    // Méthode pour obtenir la scène principale (menu principal)
    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    // Point d'entrée de l'application
    public static void main(String[] args) {
        launch(args); // Lancer l'application
    }
}

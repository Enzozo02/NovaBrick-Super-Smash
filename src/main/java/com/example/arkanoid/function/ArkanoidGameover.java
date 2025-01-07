package com.example.arkanoid.function;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ArkanoidGameover {

    // Méthode pour afficher la scène de game over
    public void showGameOverScene(Stage primaryStage, Scene mainMenuScene, ArkanoidScore score) {

        // Création d'un VBox pour organiser les éléments de la scène de manière verticale
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: black;");

        // Titre "GAME OVER" en grand avec couleur rouge
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setStyle("-fx-font-size: 50px; -fx-fill: red;");

        // Affichage du score actuel du joueur
        Text scoreText = new Text("Votre score : " + score.getScore());
        scoreText.setStyle("-fx-font-size: 30px; -fx-fill: white;");

        // Récupération du meilleur score sauvegardé
        int bestScore = score.loadBestScore();
        boolean isNewBestScore = score.getScore() > bestScore; // Vérification si le score actuel est un nouveau meilleur score

        // Affichage du texte pour le meilleur score
        Text bestScoreText;
        if (isNewBestScore) {
            // Si c'est un nouveau meilleur score, afficher un message spécial et sauvegarder ce score
            bestScoreText = new Text("Nouveau meilleur score !");
            bestScoreText.setStyle("-fx-font-size: 30px; -fx-fill: gold;");
            score.saveBestScore(); // Sauvegarde du nouveau meilleur score
        } else {
            // Si ce n'est pas un nouveau meilleur score, afficher l'ancien meilleur score
            bestScoreText = new Text("Meilleur score : " + bestScore);
            bestScoreText.setStyle("-fx-font-size: 30px; -fx-fill: white;");
        }

        // Bouton permettant de revenir au menu principal
        Button mainMenuButton = new Button("Retour au menu principal");
        mainMenuButton.setOnAction(e -> {
            // Lorsque le bouton est cliqué, on revient au menu principal
            primaryStage.setScene(mainMenuScene);
            primaryStage.setFullScreen(true);
        });

        // Ajout de tous les éléments dans le VBox
        vbox.getChildren().addAll(gameOverText, scoreText, bestScoreText, mainMenuButton);

        // Création de la scène de game over et la définition sur le stage
        Scene gameOverScene = new Scene(vbox, 800, 600);
        primaryStage.setScene(gameOverScene);
        primaryStage.setFullScreen(true); // Passage en plein écran
    }
}

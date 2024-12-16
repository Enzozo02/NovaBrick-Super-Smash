package com.example.arkanoid.function;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ArkanoidGameover {

    public void showGameOverScene(Stage primaryStage, Scene mainMenuScene, ArkanoidScore score) {

        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: black;");

        Text gameOverText = new Text("GAME OVER");
        gameOverText.setStyle("-fx-font-size: 50px; -fx-fill: red;");

        Text scoreText = new Text("Votre score : " + score.getScore());
        scoreText.setStyle("-fx-font-size: 30px; -fx-fill: white;");

        Button mainMenuButton = new Button("Retour au menu principal");
        mainMenuButton.setOnAction(e -> {
            primaryStage.setScene(mainMenuScene);
            primaryStage.setFullScreen(true);
        });

        vbox.getChildren().addAll(gameOverText, scoreText, mainMenuButton);

        Scene gameOverScene = new Scene(vbox, 800, 600);
        primaryStage.setScene(gameOverScene);
        primaryStage.setFullScreen(true);
    }
}

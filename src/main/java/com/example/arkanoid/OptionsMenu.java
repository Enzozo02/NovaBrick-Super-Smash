package com.example.arkanoid;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OptionsMenu {

    public Scene getScene(Stage primaryStage, ArkanoidMenu mainMenu) {

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> {
            primaryStage.setScene(mainMenu.getMainMenuScene());
            primaryStage.setFullScreen(true);
        });

        VBox vbox = new VBox(20, backButton);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        return new Scene(vbox, 400, 300);
    }
}

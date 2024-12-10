package com.example.arkanoid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public class ArkanoidMenu extends Application {

    private Scene mainMenuScene;
    private Scene optionsMenuScene;

    @Override
    public void start(Stage primaryStage) {

        OptionsMenu optionsMenu = new OptionsMenu();
        optionsMenuScene = optionsMenu.getScene(primaryStage, this);

        mainMenuScene = createMainMenuScene(primaryStage);

        primaryStage.setTitle("Arkanoïd Menu");
        primaryStage.setScene(mainMenuScene);
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().toString().equals("ESCAPE")) {
                event.consume();
            }
        });
    }

    private Scene createMainMenuScene(Stage primaryStage) {

        Button playButton = new Button("Jouer");
        Button optionsButton = new Button("Options");
        Button quitButton = new Button("Quitter");

        playButton.setOnAction(e -> {

            JeuArkanoid jeuArkanoid = new JeuArkanoid();
            Scene gameScene = jeuArkanoid.createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
            primaryStage.setFullScreen(true);
        });

        optionsButton.setOnAction(e -> {
            primaryStage.setScene(optionsMenuScene);
            primaryStage.setFullScreen(true);
        });

        quitButton.setOnAction(e -> primaryStage.close());

        VBox vbox = new VBox(20, playButton, optionsButton, quitButton);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");
        return new Scene(vbox, 400, 300);
    }

    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

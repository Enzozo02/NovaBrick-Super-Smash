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

    private Scene mainMenuScene;

    @Override
    public void start(Stage primaryStage) {

        mainMenuScene = createMainMenuScene(primaryStage);

        primaryStage.setTitle("NovaBrick-Super-Smash");
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
        Button quitButton = new Button("Quitter");

        playButton.setOnAction(e -> {
            JeuArkanoid jeuArkanoid = new JeuArkanoid();
            Scene gameScene = jeuArkanoid.createGameScene(primaryStage, this);
            primaryStage.setScene(gameScene);
            primaryStage.setFullScreen(true);
        });

        quitButton.setOnAction(e -> primaryStage.close());

        Image backgroundImage = new Image("file:resources/fond/Fondaccueil.png");
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        VBox vbox = new VBox(20, spacer, playButton, quitButton);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 350;");
        vbox.setBackground(new Background(bgImage));

        return new Scene(vbox, 400, 300);
    }

    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

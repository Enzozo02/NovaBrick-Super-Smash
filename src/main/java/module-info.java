module com.example.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;


    opens com.example.arkanoid to javafx.fxml;
    exports com.example.arkanoid;
}
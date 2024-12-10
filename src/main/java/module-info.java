module com.example.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.arkanoid to javafx.fxml;
    exports com.example.arkanoid;
}
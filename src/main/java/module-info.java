module com.example.estremiassolutiintervallo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires exp4j;

    opens com.example.estremiassolutiintervallo to javafx.fxml;
    exports com.example.estremiassolutiintervallo;
}
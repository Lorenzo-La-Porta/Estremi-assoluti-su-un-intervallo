module com.example.estremiassolutiintervallo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.estremiassolutiintervallo to javafx.fxml;
    exports com.example.estremiassolutiintervallo;
}
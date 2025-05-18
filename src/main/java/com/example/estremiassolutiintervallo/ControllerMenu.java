/*
Parte di Sodo
*/
package com.example.estremiassolutiintervallo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ControllerMenu {

    /**
     * Metodo per caricare il grafico.
     * Cambia la scena attuale a quella definita in Grafico.fxml.
     */
    @FXML
    protected void onLoadGraphButtonClick(ActionEvent event) throws IOException {
        // Carica il file Grafico.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Grafico.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Ottieni lo stage corrente e sostituisci la scena
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Grafico GeoGebra");
        stage.show();
    }
}
/*
Parte di Sodo
*/
package com.example.estremiassolutiintervallo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.awt.event.InputMethodEvent;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ControllerMenu {

    @FXML
    private Button btnGenera;

    @FXML
    private VBox container;

    @FXML
    private TextField txtFunzione;

    @FXML
    private TextField txtLimD;

    @FXML
    private TextField txtLimS;

    // Variabili per memorizzare i valori
    private String funzione;
    private double limiteSinistro;
    private double limiteDestro;


    /**
     * Metodo per caricare il grafico e passare i valori al ControllerGrafico
     */
    @FXML
    protected void OnClickGenera(ActionEvent event) throws IOException {
        if (!validInputs()) return;

        // 1. Recupera l’URL “base” del FXML
        URL baseFxml = getClass().getResource("Grafico.fxml");
        if (baseFxml == null) {
            showAlert(Alert.AlertType.ERROR, "Impossibile trovare Grafico.fxml");
            return;
        }

        // 2. Costruisci la query string (URL‐encode i parametri!)
        String qs = String.format("?funzione=%s&limS=%s&limD=%s",
                URLEncoder.encode(funzione, StandardCharsets.UTF_8),
                URLEncoder.encode(Double.toString(limiteSinistro), StandardCharsets.UTF_8),
                URLEncoder.encode(Double.toString(limiteDestro), StandardCharsets.UTF_8)
        );

        // 3. Carica col FXML che ha già la query
        FXMLLoader fxmlLoader = new FXMLLoader(new URL(baseFxml.toExternalForm() + qs));
        Scene scene = new Scene(fxmlLoader.load());

        // 4. Cambio scena
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Grafico GeoGebra");
        stage.show();
    }


    /**
     * Gestisce l'input della funzione
     */
    @FXML
    void OnInputFunzione(ActionEvent event) {
        funzione = txtFunzione.getText();
    }

    /**
     * Gestisce l'input del limite destro
     */
    @FXML
    void OnInputLimD(ActionEvent event) {
        try {
            limiteDestro = Double.parseDouble(txtLimD.getText());
        } catch (NumberFormatException e) {
            // Gestione dell'errore
        }
    }

    /**
     * Gestisce l'input del limite sinistro
     */
    @FXML
    void OnInputLimS(ActionEvent event) {
        try {
            limiteSinistro = Double.parseDouble(txtLimS.getText());
        } catch (NumberFormatException e) {
            // Gestione dell'errore
        }
    }

    /**
     * Valida gli input dell'utente
     */
    private boolean validInputs() {
        funzione = txtFunzione.getText().trim();
        if (funzione.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Inserire una funzione valida!");
            return false;
        }

        // Controlla che la funzione contenga almeno una 'x'
        if (!funzione.contains("x")) {
            showAlert(Alert.AlertType.ERROR, "La funzione deve contenere almeno una variabile 'x'!");
            return false;
        }

        // Controlla che la funzione contenga solo numeri, spazi, operatori, parentesi e la variabile 'x'
        if (!funzione.matches("[0-9x+\\-*/^(). ]+")) {
            showAlert(Alert.AlertType.ERROR, "La funzione contiene caratteri non validi. Sono ammesse solo cifre, operatori (+, -, *, /, ^), parentesi e la variabile 'x'.");
            return false;
        }

        try {
            limiteSinistro = Double.parseDouble(txtLimS.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Il limite sinistro deve essere un numero!");
            return false;
        }

        try {
            limiteDestro = Double.parseDouble(txtLimD.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Il limite destro deve essere un numero!");
            return false;
        }

        if (limiteSinistro >= limiteDestro) {
            showAlert(Alert.AlertType.ERROR, "Il limite sinistro deve essere minore del limite destro!");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getter e setter per funzione
    public String getFunzione() {
        return funzione;
    }

    public void setFunzione(String funzione) {
        this.funzione = funzione;
    }

    // Getter e setter per limiteSinistro
    public double getLimiteSinistro() {
        return limiteSinistro;
    }

    public void setLimiteSinistro(double limiteSinistro) {
        this.limiteSinistro = limiteSinistro;
    }

    // Getter e setter per limiteDestro
    public double getLimiteDestro() {
        return limiteDestro;
    }

    public void setLimiteDestro(double limiteDestro) {
        this.limiteDestro = limiteDestro;
    }
}

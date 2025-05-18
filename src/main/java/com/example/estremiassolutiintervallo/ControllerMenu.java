/*
Parte di Sodo
*/
package com.example.estremiassolutiintervallo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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

    @FXML
    protected void OnClickGenera(ActionEvent event) throws IOException {
        if (!validInputs()) return;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Grafico.fxml"));
        Parent root = fxmlLoader.load();  // Cambiato da BorderPane a Parent

        ControllerGrafico controllerGrafico = fxmlLoader.getController();
        controllerGrafico.setFunzione(funzione);
        controllerGrafico.setLimiteSinistro(limiteSinistro);
        controllerGrafico.setLimiteDestro(limiteDestro);

        Scene scene = new Scene(root);
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
        // 1) Funzione come stringa
        funzione = txtFunzione.getText().trim();
        if (funzione.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Inserire una funzione valida!");
            return false;
        }
        if (!funzione.contains("x")) {
            showAlert(Alert.AlertType.ERROR, "La funzione deve contenere almeno una variabile 'x'!");
            return false;
        }
        if (!funzione.matches("[0-9x+\\-*/^(). ]+")) {
            showAlert(Alert.AlertType.ERROR,
                    "La funzione contiene caratteri non validi. Sono ammesse solo cifre, operatori (+, -, *, /, ^), parentesi e la variabile 'x'.");
            return false;
        }

        // 2) Parse dei limiti
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

        // 3) Controllo di continuità con exp4j
        if (!CalcolaMassimoMinimo.isContinuous(funzione, limiteSinistro, limiteDestro)) {
            showAlert(Alert.AlertType.ERROR, "La funzione non è continua nell'intervallo specificato!");
            return false;
        }

        // Tutto ok
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

    private boolean isFunctionContinuous(String funzione, double start, double end) {
        // Per semplicità, verifica se funzione contiene divisioni "/"
        // e prova a verificare denominatore in start e end (solo esempi semplici)
        if (!funzione.contains("/")) {
            return true;  // se non ci sono divisioni, per ora assumiamo continua
        }

        // Estrai la parte dopo '/' e valuta denominatore ai limiti (molto rudimentale)
        // Nota: serve un parser matematico per una valutazione corretta!
        try {
            String[] parts = funzione.split("/");
            for (int i = 1; i < parts.length; i++) {
                String denom = parts[i].split("[+\\-*/^()]")[0].trim();
                // Prova a sostituire 'x' con i limiti e valutare se denominatore = 0
                double valStart = evaluateExpression(denom.replace("x", Double.toString(start)));
                double valEnd = evaluateExpression(denom.replace("x", Double.toString(end)));

                if (valStart == 0 || valEnd == 0) {
                    return false;  // discontinuità per divisione per zero ai limiti
                }
            }
        } catch (Exception e) {
            // in caso di errore di parsing, assumiamo discontinua per sicurezza
            return false;
        }

        return true;
    }

    // Metodo rudimentale di valutazione di espressioni semplici numeriche (solo numeri)
    private double evaluateExpression(String expr) {
        // Qui potresti usare librerie esterne tipo exp4j o simili,
        // oppure implementare un parser base
        // Per esempio, qui solo conversione diretta se è numero:
        return Double.parseDouble(expr);
    }

    private boolean isContinuous(String funzione, double limS, double limD) {
        // Controllo molto semplice: se c'è una divisione per "x" e zero è nell'intervallo
        if (funzione.contains("/x")) {
            if (limS <= 0 && limD >= 0) {
                return false; // discontinuity at x=0 inside interval
            }
        }
        // Puoi aggiungere altri casi più complessi

        return true; // continua per default
    }
}

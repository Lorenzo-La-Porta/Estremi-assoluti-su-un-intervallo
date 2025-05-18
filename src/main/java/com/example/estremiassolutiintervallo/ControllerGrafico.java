// Parte di Lollo
package com.example.estremiassolutiintervallo;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import java.util.Locale;
import java.util.Objects;

/**
 * Controller per visualizzare il grafico in una WebView (GeoGebra).
 */
public class ControllerGrafico {

    private String funzione;          // funzione da disegnare
    private double limiteSinistro;    // inizio intervallo
    private double limiteDestro;      // fine intervallo

    @FXML private WebView webView;    // componente WebView
    private WebEngine webEngine;      // motore di rendering web

    // setter chiamati da ControllerMenu
    public void setFunzione(String f)     { this.funzione = f; }
    public void setLimiteSinistro(double s){ this.limiteSinistro = s; }
    public void setLimiteDestro(double d)  { this.limiteDestro = d; }

    /**
     * Inizializzazione automatica dopo il load dell'FXML.
     */
    @FXML
    public void initialize() {
        webEngine = webView.getEngine();

        // gestisce alert da JavaScript
        webEngine.setOnAlert((WebEvent<String> ev) -> {
            if ("GeogebraPronto".equals(ev.getData())) {
                // ridimensiona il contenitore Geogebra
                double w = webView.getWidth();
                double h = webView.getHeight();
                String jsResize = String.format(Locale.US,
                        "document.getElementById('contenitoreGeogebra').style.width='%fpx';" +
                                "document.getElementById('contenitoreGeogebra').style.height='%fpx';" +
                                "window.ggb.setSize(%f, %f);",
                        w, h, w, h);
                webEngine.executeScript(jsResize);
                inviaComandiGeoGebra();  // manda i comandi a GeoGebra
            }
        });

        // carica il file HTML di GeoGebra
        String url = Objects.requireNonNull(
                getClass().getResource("/com/example/estremiassolutiintervallo/geogebra.html")
        ).toExternalForm();
        webEngine.load(url);
    }

    /**
     * Prepara e invia i comandi a GeoGebra via JavaScript.
     */
    private void inviaComandiGeoGebra() {
        CalcolaMassimoMinimo cm = new CalcolaMassimoMinimo();
        cm.calcolaMassimoMinimo(funzione, limiteDestro, limiteSinistro);

        // definisce funzione limitata
        String cmdFunz = String.format(Locale.US,
                "f(x)=If(x>=%f && x<=%f, %s)", limiteSinistro, limiteDestro, funzione);
        eseguiComandoGeogebra(cmdFunz);

        // posiziona i punti
        eseguiComandoGeogebra(
                "massimo=Point({(" + cm.getXMassimo() + "," + cm.getYMassimo() + ")})"
        );
        eseguiComandoGeogebra(
                "minimo=Point({(" + cm.getXMinimo() + "," + cm.getYMinimo() + ")})"
        );
    }

    /**
     * Esegue un comando GeoGebra in JS, escapando le virgolette.
     */
    private void eseguiComandoGeogebra(String cmd) {
        String esc = cmd.replace("\"", "\\\"");
        webEngine.executeScript("window.ggb.evalCommand(\"" + esc + "\");");
    }
}
/*
Parte di Lollo
*/
package com.example.estremiassolutiintervallo;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import java.util.Locale;
import java.util.Objects;

public class ControllerGrafico {

    @FXML private WebView webView;
    private WebEngine webEngine;

    /**
     * Inizializza il controller della scena del grafico.
     * Imposta la dimensione della WebView e gestisce i messaggi di tipo alert che gli arrivano da javascript.
     * Quando riceve il messaggio "GeogebraPronto", ridimensiona il grafico GeoGebra in base alla dimensione della WebView
     * e invia i comandi necessari.
     * Infine, carica il file HTML dal progetto.
     */
    @FXML
    public void initialize() {
        webEngine = webView.getEngine();

        // Ogni volta che javascript invia un messaggio di tipo alert
        webEngine.setOnAlert((WebEvent<String> ev) -> {
            // Se il messaggio è "GeogebraPronto", allora...
            if ("GeogebraPronto".equals(ev.getData())) {
                double lunghezza = webView.getWidth();
                double larghezza = webView.getHeight();
                // Imposto la dimensione della scena a quella della webview
                String jsResize = String.format(Locale.US, "document.getElementById('contenitoreGeogebra').style.width='%fpx';" + "document.getElementById('contenitoreGeogebra').style.height='%fpx';" + "window.ggb.setSize(%f, %f);", lunghezza, larghezza, lunghezza, larghezza);
                webEngine.executeScript(jsResize);
                // Invio della funzione e dei punti massimo e minimo
                inviaComandiGeoGebra();
            }
        });

        // Carico l'HTML "geogebra.html"
        String url = Objects.requireNonNull(getClass().getResource("/com/example/estremiassolutiintervallo/geogebra.html")).toExternalForm();
        webEngine.load(url);
    }

    /**
     * Invia a GeoGebra la definizione di una funzione limitata a destra e sinistra
     * e posiziona i punti massimo e minimo per verificare il teorema di Weierstrass.
     */
    private void inviaComandiGeoGebra() {
        double limte_sinistro = -2;
        double limte_destro = 2;
        String funzioneCompleta = "x^2";
        String funzioneConEstremi = String.format(Locale.US,"f(x)=If(x>=%f && x<=%f, %s)", limte_sinistro, limte_destro, funzioneCompleta);
        eseguiComandoGeogebra(funzioneConEstremi);
        eseguiComandoGeogebra("massimo=Point({(2,4)})");
        eseguiComandoGeogebra("minimo=Point({(0,0)})");
    }

    /**
     * Esegue un comando di GeoGebra passandolo a JavaScript.
     *
     * @param cmd il comando GeoGebra da eseguire. I caratteri speciali verranno rimossi per garantirne l'esecuzione corretta.
     */
    private void eseguiComandoGeogebra(String cmd) {
        String esc = cmd.replace("\"", "\\\"");
        webEngine.executeScript("window.ggb.evalCommand(\"" + esc + "\");");
    }
}
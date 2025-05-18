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

    private ControllerMenu menu;
    private CalcolaMassimoMinimo calc;

    // Setter per il controller del menu
    public void setMenuController(ControllerMenu menu) {
        this.menu = menu;
    }
    // Setter per il calcolatore degli estremi
    public void setCalcolatore(CalcolaMassimoMinimo calc) {
        this.calc = calc;
    }

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
                // Si passa all'invio dei comandi a geogebra
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
 /*   private void inviaComandiGeoGebra() {


        CalcolaMassimoMinimo calcolaMassimoMinimo = new CalcolaMassimoMinimo();
        String funzioneCompleta = controllerMenu.getFunzione();
        double limite_destro = controllerMenu.getLimiteDestro();
        double limite_sinistro = controllerMenu.getLimiteSinistro();

        // Calcola i valori massimi e minimi
        calcolaMassimoMinimo.calcolaMassimoMinimo(funzioneCompleta, limite_destro, limite_sinistro);
        String funzioneConEstremi = String.format(Locale.US,"f(x)=If(x>=%f && x<=%f, %s)", limite_sinistro, limite_destro, funzioneCompleta);
        eseguiComandoGeogebra(funzioneConEstremi);
        eseguiComandoGeogebra("massimo=Point({(" + calcolaMassimoMinimo.getXMassimo() + "," + calcolaMassimoMinimo.getYMassimo() + ")})");
        eseguiComandoGeogebra("minimo=Point({(" + calcolaMassimoMinimo.getXMinimo() + "," + calcolaMassimoMinimo.getYMinimo() + ")})");
    }
*/

    private void inviaComandiGeoGebra() {
        CalcolaMassimoMinimo calcolaMassimoMinimo = new CalcolaMassimoMinimo();

        // Usa i dati ricevuti tramite i setter
        String funzioneCompleta = funzione;
        double limite_destro = limiteDestro;
        double limite_sinistro = limiteSinistro;

        // Calcola i valori massimi e minimi
        calcolaMassimoMinimo.calcolaMassimoMinimo(funzioneCompleta, limite_destro, limite_sinistro);

        String funzioneConEstremi = String.format(Locale.US,
                "f(x)=If(x>=%f && x<=%f, %s)", limite_sinistro, limite_destro, funzioneCompleta);
        eseguiComandoGeogebra(funzioneConEstremi);

        eseguiComandoGeogebra("massimo=Point({(" + calcolaMassimoMinimo.getXMassimo() + "," + calcolaMassimoMinimo.getYMassimo() + ")})");
        eseguiComandoGeogebra("minimo=Point({(" + calcolaMassimoMinimo.getXMinimo() + "," + calcolaMassimoMinimo.getYMinimo() + ")})");
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

    private String funzione;
    private double limiteSinistro;
    private double limiteDestro;

    public void setFunzione(String funzione) {
        this.funzione = funzione;
    }

    public void setLimiteSinistro(double limiteSinistro) {
        this.limiteSinistro = limiteSinistro;
    }

    public void setLimiteDestro(double limiteDestro) {
        this.limiteDestro = limiteDestro;
    }

}
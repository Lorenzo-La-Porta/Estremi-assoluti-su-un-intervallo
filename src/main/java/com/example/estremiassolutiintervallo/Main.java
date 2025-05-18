/*
 * Parte di Sofia
 * Classe Main che rappresenta il punto di ingresso dell'applicazione JavaFX
 */
package com.example.estremiassolutiintervallo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Classe principale che estende Application e avvia l'applicazione JavaFX.
 * Configura la finestra principale adattandola alle dimensioni dello schermo.
 */
public class Main extends Application {

    /**
     * Metodo start, punto di ingresso principale per l'applicazione JavaFX.
     *
     * @param primaryStage Lo stage principale dell'applicazione
     * @throws Exception Se si verificano errori durante il caricamento del file FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file FXML della schermata principale (Menu)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/estremiassolutiintervallo/Menu.fxml"));
        Parent root = fxmlLoader.load();

        // Ottiene le dimensioni dello schermo primario
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Crea una nuova scena con le dimensioni dello schermo
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        // Configura lo stage per occupare tutto lo schermo
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        // Imposta il titolo della finestra e mostra la scena
        primaryStage.setTitle("Menu Principale");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Metodo main, punto di ingresso tradizionale per applicazioni Java.
     *
     * @param args Argomenti da riga di comando
     */
    public static void main(String[] args) {
        // Avvia l'applicazione JavaFX
        launch(args);
    }
}
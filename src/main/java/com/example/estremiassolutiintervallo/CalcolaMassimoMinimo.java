/*
Parte di Sofia
*/
package com.example.estremiassolutiintervallo;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalcolaMassimoMinimo {

    private double yMassimo;  // Valore y massimo calcolato
    private double yMinimo;   // Valore y minimo calcolato
    private double xMassimo; // Coordinata x del massimo
    private double xMinimo;  // Coordinata x del minimo

    /**
     * Metodo per calcolare il valore della funzione data una stringa ed un valore di x.
     *
     * @param funzione La funzione come stringa, es. "x^2 - 4*x + 3".
     * @param x Il valore di x.
     * @return Il risultato della funzione per il valore di x.
     */
    private static double funzione(String funzione, double x) {
        try {
            // Crea l'espressione e inserisce il valore di x
            Expression expression = new ExpressionBuilder(funzione)
                    .variables("x") // Aggiunge la variabile x
                    .build()
                    .setVariable("x", x); // Imposta il valore di x

            // Calcola e ritorna il risultato
            return expression.evaluate();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel calcolo della funzione: " + e.getMessage());
        }
    }

    /**
     * Calcola massimo e minimo della funzione nell'intervallo [a, b].
     *
     * I dati (funzione, limiti) sono ottenuti tramite i getter di ControllerMenu.
     * I risultati (massimo e minimo, incluse le coordinate) sono memorizzati nelle variabili della classe.
     */
    public void calcolaMassimoMinimo(String funzione, double limiteDestro, double limiteSinistro) {
        int n = 1000; // Numero di punti per il calcolo
        double passo = (limiteDestro - limiteSinistro) / n;

        double x = limiteSinistro;
        double y = funzione(funzione, x);

        // Inizializza il massimo e il minimo
        yMassimo = y;
        yMinimo = y;
        xMassimo = x; // Memorizza la coordinata x del massimo
        xMinimo = x; // Memorizza la coordinata x del minimo

        // Esegui il calcolo iterando nell'intervallo
        for (int i = 1; i <= n; i++) {
            x = limiteSinistro + i * passo;
            y = funzione(funzione, x);
            if (y < yMinimo) {
                yMinimo = y;
                xMinimo = x; // Aggiorna la coordinata x del minimo
            }
            if (y > yMassimo) {
                yMassimo = y;
                xMassimo = x; // Aggiorna la coordinata x del massimo
            }
        }
    }

    /**
     * Restituisce il massimo y calcolato.
     *
     * @return valore y massimo
     */
    public double getYMassimo() {
        return yMassimo;
    }

    /**
     * Restituisce il minimo y calcolato.
     *
     * @return valore y minimo
     */
    public double getYMinimo() {
        return yMinimo;
    }

    /**
     * Restituisce la coordinata x del massimo.
     *
     * @return coordinata x del massimo
     */
    public double getXMassimo() {
        return xMassimo;
    }

    /**
     * Restituisce la coordinata x del minimo.
     *
     * @return coordinata x del minimo
     */
    public double getXMinimo() {
        return xMinimo;
    }
}
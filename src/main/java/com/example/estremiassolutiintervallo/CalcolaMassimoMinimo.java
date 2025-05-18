// Parte di Sofia
package com.example.estremiassolutiintervallo;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Classe per calcolare massimo e minimo di una funzione in un intervallo.
 */
public class CalcolaMassimoMinimo {

    // Risultati calcolati
    private double yMassimo;      // Valore y massimo trovato
    private double yMinimo;       // Valore y minimo trovato
    private double xMassimo;      // Coordinata x del massimo
    private double xMinimo;       // Coordinata x del minimo

    private static final int SAMPLES = 1000; // Numero di punti per il campionamento

    /**
     * Valuta la funzione in un punto x usando exp4j.
     * @param funzione espressione come stringa (es. "x^2+1").
     * @param x valore di x.
     * @return risultato numerico.
     */
    private static double funzione(String funzione, double x) {
        try {
            Expression expr = new ExpressionBuilder(funzione)
                    .variables("x")            // definisce "x"
                    .build()
                    .setVariable("x", x);     // imposta valore x
            return expr.evaluate();           // calcola e ritorna
        } catch (Exception e) {
            // rilancio in caso di errori di parsing o valutazione
            throw new RuntimeException("Errore nel calcolo: " + e.getMessage());
        }
    }

    /**
     * Campiona la funzione in SAMPLES punti e trova estremo max/min.
     * @param funzione stringa della funzione
     * @param limiteSinistro inizio intervallo
     * @param limiteDestro fine intervallo
     */
    public void calcolaMassimoMinimo(String funzione, double limiteDestro, double limiteSinistro) {
        double passo = (limiteDestro - limiteSinistro) / SAMPLES; // distanza tra campioni

        // inizializza con il primo punto
        double x = limiteSinistro;
        double y = funzione(funzione, x);
        yMassimo = y;
        yMinimo = y;
        xMassimo = x;
        xMinimo = x;

        // scorre i campioni restanti
        for (int i = 1; i <= SAMPLES; i++) {
            x = limiteSinistro + i * passo;
            y = funzione(funzione, x);
            if (y < yMinimo) {
                yMinimo = y;
                xMinimo = x;
            }
            if (y > yMassimo) {
                yMassimo = y;
                xMassimo = x;
            }
        }
    }

    // getter per i risultati
    public double getYMassimo() { return yMassimo; }
    public double getYMinimo()  { return yMinimo; }
    public double getXMassimo() { return xMassimo; }
    public double getXMinimo()  { return xMinimo; }

    /**
     * Verifica la continuità valutando la funzione in SAMPLES punti.
     * @param funzione stringa della funzione
     * @param limS inizio intervallo
     * @param limD fine intervallo
     * @return true se non si verificano errori o valori infiniti/NaN
     */
    public static boolean isContinuous(String funzione, double limS, double limD) {
        double passo = (limD - limS) / SAMPLES;
        for (int i = 0; i <= SAMPLES; i++) {
            double x = limS + i * passo;
            try {
                double y = funzione(funzione, x);
                if (Double.isNaN(y) || Double.isInfinite(y)) {
                    return false;  // valore non valido indica discontinuità
                }
            } catch (RuntimeException e) {
                return false;      // eccezione indica discontinuità
            }
        }
        return true;               // nessun problema, funzione continua
    }
}
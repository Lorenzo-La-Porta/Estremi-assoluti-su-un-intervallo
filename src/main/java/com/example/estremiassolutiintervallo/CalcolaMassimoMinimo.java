/*
Parte di Sofia
*/
package com.example.estremiassolutiintervallo;

public class CalcolaMassimoMinimo {

    public static double funzione(double x) {
        return x * x - 4 * x + 3;
    }

    public static void main(String[] args) {

        double a = 2;
        double b = -2;

        int n = 1000;
        double passo = (b - a) / n;

        double x = a;
        double minimo = funzione(x);
        double massimo = funzione(x);

        for (int i = 1; i <= n; i++) {
            x = a + i * passo;
            double y = funzione(x);
            if (y < minimo) minimo = y;
            if (y > massimo) massimo = y;
        }
    }
}
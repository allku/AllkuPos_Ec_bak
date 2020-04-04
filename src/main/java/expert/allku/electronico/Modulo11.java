/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expert.allku.electronico;

/**
 *
 * @author jorgequiguango
 */
public class Modulo11 {

    private String invertirCadena(String cadena) {
        String cadenaInvertida = "";
        for (int x = cadena.length() - 1; x >= 0; x--) {
            cadenaInvertida = cadenaInvertida + cadena.charAt(x);
        }
        return cadenaInvertida;
    }

    private int obtenerSumaPorDigitos(String cadena) {

        int pivote = 2;
        int longitudCadena = cadena.length();
        int cantidadTotal = 0;
        int b = 1;
        for (int i = 0; i < longitudCadena; i++) {
            if (pivote == 8) {
                pivote = 2;
            }
            int temporal = Integer.parseInt("" + cadena.substring(i, b));
            b++;
            temporal = temporal * pivote;
            pivote++;
            cantidadTotal = cantidadTotal + temporal;
        }
        cantidadTotal = 11 - cantidadTotal % 11;
        if (cantidadTotal == 10) {
            cantidadTotal = 1;
        }
        if (cantidadTotal == 11) {
            cantidadTotal = 0;
        }
        return cantidadTotal;
    }

    public int modulo11(String cadena) {
        return obtenerSumaPorDigitos(invertirCadena(cadena));
    }
}

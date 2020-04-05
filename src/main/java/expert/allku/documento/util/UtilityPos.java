/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expert.allku.documento.util;

/**
 *
 * @author jorgequiguango
 */
public class UtilityPos {

    public String extraerApellido(String razonSocial) {

        String[] words = razonSocial.split(" ");
        String apellido = "";
        int spaceCount = countWhiteSpace(razonSocial);
        int i = 0;

        if (spaceCount == 0) {
            return razonSocial;
        }

        for (String token : words) {
            apellido = apellido + token;
            if (spaceCount - 1 == i || i == 1) {
                return apellido.trim();
            }
            apellido = apellido + " ";
            i++;
        }

        return "";
    }

    public String extraerNombre(String razonSocial) {

        String[] words = razonSocial.split(" ");
        String nombre = "";
        int spaceCount = countWhiteSpace(razonSocial);
        int i = 0;

        for (String token : words) {
            if (spaceCount == i || i > 1) {
                nombre = nombre + token;
                nombre = nombre + " ";
            }
            i++;
        }

        return nombre;
    }

    int countWhiteSpace(String text) {
        int spaceCount = 0;
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }

}

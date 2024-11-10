package com.feroxdev.inmobigestor.utilities;

public class GenericUtils {
    private static boolean isDNIValid(String dni) {
        if (dni == null || !dni.matches("\\d{8}[A-Z]")) {
            return false;
        }
        int numeroDNI = Integer.parseInt(dni.substring(0, 8));
        char letraDNI = dni.charAt(8);
        String letras_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
        char letraCorrecta = letras_DNI.charAt(numeroDNI % 23);

        return letraDNI == letraCorrecta;
    }
}

package com.feroxdev.inmobigestor.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Locale;

public class CsvUtils {

    /**
     * Normaliza una cadena de texto
     * @param input Cadena de texto
     * @return Cadena de texto normalizada
     */
    private static String normalizeString(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase(Locale.ROOT);
    }

    /**
     * Busca una cadena de texto en un archivo CSV
     * @param searchString Cadena de texto a buscar
     * @return Array con los datos encontrados
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String[] searchInCsv(String searchString) throws IOException, URISyntaxException {
        URL resource = CsvUtils.class.getClassLoader().getResource("docs/municipios.csv");
        if (resource == null) {
            throw new IOException("Archivo no encontrado");
        }
        File csvFile = Paths.get(resource.toURI()).toFile();
        String normalizedSearchString = normalizeString(searchString);
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] columns = line.split(";");

                if (columns.length < 2) {
                    continue;
                }

                String normalizedColumn2 = normalizeString(columns[1]);

                if (normalizedColumn2.contains(normalizedSearchString)) {
                    return new String[]{columns[0], columns[1]};
                }
            }
        }

        return null;
    }
}

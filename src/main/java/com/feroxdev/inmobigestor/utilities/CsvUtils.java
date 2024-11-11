package com.feroxdev.inmobigestor.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Locale;

public class CsvUtils {
    private static String normalizeString(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase(Locale.ROOT);
    }

    public static String[] searchInCsv(String csvFile, String searchString) throws IOException {
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

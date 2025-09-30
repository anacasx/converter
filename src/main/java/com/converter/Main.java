package com.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.converter.io.CsvFileWriter;
import com.converter.io.JsonToCsvMapper;
import com.converter.io.JsonFileReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Application entrypoint for the Converter tool.
 *
 * Supports:
 *  --input <input.json>
 *  --output <output.csv>
 *  --delimiter <,|;|\t> (single char)
 *  --config <path/to/config.properties> (optional)
 *
 * If both CLI args and config file set a parameter, CLI overrides config file.
 *
 * Example:
 *   java -jar converter.jar --input sample_data/input.json --output sample_data/out.csv --delimiter ','
 */
public class Main {

    public static void main(String[] args) {
        Map<String, String> params = parseArgs(args);

        Properties config = new Properties();
        // If config file provided, load it
        if (params.containsKey("config")) {
            try (FileInputStream fis = new FileInputStream(params.get("config"))) {
                config.load(fis);
            } catch (IOException e) {
                System.err.println("Warning: could not read config file: " + e.getMessage());
            }
        }

        // Resolve parameters (CLI overrides config)
        String input = firstNonNull(params.get("input"), config.getProperty("input"), "sample_data/input.json");
        String output = firstNonNull(params.get("output"), config.getProperty("output"), "sample_data/output.csv");
        String delimStr = firstNonNull(params.get("delimiter"), config.getProperty("delimiter"), ",");
        char delimiter = delimStr.isEmpty() ? ',' : delimStr.charAt(0);

        JsonFileReader reader = new JsonFileReader();
        JsonToCsvMapper mapper = new JsonToCsvMapper();
        CsvFileWriter writer = new CsvFileWriter();

        try {
            // Read JSON using Sprint 2 function
            List<Map<String, Object>> rows = reader.readJson(input, new TypeReference<List<Map<String, Object>>>() {});
            // Transform to flat rows suitable for CSV
            List<Map<String, Object>> flatRows = mapper.mapToFlatRows(rows);
            // Write CSV; headers null -> writer will infer headers automatically
            writer.writeCsv(output, null, flatRows, delimiter);
            System.out.println("CSV created successfully at: " + output);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String firstNonNull(String... vals) {
        for (String v : vals) {
            if (v != null && !v.isEmpty()) return v;
        }
        return null;
    }

    /**
     * Minimal CLI args parser that accepts key-value pairs:
     * --input value --output value --delimiter value --config value
     */
    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> out = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (!a.startsWith("--")) continue;
            String key = a.substring(2);
            String val = "";
            if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                val = args[i + 1];
                i++;
            }
            out.put(key, val);
        }
        return out;
    }
}

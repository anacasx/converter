package com.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.converter.io.CsvFileWriter;
import com.converter.io.JsonFileReader;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String inputJson = "sample_data/input.json";
        String outputCsv = "sample_data/output.csv";
        JsonFileReader reader = new JsonFileReader();
        CsvFileWriter writer = new CsvFileWriter();

        try {
            // Read JSON as list of maps
            List<Map<String, Object>> rows = reader.readJson(inputJson, new TypeReference<List<Map<String,Object>>>() {});
            // Define headers according to the JSON structure
            String[] headers = new String[] { "id", "name", "age" };
            // Write CSV
            writer.writeCsv(outputCsv, headers, rows, ',');
            System.out.println("CSV creado en: " + outputCsv);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

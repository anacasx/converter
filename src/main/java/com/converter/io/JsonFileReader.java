package com.converter.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JsonFileReader provides utilities to read JSON files and convert them
 * into Java structures (List/Map) or specific POJOs.
 */
public class JsonFileReader {
    private final ObjectMapper objectMapper;

    public JsonFileReader() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Reads a JSON file and parses it as a list of maps
     * (each element corresponds to a JSON object).
     *
     * @param filePath path to the JSON file
     * @return List of Map with the JSON data
     * @throws IOException if an error occurs while reading or parsing the file
     */
    public List<Map<String, Object>> readJsonAsListOfMap(String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        // TypeReference para List<Map<String,Object>>
        return objectMapper.readValue(f, new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Reads a JSON file and parses it into a list of POJOs of the provided type.
     *
     * @param filePath path to the JSON file
     * @param valueTypeRef TypeReference for the target type
     *                     (e.g., new TypeReference<List<Record>>() {})
     * @param <T> generic return type
     * @return object parsed from the JSON file
     * @throws IOException if an error occurs while reading or parsing the file
     */
    public <T> T readJson(String filePath, TypeReference<T> valueTypeRef) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        return objectMapper.readValue(f, valueTypeRef);
    }
}

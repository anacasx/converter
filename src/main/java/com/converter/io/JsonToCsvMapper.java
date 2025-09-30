package com.converter.io;

import java.util.*;

/**
 * JsonToCsvMapper transforms JSON-parsed structures (List of Map) into flattened
 * rows suitable for CSV output.
 * *
 * Behavior:
 * - Recursively flattens nested Maps. Nested keys are joined with '.' (dot).
 * - Lists are converted to a single string by joining elements with '|' (pipe).
 * - Primitive values are converted to String using toString().
 * *
 * Example:
 * Input: {"id":"1","person":{"name":"Ana","address":{"city":"MX"}}}
 * Output row keys: "id", "person.name", "person.address.city"
 *
 */
public class JsonToCsvMapper {

    /**
     * Map a list of maps (typical result of parsing a JSON array of objects)
     * into a list of flattened maps suitable for CSV generation.
     *
     * @param rows List of Map<String,Object> parsed from JSON
     * @return List of flattened Map<String,Object> where nested structures are flattened
     * @throws IllegalArgumentException if rows is null
     */
    public List<Map<String, Object>> mapToFlatRows(List<Map<String, Object>> rows) {
        if (rows == null) {
            throw new IllegalArgumentException("rows cannot be null");
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Map<String, Object> flat = new LinkedHashMap<>();
            flattenMap("", row, flat);
            result.add(flat);
        }
        return result;
    }

    /**
     * Recursively flattens a map into the provided accumulator.
     *
     * @param prefix prefix for keys (use "" for top-level)
     * @param current current object (Map / List / primitive)
     * @param acc accumulator map where flattened pairs are stored
     */
    @SuppressWarnings("unchecked")
    private void flattenMap(String prefix, Object current, Map<String, Object> acc) {
        if (current == null) {
            // leave null as empty string in CSV later
            return;
        }

        if (current instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) current;
            for (Map.Entry<String, Object> e : map.entrySet()) {
                String key = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
                flattenMap(key, e.getValue(), acc);
            }
        } else if (current instanceof List) {
            List<Object> list = (List<Object>) current;
            // flatten list by converting its elements to strings (recursively if needed)
            List<String> items = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Map || item instanceof List) {
                    // for nested complex types, produce JSON-like string using simple join
                    Map<String, Object> tmp = new LinkedHashMap<>();
                    flattenMap("", item, tmp);
                    items.add(mapToInlineString(tmp));
                } else if (item == null) {
                    items.add("");
                } else {
                    items.add(item.toString());
                }
            }
            acc.put(prefix, String.join("|", items));
        } else {
            // primitive value
            acc.put(prefix, current.toString());
        }
    }

    /**
     * Helper to convert a flat map into a simple inline string "k1:v1,k2:v2".
     * Used when a list element is itself a complex object.
     */
    private String mapToInlineString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append(e.getKey()).append(":").append(e.getValue() == null ? "" : e.getValue().toString());
            first = false;
        }
        return sb.toString();
    }
}

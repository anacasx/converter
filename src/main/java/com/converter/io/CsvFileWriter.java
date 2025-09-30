package com.converter.io;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * CsvFileWriter provides utilities to write CSV from a list of maps (rows).
 * If headers == null, headers will be inferred from the union of keys across rows,
 * preserving insertion order (first-seen order).
 */
public class CsvFileWriter {

    /**
     * Write rows to CSV.
     *
     * @param filePath path to output CSV
     * @param headers array of column names. If null or empty, headers inferred from rows.
     * @param rows list of rows as Map<columnName, value>
     * @param delimiter csv delimiter character (e.g. ',', ';', '\t')
     * @throws IOException if writing fails
     */
    public void writeCsv(String filePath, String[] headers, List<Map<String, Object>> rows, char delimiter) throws IOException {
        if (rows == null) {
            throw new IllegalArgumentException("Rows cannot be null");
        }
        if (rows.isEmpty()) {
            // create empty CSV with only headers if provided, else throw
            if (headers == null || headers.length == 0) {
                throw new IllegalArgumentException("Rows empty and headers not provided.");
            }
        }

        // If headers are not provided, infer from union of keys preserving first-seen order
        if (headers == null || headers.length == 0) {
            LinkedHashSet<String> headerSet = new LinkedHashSet<>();
            for (Map<String, Object> row : rows) {
                if (row != null) {
                    headerSet.addAll(row.keySet());
                }
            }
            headers = headerSet.toArray(new String[0]);
        }

        try (Writer writer = new FileWriter(filePath);
             CSVWriter csvWriter = new CSVWriter(writer,
                     delimiter,
                     CSVWriter.DEFAULT_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            // Write header
            csvWriter.writeNext(headers);

            // Write rows
            for (Map<String, Object> row : rows) {
                String[] line = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    Object value = (row == null) ? null : row.get(headers[i]);
                    line[i] = (value == null) ? "" : value.toString();
                }
                csvWriter.writeNext(line);
            }
            csvWriter.flush();
        } catch (IOException e) {
            throw new IOException("Error writing CSV to " + filePath, e);
        }
    }
}

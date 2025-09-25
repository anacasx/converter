package com.converter.io;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * CsvFileWriter provides utilities to write data into CSV files.
 */
public class CsvFileWriter {

    /**
     * Writes a list of maps (each map = one row) into a CSV file.
     * Columns follow the order defined in `headers`.
     *
     * @param filePath path to the output CSV file
     * @param headers array of column names (header row)
     * @param rows list of rows represented as Map<columnHeader, value>
     * @param delimiter character used as separator (e.g., ',', ';', '\t')
     * @throws IOException if an error occurs while writing the file
     */
    public void writeCsv(String filePath, String[] headers, List<Map<String, Object>> rows, char delimiter) throws IOException {
        try (Writer writer = new FileWriter(filePath);
             CSVWriter csvWriter = new CSVWriter(writer,
                     delimiter,
                     CSVWriter.DEFAULT_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {
            // Write headers
            csvWriter.writeNext(headers);

            // Write each row following the header order
            for (Map<String, Object> row : rows) {
                String[] line = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    Object value = row.get(headers[i]);
                    line[i] = (value == null) ? "" : value.toString();
                }
                csvWriter.writeNext(line);
            }
            csvWriter.flush();
        } catch (IOException e) {
            // Rethrow so the caller can handle or log the error
            throw new IOException("Error writing CSV to " + filePath, e);
        }
    }
}

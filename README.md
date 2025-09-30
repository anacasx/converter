# JSON to CSV Converter

## Description
This repository contains a Java 17 project called **Converter**, developed to transform JSON files into CSV format.  
It includes functionality for reading JSON, transforming the data into a flat structure, and writing the results into CSV files.

---

## Features
- **JSON File Reading**: Reads data from JSON files using Jackson.
- **Data Transformation**: Nested objects are flattened (e.g., `person.name`), and lists are joined with `|`.
- **CSV File Writing**: Automatically generates headers from JSON keys and writes CSV using OpenCSV.
- **Parameter Configuration**: Input/output files and delimiter can be set via command-line arguments (`--input`, `--output`, `--delimiter`) or with a configuration file.
- **Error Handling**: Exceptions are caught and reported for missing files, invalid formats, or write errors.

---

## Transformation Algorithm
1. **Read JSON file** into a list of maps.
2. **Flatten nested data** into simple key-value pairs.
3. **Collect headers** from the JSON keys.
4. **Write CSV file** with headers and rows.

---

## Quick Start
```bash
# Default run (uses sample_data/input.json)
mvn compile exec:java -Dexec.mainClass="com.converter.Main"

# With parameters
mvn compile exec:java -Dexec.mainClass="com.converter.Main" -Dexec.args="--input sample_data/input.json --output sample_data/output.csv --delimiter ,"

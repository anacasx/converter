package io;

import com.converter.io.CsvFileWriter;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CsvFileWriterTest {
    @Test
    public void testWriteCsv() throws Exception {
        Path temp = Files.createTempFile("out", ".csv");
        CsvFileWriter writer = new CsvFileWriter();
        String[] headers = {"id","name","age"};
        Map<String,Object> r1 = new HashMap<>();
        r1.put("id","1"); r1.put("name","Ana"); r1.put("age",30);
        Map<String,Object> r2 = new HashMap<>();
        r2.put("id","2"); r2.put("name","Luis"); r2.put("age",25);

        writer.writeCsv(temp.toString(), headers, Arrays.asList(r1,r2), ',');

        List<String> lines = Files.readAllLines(temp);
        assertFalse(lines.isEmpty());
        assertEquals("\"id\",\"name\",\"age\"", lines.get(0).replace("\r",""));
        assertTrue(lines.size() >= 3);
    }
}

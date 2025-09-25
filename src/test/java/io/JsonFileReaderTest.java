package io;

import com.converter.io.JsonFileReader;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFileReaderTest {
    @Test
    public void testReadJsonAsListOfMap() throws Exception {
        // Create a temporary JSON file for testing
        Path temp = Files.createTempFile("test", ".json");
        String json = "[{\"id\":\"1\",\"name\":\"Ana\",\"age\":30},{\"id\":\"2\",\"name\":\"Luis\",\"age\":25}]";
        Files.writeString(temp, json);

        JsonFileReader reader = new JsonFileReader();
        List<Map<String,Object>> list = reader.readJson(temp.toString(), new TypeReference<List<Map<String,Object>>>() {});
        assertEquals(2, list.size());
        assertEquals("Ana", list.get(0).get("name"));
    }

    @Test
    public void testFileNotFound() {
        JsonFileReader reader = new JsonFileReader();
        Exception ex = assertThrows(Exception.class, () -> {
            reader.readJson("nonexistent.json", new TypeReference<List<Map<String,Object>>>() {});
        });
        assertTrue(ex.getMessage().toLowerCase().contains("not found"));
    }
}

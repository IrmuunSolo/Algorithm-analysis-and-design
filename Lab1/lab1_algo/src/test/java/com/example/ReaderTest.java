package com.example;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @Test
    void testReadFile() throws IOException {
        Path testFile = Files.createTempFile("test", ".txt");
        Files.write(testFile, "Hello World\nJava Programming".getBytes());

        String result = Reader.readFile(testFile.toString());

        assertNotNull(result);
        assertTrue(result.contains("Hello World"));
        assertTrue(result.contains("Java Programming"));

        Files.deleteIfExists(testFile);
    }

    @Test
    void testEmptyFile() throws IOException {
        Path testFile = Files.createTempFile("empty", ".txt");

        String result = Reader.readFile(testFile.toString());
        assertNull(result);

        Files.deleteIfExists(testFile);
    }

    @Test
    void testFileWithSpaces() throws IOException {
        Path testFile = Files.createTempFile("spaces", ".txt");
        Files.write(testFile, "   \n\t\n    ".getBytes());

        String result = Reader.readFile(testFile.toString());

        assertNull(result);

        Files.deleteIfExists(testFile);
    }

    @Test
    void testNonExistentFile() {
        String nonExistentFile = "nonexistent_file_12345.txt";

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> Reader.readFile(nonExistentFile));

        assertTrue(ex.getMessage().contains("Error to read file:"));
    }
}

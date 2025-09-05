package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    public static String readFile(String fileName) {
        StringBuilder content = new StringBuilder();
        boolean isEmpty = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    isEmpty = false;
                }
                content.append(line).append("\n");
            }

            if (isEmpty) {
                return null; 
            }

            return content.toString();

        } catch (IOException e) {
            throw new RuntimeException("Error to read file: " + e.getMessage(), e);
        }
    }
}

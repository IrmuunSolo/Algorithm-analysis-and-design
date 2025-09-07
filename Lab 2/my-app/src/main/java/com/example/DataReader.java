package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataReader {
    // file: each line can contain space separated ints
    public static List<int[]> readAllIntLines(String path) throws IOException {
        List<int[]> res = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                String[] parts = line.split("[,\\s]+");
                int[] arr = new int[parts.length];
                for (int i = 0; i < parts.length; i++)
                    arr[i] = Integer.parseInt(parts[i]);
                res.add(arr);
            }
        }
        return res;
    }
}

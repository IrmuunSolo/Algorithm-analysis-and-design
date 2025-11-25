package com.example.justify;

import java.util.*;

public class GreedyJustifier {
    public static List<String> justify(List<String> words, int maxWidth) {
        List<String> lines = new ArrayList<>();
        int n = words.size();
        int i = 0;
        while (i < n) {
            int lineLen = words.get(i).length();
            int j = i + 1;
            while (j < n && lineLen + 1 + words.get(j).length() <= maxWidth) {
                lineLen += 1 + words.get(j).length();
                j++;
            }
            List<String> lineWords = words.subList(i, j);
            boolean isLast = j >= n;
            lines.add(JustifyHelper.formatLine(lineWords, maxWidth, isLast));
            i = j;
        }
        return lines;
    }
}

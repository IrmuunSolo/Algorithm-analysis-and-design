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
            lines.add(formatLine(lineWords, maxWidth, isLast));
            i = j;
        }
        return lines;
    }

    static String formatLine(List<String> words, int maxWidth, boolean isLast) {
        if (words.isEmpty()) return " ".repeat(maxWidth);
        if (words.size() == 1 || isLast) {
            String s = String.join(" ", words);
            return s + " ".repeat(maxWidth - s.length());
        }
        int totalChars = words.stream().mapToInt(String::length).sum();
        int spaces = maxWidth - totalChars;
        int slots = words.size() - 1;
        int base = spaces / slots;
        int extra = spaces % slots;
        StringBuilder sb = new StringBuilder(maxWidth);
        for (int i = 0; i < words.size() - 1; i++) {
            sb.append(words.get(i));
            int gap = base + (i < extra ? 1 : 0);
            sb.append(" ".repeat(Math.max(0, gap)));
        }
        sb.append(words.get(words.size() - 1));
        return sb.toString();
    }
}


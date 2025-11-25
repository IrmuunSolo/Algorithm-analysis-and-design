package com.example.justify;

import java.util.List;

public class JustifyHelper {
    private JustifyHelper() {}

    public static int lineLength(List<String> words, int i, int j) {
        int len = 0;
        for (int k = i; k <= j; k++) len += words.get(k).length();
        return len + (j - i);
    }

    public static int badness(int maxWidth, int used) {
        int rem = maxWidth - used;
        return rem * rem * rem;
    }

    public static String formatLine(List<String> words, int maxWidth, boolean isLast) {
        if (words.isEmpty()) return " ".repeat(maxWidth);
        if (words.size() == 1 || isLast) {
            String s = String.join(" ", words);
            if (s.length() >= maxWidth) {
                return s;
            }
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


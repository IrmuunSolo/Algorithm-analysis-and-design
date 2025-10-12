package com.example;

import java.util.*;

//java -cp target/algo_lab5-1.0-SNAPSHOT.jar com.example.Solution1

class Solution1 {
    public String longestNiceSubstring(String s) {
        if (s.length() < 2) {
            return "";
        }

        // set үүсгэж бүх тэмдэгтийг хадгална
        Set<Character> letters = new HashSet<>();
        for (char ch : s.toCharArray()) {
            letters.add(ch);
        }

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            // swapcase байхгүй бол "муу" тэмдэгт
            if (!(letters.contains(Character.toLowerCase(ch)) 
               && letters.contains(Character.toUpperCase(ch)))) {
                String left = longestNiceSubstring(s.substring(0, i));
                String right = longestNiceSubstring(s.substring(i + 1));

                // урт нь адил бол earliest occurrence-г сонгоно
                if (left.length() >= right.length()) {
                    return left;
                } else {
                    return right;
                }
            }
        }

        // бүх тэмдэгт nice байсан → string-ээ буцаана
        return s;
    }
}
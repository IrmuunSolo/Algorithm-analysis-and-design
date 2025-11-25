package com.example.justify;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class JustifyTest {
    static int badness(List<String> lines, int width) {
        int sum = 0;
        for (int i = 0; i < lines.size() - 1; i++) {
            int used = lines.get(i).length();
            if (used > width) return Integer.MAX_VALUE / 4;
            int rem = width - used;
            sum += rem * rem * rem;
        }
        return sum;
    }

    @Test
    void greedyBasic() {
        String text = "Dynamic programming optimizes text justification by minimizing cost";
        List<String> words = Arrays.asList(text.split("\\s+"));
        int w = 25;
        List<String> lines = GreedyJustifier.justify(words, w);
        for (int i = 0; i < lines.size(); i++) {
            if (i == lines.size() - 1) {
                assertTrue(lines.get(i).stripTrailing().length() <= w);
            } else {
                assertEquals(w, lines.get(i).length());
            }
        }
    }

    @Test
    void dpNoWorseThanGreedy() {
        String text = "Шунахай арга ба динамик программчлалын хооронд глобал ба локал шийдлийн ялгаа бий.";
        List<String> words = Arrays.asList(text.split("\\s+"));
        int w = 26;
        List<String> g = GreedyJustifier.justify(words, w);
        List<String> d = DPJustifier.justify(words, w);
        assertTrue(badness(d, w) <= badness(g, w));
    }

    @Test
    void exactFitLine() {
        List<String> words = Arrays.asList("abcd", "efg"); // 4 + 1 + 3 = 8
        int w = 8;
        List<String> lines = DPJustifier.justify(words, w);
        assertEquals(1, lines.size());
        assertEquals("abcd efg", lines.get(0).stripTrailing());
    }

    @Test
    void preservesAllWordsOrder() {
        List<String> words = Arrays.asList("one", "two", "three", "four");
        int w = 9;
        List<String> lines = GreedyJustifier.justify(words, w);
        String joined = String.join(" ", words);
        String outJoined = String.join(" ", lines).replaceAll("\\s+", " ").trim();
        assertEquals(joined, outJoined);
    }

    @Test
    void singleOverwidthWordStaysIntact() {
        String longWord = "x".repeat(35);
        int w = 20;
        List<String> lines = DPJustifier.justify(List.of(longWord), w);
        assertEquals(1, lines.size());
        assertEquals(longWord, lines.get(0).trim());
    }

    @Test
    void dpKeepsPerfectPairTogether() {
        List<String> words = Arrays.asList("aaaaa", "bbbbb", "cc");
        int w = 11; // "aaaaa bbbbb" fits perfectly
        List<String> lines = DPJustifier.justify(words, w);
        assertTrue(lines.stream().anyMatch(s -> s.stripTrailing().equals("aaaaa bbbbb")));
    }
}

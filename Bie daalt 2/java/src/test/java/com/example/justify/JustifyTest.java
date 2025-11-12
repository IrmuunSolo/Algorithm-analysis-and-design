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
}


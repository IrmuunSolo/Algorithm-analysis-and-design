package com.example.justify;

import java.util.*;

public class DPJustifier {
    static int lineLength(List<String> words, int i, int j) {
        int len = 0;
        for (int k = i; k <= j; k++) len += words.get(k).length();
        return len + (j - i);
    }

    static int badness(int maxWidth, int used) {
        int rem = maxWidth - used;
        return rem * rem * rem;
    }

    public static List<String> justify(List<String> words, int maxWidth) {
        int n = words.size();
        long INF = Long.MAX_VALUE / 4;
        long[] cost = new long[n + 1];
        int[] nextBreak = new int[n + 1];
        cost[n] = 0; nextBreak[n] = -1;
        for (int i = n - 1; i >= 0; i--) {
            long best = INF; int bestJ = -1;
            for (int j = i; j < n; j++) {
                int used = lineLength(words, i, j);
                if (used > maxWidth) break;
                long c = (j == n - 1) ? 0 : badness(maxWidth, used);
                long total = c + cost[j + 1];
                if (total < best) { best = total; bestJ = j; }
            }
            cost[i] = best; nextBreak[i] = bestJ;
        }

        List<List<String>> lines = new ArrayList<>();
        for (int i = 0; i < n; ) {
            int j = nextBreak[i];
            if (j < i) j = i;
            lines.add(new ArrayList<>(words.subList(i, j + 1)));
            i = j + 1;
        }

        List<String> out = new ArrayList<>();
        for (int idx = 0; idx < lines.size(); idx++) {
            List<String> lw = lines.get(idx);
            boolean isLast = idx == lines.size() - 1;
            out.add(GreedyJustifier.formatLine(lw, maxWidth, isLast));
        }
        return out;
    }
}


package com.example.justify;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.management.ManagementFactory;
import com.sun.management.ThreadMXBean;

public class App {
    static int promptInt(BufferedReader br, String msg) throws IOException {
        while (true) {
            System.out.print(msg);
            String s = br.readLine();
            try {
                int v = Integer.parseInt(s.trim());
                if (v > 0)
                    return v;
            } catch (Exception ignored) {
            }
            System.out.println("Wrong input. Please enter again.");
        }
    }

    static String promptAlgo(BufferedReader br) throws IOException {
        System.out.println("Choose algorithm: DP -> 1, greedy -> 2");
        while (true) {
            System.out.print("> ");
            String s = br.readLine();
            if (s == null)
                return "greedy";
            s = s.trim();
            if (s.equals("1"))
                return "dp";
            if (s.equals("2"))
                return "greedy";
            System.out.println("Please enter only 1 or 2.");
        }
    }

    static String promptText(BufferedReader br) throws IOException {
        System.out.println("Enter your text (end with a blank line):");
        StringBuilder sb = new StringBuilder();
        while (true) {
            String ln = br.readLine();
            if (ln == null || ln.trim().isEmpty())
                break;
            sb.append(ln).append('\n');
        }
        return sb.toString();
    }

    static List<String> tokenize(String text) {
        String[] parts = text.trim().split("\\s+");
        List<String> words = new ArrayList<>();
        for (String p : parts)
            if (!p.isEmpty())
                words.add(p);
        return words;
    }

    public static void main(String[] argv) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        int width = promptInt(br, "Row width:");
        String algo = promptAlgo(br);
        String text = promptText(br);
        List<String> words = tokenize(text);
        if (words.isEmpty()) {
            System.out.println("There is no text to enter.");
            return;
        }

        // Measure allocated bytes (thread) and retained heap around the algorithm
        Runtime rt = Runtime.getRuntime();
        ThreadMXBean tmx = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        boolean supported = tmx.isThreadAllocatedMemorySupported();
        boolean enabledBefore = tmx.isThreadAllocatedMemoryEnabled();
        if (supported && !enabledBefore)
            tmx.setThreadAllocatedMemoryEnabled(true);
        long tid = Thread.currentThread().getId();

        rt.gc();
        // Capture memory before
        long usedBefore = rt.totalMemory() - rt.freeMemory();
        long allocBefore = supported ? tmx.getThreadAllocatedBytes(tid) : -1L;
        long t0 = System.nanoTime();
        List<String> lines = "dp".equalsIgnoreCase(algo)
                ? DPJustifier.justify(words, width)
                : GreedyJustifier.justify(words, width);
        long t1 = System.nanoTime();
        // Capture memory after
        long usedAfter = rt.totalMemory() - rt.freeMemory();
        long allocAfter = supported ? tmx.getThreadAllocatedBytes(tid) : -1L;
        if (supported && !enabledBefore)
            tmx.setThreadAllocatedMemoryEnabled(false);

        System.out.println("Result:");
        for (String ln : lines)
            System.out.println(ln);

        double ms = (t1 - t0) / 1e6;
        long usedBytes;
        if (allocBefore >= 0 && allocAfter >= 0) {
            usedBytes = Math.max(0L, allocAfter - allocBefore);
        } else {
            // Fallback approximation if thread allocated bytes is unsupported
            usedBytes = Math.max(0L, usedAfter - usedBefore);
        }

        System.out.printf("%nExecution time: %.3f ms%n", ms);
        System.out.printf("Used memory: %.1f KiB%n", usedBytes / 1024.0);
    }
}

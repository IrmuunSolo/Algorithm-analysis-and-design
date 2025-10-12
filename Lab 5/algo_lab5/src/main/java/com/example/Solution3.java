package com.example;

import java.util.*;

//java -cp target/algo_lab5-1.0-SNAPSHOT.jar com.example.Solution3

public class Solution3 {
    static int[] a;
    static int[] ans;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        StringBuilder out = new StringBuilder();

        while (t-- > 0) {
            int n = sc.nextInt();
            a = new int[n];
            ans = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = sc.nextInt();
            }

            solve(0, n - 1, 0);

            for (int i = 0; i < n; i++) {
                out.append(ans[i]).append(" ");
            }
            out.append("\n");
        }

        System.out.print(out.toString());
        sc.close();
    }

    // divide-and-conquer function
    static void solve(int l, int r, int depth) {
        if (l > r) return;
        // find index of max element
        int idx = l;
        for (int i = l; i <= r; i++) {
            if (a[i] > a[idx]) {
                idx = i;
            }
        }
        ans[idx] = depth;
        solve(l, idx - 1, depth + 1);
        solve(idx + 1, r, depth + 1);
    }
}


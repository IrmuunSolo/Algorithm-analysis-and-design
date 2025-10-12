package com.example;

import java.util.Arrays;

public class Sorts {

    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    public static void mergeSort(int[] a) {
        if (a == null || a.length <= 1)
            return;
        mergeSortRec(a, 0, a.length - 1);
    }

    private static void mergeSortRec(int[] a, int l, int r) {
        if (l >= r)
            return;
        int m = l + (r - l) / 2;
        mergeSortRec(a, l, m);
        mergeSortRec(a, m + 1, r);
        merge(a, l, m, r);
    }

    private static void merge(int[] a, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        System.arraycopy(a, l, L, 0, n1);
        System.arraycopy(a, m + 1, R, 0, n2);
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j])
                a[k++] = L[i++];
            else
                a[k++] = R[j++];
        }
        while (i < n1)
            a[k++] = L[i++];
        while (j < n2)
            a[k++] = R[j++];
    }

    public static int maxDivideConquer(int[] a) {
        if (a == null || a.length == 0)
            throw new IllegalArgumentException("Empty array");
        return Max(a, 0, a.length - 1);
    }

    public static int Max(int a[], int l, int r) {

        if (l == r)
            return a[l];
        int m = l + (r - l) / 2;
        int leftMax = Max(a, l, m);
        int rightMax = Max(a, m + 1, r);
        return Math.max(leftMax, rightMax);

    }

    public static String toStr(int[] a) {
        return Arrays.toString(a);
    }
}

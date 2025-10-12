package com.example;

public class Searchs {

    public static int binarySearchRecursive(int[] a, int key) {
        return bsRec(a, 0, a.length - 1, key);
    }

    private static int bsRec(int[] a, int l, int r, int key) {
        if (l > r)
            return -1;
        int m = l + (r - l) / 2;
        if (a[m] == key)
            return m;
        else if (a[m] > key)
            return bsRec(a, l, m - 1, key);
        else
            return bsRec(a, m + 1, r, key);
    }
}

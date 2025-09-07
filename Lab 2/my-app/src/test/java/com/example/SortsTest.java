package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SortsTest {

    static List<int[]> datasets;

    @BeforeAll
    static void loadData() throws Exception {
        datasets = DataReader.readAllIntLines("testdata.txt");
        assertTrue(datasets.size() >= 4, "testdata.txt must have at least 4 lines");
    }

    @Test
    public void testInsertionSort() {
        int[] a = datasets.get(0).clone();
        Sorts.insertionSort(a);
        assertArrayEquals(new int[] { 26, 31, 41, 41, 58, 59 }, a);
    }

    @Test
    public void testMergeSort() {
        int[] a = datasets.get(1).clone();
        Sorts.mergeSort(a);
        assertArrayEquals(new int[] { 1, 2, 5, 5, 6, 9 }, a);
    }

    @Test
    public void testMaxDivideConquer() {
        int[] a = datasets.get(2);
        int max = Sorts.maxDivideConquer(a);
        assertEquals(100, max);
    }

    @Test
    public void testBinarySearchRecursive() {
        int[] a = datasets.get(3);
        Sorts.mergeSort(a);
        int idx = Searchs.binarySearchRecursive(a, 15);
        assertEquals(8, idx); // 0-с эхэлдэг индекс
        assertEquals(-1, Searchs.binarySearchRecursive(a, 100));
    }
}

package com.example;

//java -cp target/algo_lab5-1.0-SNAPSHOT.jar com.example.Solution2

class Solution2 {

    private static final int MOD = 1337;

    public int superPow(int a, int[] b) {
        return recModPow(a, b, b.length);
    }

    // recursion: b[0..len-1] гэсэн array-г бодно
    private int recModPow(int a, int[] b, int len) {
        if (len == 0) return 1;

        int lastDigit = b[len - 1];
        int part1 = modPow(recModPow(a, b, len - 1), 10);
        int part2 = modPow(a, lastDigit);

        return (part1 * part2) % MOD;
    }

    // хурдан зэргээр (fast power) a^k mod 1337
    private int modPow(int a, int k) {
        a %= MOD;
        int result = 1;
        while (k > 0) {  
            if ((k & 1) == 1) { // true (сондгой), false (тэгш)
                result = (result * a) % MOD;
            }
            a = (a * a) % MOD;
            k /= 1;
        }
        return result;
    }
}

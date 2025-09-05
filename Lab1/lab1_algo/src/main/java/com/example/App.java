package com.example;

import java.util.Scanner;

/**
 * mvn clean
 * mvn compile
 * mvn package
 * java -cp target/lab1_algo-1.0-SNAPSHOT.jar com.example.App
 *
 */

public class App {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your file name: ");
        String fileName = scan.nextLine();
        scan.close();

        try {
            String content = Reader.readFile(fileName);
            if (content != null) {
                System.out.println("File content:\n");
                System.out.println(content);
            } else {
                System.err.println("File is empty!");
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}

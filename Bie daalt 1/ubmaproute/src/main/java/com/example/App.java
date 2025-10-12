package com.example;

import javax.swing.SwingUtilities;

import com.example.ui.UBMapFrame;

// mvn -q package
// mvn -q exec:java 

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UBMapFrame frame = new UBMapFrame();
            frame.setVisible(true);
        });
    }
}

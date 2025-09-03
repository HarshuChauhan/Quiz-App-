package com.onlinequiz;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize DB and default admin
        Database.init();
        SwingUtilities.invokeLater(() -> {
            new ui.LoginFrame();
        });
    }
}

package com.onlinequiz.ui;

import com.onlinequiz.dao.UserDAO;
import com.onlinequiz.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("Online Quiz - Login");
        setSize(400,220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }

    private void init() {
        JPanel p = new JPanel(new GridLayout(4,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(new JLabel("Username:"));
        userField = new JTextField();
        p.add(userField);
        p.add(new JLabel("Password:"));
        passField = new JPasswordField();
        p.add(passField);

        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register");
        p.add(loginBtn);
        p.add(regBtn);

        add(p);

        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String pw = new String(passField.getPassword());
            if (u.isEmpty() || pw.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter both fields"); return; }
            com.onlinequiz.model.User user = UserDAO.authenticate(u, pw);
            if (user != null) {
                dispose();
                if (user.isAdmin()) new AdminFrame(user);
                else new UserHomeFrame(user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        });

        regBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String pw = new String(passField.getPassword());
            if (u.isEmpty() || pw.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter both fields"); return; }
            if (UserDAO.exists(u)) { JOptionPane.showMessageDialog(this, "Username exists"); return; }
            UserDAO.createUser(u, pw, false);
            JOptionPane.showMessageDialog(this, "Registered. You can login now.");
        });
    }
}

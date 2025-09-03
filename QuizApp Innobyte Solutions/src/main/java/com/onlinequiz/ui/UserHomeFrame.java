package com.onlinequiz.ui;

import com.onlinequiz.model.User;
import com.onlinequiz.dao.QuizDAO;
import com.onlinequiz.dao.AttemptDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class UserHomeFrame extends JFrame {
    private User user;
    private DefaultListModel<String> listModel;
    private List<Map<String,Object>> quizzes;

    public UserHomeFrame(User user) {
        this.user = user;
        setTitle("Welcome, " + user.getUsername());
        setSize(700,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init() {
        JPanel main = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<>();
        JList<String> quizList = new JList<>(listModel);
        loadQuizzes();
        main.add(new JScrollPane(quizList), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(4,1,6,6));
        JButton takeBtn = new JButton("Take Quiz");
        JButton attemptsBtn = new JButton("My Attempts");
        JButton lbBtn = new JButton("Leaderboard");
        right.add(takeBtn); right.add(attemptsBtn); right.add(lbBtn);
        main.add(right, BorderLayout.EAST);
        add(main);

        takeBtn.addActionListener(e -> {
            int idx = quizList.getSelectedIndex();
            if (idx<0) { JOptionPane.showMessageDialog(this, "Select quiz"); return; }
            int quizId = (int)quizzes.get(idx).get("id");
            new QuizFrame(user, quizId);
        });

        attemptsBtn.addActionListener(e -> {
            List<Map<String,Object>> attempts = AttemptDAO.getAttemptsForUser(user.getId());
            StringBuilder sb = new StringBuilder();
            for (Map<String,Object> a : attempts) {
                sb.append(a.get("quiz_title")).append(" - ").append(a.get("score")).append("/"+a.get("total")).append(" at ").append(a.get("taken_at")).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.length()==0?"No attempts":sb.toString());
        });

        lbBtn.addActionListener(e -> {
            int idx = quizList.getSelectedIndex();
            if (idx<0) { JOptionPane.showMessageDialog(this, "Select quiz"); return; }
            int quizId = (int)quizzes.get(idx).get("id");
            new LeaderboardFrame(quizId);
        });
    }

    private void loadQuizzes() {
        quizzes = QuizDAO.listQuizzes();
        listModel.clear();
        for (Map<String,Object> q : quizzes) {
            listModel.addElement(q.get("id") + ": " + q.get("title"));
        }
    }
}

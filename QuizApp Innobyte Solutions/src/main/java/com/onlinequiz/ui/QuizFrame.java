package com.onlinequiz.ui;

import com.onlinequiz.dao.QuizDAO;
import com.onlinequiz.dao.AttemptDAO;
import com.onlinequiz.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class QuizFrame extends JFrame {
    private User user;
    private int quizId;
    private java.util.List<Map<String,Object>> questions;
    private int index = 0;
    private int score = 0;
    private JPanel center;
    private ButtonGroup bg;

    public QuizFrame(User user, int quizId) {
        this.user = user;
        this.quizId = quizId;
        setTitle("Quiz");
        setSize(600,400);
        setLocationRelativeTo(null);
        questions = QuizDAO.getQuestions(quizId);
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions in this quiz");
            return;
        }
        init();
        setVisible(true);
    }

    private void init() {
        setLayout(new BorderLayout());
        center = new JPanel(new BorderLayout());
        add(center, BorderLayout.CENTER);
        JButton next = new JButton("Submit");
        add(next, BorderLayout.SOUTH);
        showQuestion();
        next.addActionListener(e -> {
            int selected = getSelectedOption();
            if (selected==-1) { JOptionPane.showMessageDialog(this, "Select an option"); return; }
            char correct = (char)questions.get(index).get("correct");
            char chosen = "ABCD".charAt(selected);
            if (chosen==correct) {
                score++;
                JOptionPane.showMessageDialog(this, "Correct!");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect. Correct: " + correct);
            }
            index++;
            if (index < questions.size()) {
                showQuestion();
            } else {
                AttemptDAO.recordAttempt(user.getId(), quizId, score, questions.size());
                JOptionPane.showMessageDialog(this, "Quiz finished. Score: " + score + "/" + questions.size());
                dispose();
            }
        });
    }

    private void showQuestion() {
        center.removeAll();
        Map<String,Object> q = questions.get(index);
        JTextArea qta = new JTextArea((String)q.get("qtext"));
        qta.setLineWrap(true); qta.setWrapStyleWord(true); qta.setEditable(false);
        center.add(new JScrollPane(qta), BorderLayout.NORTH);
        JPanel opts = new JPanel(new GridLayout(4,1));
        JRadioButton rA = new JRadioButton("A. " + (String)q.get("a"));
        JRadioButton rB = new JRadioButton("B. " + (String)q.get("b"));
        JRadioButton rC = new JRadioButton("C. " + (String)q.get("c"));
        JRadioButton rD = new JRadioButton("D. " + (String)q.get("d"));
        bg = new ButtonGroup();
        bg.add(rA); bg.add(rB); bg.add(rC); bg.add(rD);
        opts.add(rA); opts.add(rB); opts.add(rC); opts.add(rD);
        center.add(opts, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private int getSelectedOption() {
        Enumeration<AbstractButton> buttons = bg.getElements();
        int idx = 0;
        while (buttons.hasMoreElements()) {
            AbstractButton b = buttons.nextElement();
            if (b.isSelected()) return idx;
            idx++;
        }
        return -1;
    }
}

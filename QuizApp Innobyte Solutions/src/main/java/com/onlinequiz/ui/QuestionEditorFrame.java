package com.onlinequiz.ui;

import com.onlinequiz.dao.QuizDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuestionEditorFrame extends JFrame {
    private int quizId;
    public QuestionEditorFrame(int quizId, String quizTitle) {
        this.quizId = quizId;
        setTitle("Questions - " + quizTitle);
        setSize(500,400);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }

    private void init() {
        JPanel p = new JPanel(new GridLayout(8,1,6,6));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JTextArea qtext = new JTextArea(3,40);
        JTextField a = new JTextField();
        JTextField b = new JTextField();
        JTextField c = new JTextField();
        JTextField d = new JTextField();
        JComboBox<String> correct = new JComboBox<>(new String[]{"A","B","C","D"});
        p.add(new JLabel("Question:")); p.add(new JScrollPane(qtext));
        p.add(new JLabel("Option A:")); p.add(a);
        p.add(new JLabel("Option B:")); p.add(b);
        p.add(new JLabel("Option C:")); p.add(c);
        p.add(new JLabel("Option D:")); p.add(d);
        p.add(new JLabel("Correct Option:")); p.add(correct);
        JButton add = new JButton("Add Question");
        add(p, BorderLayout.CENTER);
        add(add, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            String qt = qtext.getText().trim();
            String A = a.getText().trim();
            String B = b.getText().trim();
            String C = c.getText().trim();
            String D = d.getText().trim();
            char corr = ((String)correct.getSelectedItem()).charAt(0);
            if (qt.isEmpty()) { JOptionPane.showMessageDialog(this, "Question text required"); return; }
            QuizDAO.addQuestion(quizId, qt, A, B, C, D, corr);
            JOptionPane.showMessageDialog(this, "Added");
            qtext.setText(""); a.setText(""); b.setText(""); c.setText(""); d.setText("");
        });
    }
}

package com.onlinequiz.ui;

import com.onlinequiz.dao.QuizDAO;
import com.onlinequiz.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class AdminFrame extends JFrame {
    private User admin;
    private DefaultListModel<String> quizListModel;
    private JList<String> quizList;
    private java.util.List<Map<String,Object>> quizzes;

    public AdminFrame(User admin) {
        this.admin = admin;
        setTitle("Admin Panel - " + admin.getUsername());
        setSize(700,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init() {
        JPanel main = new JPanel(new BorderLayout());
        quizListModel = new DefaultListModel<>();
        quizList = new JList<>(quizListModel);
        loadQuizzes();
        main.add(new JScrollPane(quizList), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(6,1,6,6));
        JTextField titleF = new JTextField();
        JTextField descF = new JTextField();
        right.add(new JLabel("Title:")); right.add(titleF);
        right.add(new JLabel("Description:")); right.add(descF);
        JButton createBtn = new JButton("Create Quiz");
        right.add(createBtn);
        main.add(right, BorderLayout.EAST);

        add(main);

        createBtn.addActionListener(e -> {
            String t = titleF.getText().trim();
            String d = descF.getText().trim();
            if (t.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter title"); return; }
            int id = QuizDAO.createQuiz(t,d);
            if (id!=-1) {
                JOptionPane.showMessageDialog(this, "Quiz created. Select it and add questions.");
                loadQuizzes();
            }
        });

        quizList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount()==2) {
                    int idx = quizList.getSelectedIndex();
                    if (idx>=0) {
                        Map<String,Object> q = quizzes.get(idx);
                        new QuestionEditorFrame((int)q.get("id"), (String)q.get("title"));
                    }
                }
            }
        });
    }

    private void loadQuizzes() {
        quizzes = QuizDAO.listQuizzes();
        quizListModel.clear();
        for (Map<String,Object> q : quizzes) {
            quizListModel.addElement(q.get("id") + ": " + q.get("title"));
        }
    }
}

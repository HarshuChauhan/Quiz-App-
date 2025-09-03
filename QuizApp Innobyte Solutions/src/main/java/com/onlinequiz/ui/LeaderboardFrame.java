package com.onlinequiz.ui;

import com.onlinequiz.dao.AttemptDAO;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LeaderboardFrame extends JFrame {
    public LeaderboardFrame(int quizId) {
        setTitle("Leaderboard");
        setSize(400,300);
        setLocationRelativeTo(null);
        init(quizId);
        setVisible(true);
    }

    private void init(int quizId) {
        java.util.List<Map<String,Object>> list = AttemptDAO.leaderboard(quizId);
        StringBuilder sb = new StringBuilder();
        int i=1;
        for (Map<String,Object> r : list) {
            sb.append(i++).append(". ").append(r.get("username")).append(" - ").append(r.get("score")).append("\n");
        }
        if (sb.length()==0) sb.append("No attempts yet");
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        add(new JScrollPane(ta));
    }
}

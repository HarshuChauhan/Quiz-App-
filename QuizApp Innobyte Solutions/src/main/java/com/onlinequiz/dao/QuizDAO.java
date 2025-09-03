package com.onlinequiz.dao;

import com.onlinequiz.Database;
import java.sql.*;
import java.util.*;

public class QuizDAO {
    public static int createQuiz(String title, String description) {
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("INSERT INTO quizzes(title,description) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, title);
            p.setString(2, description);
            p.executeUpdate();
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public static List<Map<String,Object>> listQuizzes() {
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT id,title,description FROM quizzes")) {
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Map<String,Object> m = new HashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("title", rs.getString("title"));
                m.put("description", rs.getString("description"));
                out.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    public static void addQuestion(int quizId, String qtext, String a, String b, String copt, String d, char correct) {
        try (Connection conn = Database.getConnection();
             PreparedStatement p = conn.prepareStatement(
                "INSERT INTO questions(quiz_id,question_text,option_a,option_b,option_c,option_d,correct_option) VALUES(?,?,?,?,?,?,?)")) {
            p.setInt(1, quizId);
            p.setString(2, qtext);
            p.setString(3, a);
            p.setString(4, b);
            p.setString(5, copt);
            p.setString(6, d);
            p.setString(7, String.valueOf(correct));
            p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static List<Map<String,Object>> getQuestions(int quizId) {
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM questions WHERE quiz_id = ?")) {
            p.setInt(1, quizId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Map<String,Object> m = new HashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("qtext", rs.getString("question_text"));
                m.put("a", rs.getString("option_a"));
                m.put("b", rs.getString("option_b"));
                m.put("c", rs.getString("option_c"));
                m.put("d", rs.getString("option_d"));
                m.put("correct", rs.getString("correct_option").charAt(0));
                out.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }
}

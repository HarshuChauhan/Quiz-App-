package com.onlinequiz.dao;

import com.onlinequiz.Database;
import java.sql.*;
import java.util.*;

public class AttemptDAO {
    public static void recordAttempt(int userId, int quizId, int score, int total) {
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("INSERT INTO attempts(user_id,quiz_id,score,total) VALUES(?,?,?,?)")) {
            p.setInt(1, userId);
            p.setInt(2, quizId);
            p.setInt(3, score);
            p.setInt(4, total);
            p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static List<Map<String,Object>> getAttemptsForUser(int userId) {
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT a.id,a.score,a.total,a.taken_at,q.title FROM attempts a JOIN quizzes q ON a.quiz_id=q.id WHERE user_id = ? ORDER BY taken_at DESC")) {
            p.setInt(1, userId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Map<String,Object> m = new HashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("score", rs.getInt("score"));
                m.put("total", rs.getInt("total"));
                m.put("taken_at", rs.getString("taken_at"));
                m.put("quiz_title", rs.getString("title"));
                out.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    public static List<Map<String,Object>> leaderboard(int quizId) {
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT u.username, MAX(a.score) as best FROM attempts a JOIN users u ON a.user_id=u.id WHERE a.quiz_id = ? GROUP BY u.id ORDER BY best DESC LIMIT 10")) {
            p.setInt(1, quizId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Map<String,Object> m = new HashMap<>();
                m.put("username", rs.getString("username"));
                m.put("score", rs.getInt("best"));
                out.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }
}

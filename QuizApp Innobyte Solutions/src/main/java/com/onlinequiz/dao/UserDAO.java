package com.onlinequiz.dao;

import com.onlinequiz.Database;
import com.onlinequiz.model.User;
import java.sql.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.MessageDigest;

public class UserDAO {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static boolean exists(String username) {
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            p.setString(1, username);
            ResultSet rs = p.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createUser(String username, String password, boolean isAdmin) {
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(
                 "INSERT INTO users(username,password_hash,salt,is_admin) VALUES(?,?,?,?)")) {
            String salt = generateSalt();
            String hash = hashPassword(password, salt);
            p.setString(1, username);
            p.setString(2, hash);
            p.setString(3, salt);
            p.setInt(4, isAdmin ? 1 : 0);
            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User authenticate(String username, String password) {
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT id,password_hash,salt,is_admin FROM users WHERE username = ?")) {
            p.setString(1, username);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("password_hash");
                String salt = rs.getString("salt");
                String calc = hashPassword(password, salt);
                if (calc.equals(hash)) {
                    return new User(rs.getInt("id"), username, rs.getInt("is_admin") == 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generateSalt() {
        byte[] b = new byte[16];
        RANDOM.nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes("UTF-8"));
            byte[] digest = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte by : digest) {
                sb.append(String.format("%02x", by));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

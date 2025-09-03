package com.onlinequiz;

import java.sql.*;
import java.nio.file.*;

public class Database {
    private static final String DB_FILE = "onlinequiz.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            // users table
            s.execute("""CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                salt TEXT NOT NULL,
                is_admin INTEGER NOT NULL DEFAULT 0
            )""");

            // quizzes
            s.execute("""CREATE TABLE IF NOT EXISTS quizzes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT
            )""");

            // questions
            s.execute("""CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                quiz_id INTEGER NOT NULL,
                question_text TEXT NOT NULL,
                option_a TEXT,
                option_b TEXT,
                option_c TEXT,
                option_d TEXT,
                correct_option CHAR(1),
                FOREIGN KEY(quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
            )""");

            // attempts
            s.execute("""CREATE TABLE IF NOT EXISTS attempts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                quiz_id INTEGER NOT NULL,
                score INTEGER NOT NULL,
                total INTEGER NOT NULL,
                taken_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES users(id),
                FOREIGN KEY(quiz_id) REFERENCES quizzes(id)
            )""");

            // create default admin if not exists
            if (!UserDAO.exists("admin")) {
                UserDAO.createUser("admin", "admin123", true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

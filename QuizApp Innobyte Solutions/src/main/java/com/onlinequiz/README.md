# Online Quiz Application (Java Swing + SQLite)

## Overview
This is a simple Online Quiz Application built using Java (Swing UI) and SQLite via JDBC.
It supports:
- User registration & login (password hashing + salt)
- Admin: create/edit/delete quizzes and questions
- Take quiz: one question at a time with immediate feedback
- Recording attempts and displaying past scores
- Leaderboard per quiz

## Requirements
- Java 11 or higher
- Maven (to build and download dependencies)

## How to run
1. Clone or download this project.
2. Open terminal in project root and run:
   ```
   mvn package
   ```
3. After successful build, run the jar:
   ```
   java -jar target/online-quiz-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```
4. The application will create a local SQLite database file `onlinequiz.db` in the working directory.

## Notes
- Default admin user:
  - username: admin
  - password: admin123
- You can register new users from the login window.

## Limitations / Assumptions
- Uses an embedded SQLite DB (no external DB server needed).
- Basic Swing UI for demonstration; can be improved with JavaFX.
- Passwords are stored hashed with SHA-256 and a per-user salt.

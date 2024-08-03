/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication;
/**
 *
 * @author RandyMaximize
 */


import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;

    // Dependency Injection for UserDao
    public UserService(UserDao userDao) {
        if (userDao == null) {
            throw new IllegalArgumentException("UserDao must not be null");
        }
        this.userDao = userDao;
    }

    public void registerUser(User user) throws SQLException {
        validateUser(user); // Additional business logic
        try {
            userDao.addUser(user); // Fixed method call to addUser
        } catch (SQLException e) {
            // Log or handle specific SQLExceptions
            System.err.println("Error registering user: " + e.getMessage());
            throw e; // Rethrow for higher-level handling
        }
    }

    public User loginUser(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty() || password == null) {
            throw new IllegalArgumentException("Username and password must not be null or empty");
        }

        try {
            User user = userDao.getUser(username, password); // Fixed method call to getUser
            if (user == null) {
                throw new IllegalArgumentException("Invalid username or password");
            }
            return user;
        } catch (SQLException e) {
            // Log or handle specific SQLExceptions
            System.err.println("Error logging in user: " + e.getMessage());
            throw e; // Rethrow for higher-level handling
        }
    }

    // Basic validation for user registration
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (user.getAccountName() == null || user.getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("Account name must not be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        // Add more validations as needed
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication;
/**
 *
 * @author RandyMaximize
 */
import java.sql.*;
import java.util.*;

public class UserDao {
    private final Connection connection;
    public UserDao(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }
    

   public void addUser(User user) throws SQLException {
    String sql = "INSERT INTO Users (accountName, password, name, age, address, phoneNumber, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, user.getAccountName());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getName());
        statement.setInt(4, user.getAge());
        statement.setString(5, user.getAddress());
        statement.setString(6, user.getPhoneNumber());
        statement.setString(7, user.getEmail());
        statement.executeUpdate(); // Insert the new user
    } catch (SQLException e) {
        System.err.println("Error adding user: " + e.getMessage());
        throw e; // Re-throw for higher-level handling
    }
}


    public User getUser(String accountName, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE accountName = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountName);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToUser(resultSet);
                }
            }
        }

        return null;
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

public int getTotalUsers() throws SQLException {
    String sql = "SELECT COUNT(*) AS user_count FROM Users"; // Query to count users
    int totalUsers = 0;

    try (PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) { // Execute the query

        if (resultSet.next()) { // Check if there's a result
            totalUsers = resultSet.getInt("user_count"); // Retrieve the count
        }
    } catch (SQLException e) { // Handle SQL exceptions
        System.err.println("Error while fetching total user count.");
        e.printStackTrace(); // Log the error
        throw e; // Re-throw the exception after logging
    }

    return totalUsers; // Return the total count of users
}
public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToUser(resultSet); // Map the result to a User object
                }
            }
        }

        return null; // Return null if no user was found
    }


       // Method to retrieve a user by account name
    public User getUserByAccountName(String accountName) throws SQLException {
        String sql = "SELECT * FROM Users WHERE accountName = ?"; // SQL query with parameter
        User user = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountName); // Set the account name parameter

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) { // If there's a result
                    user = mapToUser(resultSet); // Map the result to a User object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by account name: " + e.getMessage());
            throw e; // Rethrow the exception for higher-level handling
        }

        return user; // Return the found user or null if not found
    }
    
public void updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET accountName = ?, password = ?, name = ?, age = ?, address = ?, phoneNumber = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getAccountName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setInt(4, user.getAge());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getPhoneNumber());
            statement.setString(7, user.getEmail());
            statement.setInt(8, user.getId());
            statement.executeUpdate(); // Execute the update
        }
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setAccountName(resultSet.getString("accountName"));
        user.setPassword(resultSet.getString("password")); // Handle sensitive data securely
        user.setName(resultSet.getString("name"));
        user.setAge(resultSet.getInt("age"));
        user.setAddress(resultSet.getString("address"));
        user.setPhoneNumber(resultSet.getString("phoneNumber"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }
     public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM Users"; // SQL query to fetch all users
        List<User> users = new ArrayList<>(); // Initialize an empty list

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate over the result set and create User objects
            while (resultSet.next()) {
                User user = mapToUser(resultSet);
                users.add(user); // Add to the list
            }
        }

        return users; // Return the list of users
    }
}

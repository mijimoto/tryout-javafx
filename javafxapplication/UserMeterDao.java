/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMeterDao {
    private final Connection connection;

    public UserMeterDao(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public void addUserMeter(UserMeter userMeter) throws SQLException {
        String sql = "INSERT INTO UserMeter (Meterid, id, PayCheck) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userMeter.getMeterId());
            statement.setInt(2, userMeter.getUserId());
            statement.setBoolean(3, userMeter.isPayCheck());
            statement.executeUpdate(); // Insert the new record
        }
    }

    public List<UserMeter> getAllUserMeters() throws SQLException {
        List<UserMeter> userMeters = new ArrayList<>();
        String sql = "SELECT * FROM UserMeter";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UserMeter userMeter = new UserMeter();
                userMeter.setMeterId(resultSet.getInt("Meterid"));
                userMeter.setUserId(resultSet.getInt("id"));
                userMeter.setPayCheck(resultSet.getBoolean("PayCheck")); // New field retrieval
                userMeters.add(userMeter);
            }
        }
        return userMeters;
    }

    public List<UserMeter> getUserMetersByUserId(int userId) throws SQLException {
        List<UserMeter> userMeters = new ArrayList<>();
        String sql = "SELECT * FROM UserMeter WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UserMeter userMeter = new UserMeter();
                    userMeter.setMeterId(resultSet.getInt("Meterid"));
                    userMeter.setUserId(resultSet.getInt("id"));
                    userMeter.setPayCheck(resultSet.getBoolean("PayCheck")); // Include PayCheck
                    userMeters.add(userMeter);
                }
            }
        }
        return userMeters;
    }

    public void deleteUserMeterByMeterId(int meterId) throws SQLException {
        String sql = "DELETE FROM UserMeter WHERE Meterid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, meterId);
            statement.executeUpdate(); // Delete the specified record
        }
    }

    public void deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM UserMeter WHERE id = ?"; // Delete records by User ID
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate(); // Execute the delete operation
        }
    }

    // New method to update PayCheck status
    public void updatePayCheckStatus(int meterId, boolean payCheck) throws SQLException {
        String sql = "UPDATE UserMeter SET PayCheck = ? WHERE Meterid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, payCheck);
            statement.setInt(2, meterId);
            statement.executeUpdate(); // Execute the update operation
        }
    }
    public UserMeter getUserMeterByMeterId(int meterId) throws SQLException {
        String sql = "SELECT * FROM UserMeter WHERE Meterid = ?";
        UserMeter userMeter = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, meterId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userMeter = new UserMeter();
                    userMeter.setMeterId(resultSet.getInt("Meterid"));
                    userMeter.setUserId(resultSet.getInt("id"));
                    userMeter.setPayCheck(resultSet.getBoolean("PayCheck"));
                }
            }
        }
        return userMeter;
    }
    
}

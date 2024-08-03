/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminHomeController implements Initializable {

    @FXML
    private Label totacc; // Label to display the total number of accounts
    @FXML
    private Label revenue; // Label to display the total revenue

    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            connection = dbConnection.DatabaseConnection();

            if (connection == null) {
                showError("Failed to establish a database connection.");
                return;
            }

            UserDao userDao = new UserDao(connection);
            int totalUsers = userDao.getTotalUsers(); // Get the total user count
            totacc.setText(String.valueOf(totalUsers)); // Display the total user count in the label

            // Calculate the revenue for all paid water meters
            double totalRevenue = calculateTotalRevenue();
            revenue.setText(String.format("%.2f Đ", totalRevenue)); // Display the revenue

        } catch (SQLException e) {
            showError("Error during initialization: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private double calculateTotalRevenue() throws SQLException {
        String query = """
            SELECT SUM(w.WaterMeter * 1500 * 1.05) AS totalRevenue
            FROM WaterMeter w
            JOIN UserMeter um ON um.Meterid = w.Meterid
            WHERE um.PayCheck = 1
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("totalRevenue"); // Get the sum of all paid water meters
            } else {
                return 0.0;
            }
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

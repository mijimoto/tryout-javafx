/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class WaterMeterDao {
    private final Connection connection;

    public WaterMeterDao(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public void addWaterMeter(WaterMeter waterMeter) throws SQLException {
        String sql = "INSERT INTO WaterMeter (WaterMeter, MeterMonth, MeterYear, MeterName) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, waterMeter.getWaterMeter());
            statement.setInt(2, waterMeter.getMeterMonth());
            statement.setInt(3, waterMeter.getMeterYear());
            statement.setString(4, waterMeter.getMeterName());
            statement.executeUpdate(); // Insert the new record
        }
    }
// Retrieves the latest Meterid
    public int getLatestWaterMeterId() throws SQLException {
        String sql = "SELECT MAX(Meterid) AS latest_id FROM WaterMeter";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("latest_id"); // Return the most recent Meterid
            }
        } catch (SQLException e) {
            System.err.println("Error fetching latest WaterMeter ID: " + e.getMessage());
            throw e; // Rethrow the exception for higher-level handling
        }
        return -1; // Return -1 if there's no data
    }
    
    public List<WaterMeter> getAllWaterMeters() throws SQLException {
        List<WaterMeter> waterMeters = new ArrayList<>();
        String sql = "SELECT * FROM WaterMeter";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                waterMeters.add(mapToWaterMeter(resultSet)); // Add to the list after mapping
            }
        }
        return waterMeters; // Return all water meter records
    }

    public List<WaterMeter> getWaterMetersByUserId(int userId) throws SQLException {
        List<WaterMeter> waterMeters = new ArrayList<>();
        String sql = "SELECT wm.Meterid, wm.WaterMeter, wm.MeterMonth, wm.MeterYear, wm.MeterName " +
                     "FROM WaterMeter wm " +
                     "JOIN UserMeter um ON wm.Meterid = um.Meterid " +
                     "WHERE um.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    WaterMeter waterMeter = new WaterMeter();
                    waterMeter.setMeterId(resultSet.getInt("Meterid"));
                    waterMeter.setWaterMeter(resultSet.getDouble("WaterMeter"));
                    waterMeter.setMeterMonth(resultSet.getInt("MeterMonth"));
                    waterMeter.setMeterYear(resultSet.getInt("MeterYear"));
                    waterMeter.setMeterName(resultSet.getString("MeterName"));
                    waterMeters.add(waterMeter); // Add to the list
                }
            }
        }
        return waterMeters;
    }

    public void deleteWaterMeter(int meterId) throws SQLException {
        String sql = "DELETE FROM WaterMeter WHERE Meterid = ?"; // SQL command to delete a water meter record
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, meterId); // Set the Meterid parameter
            statement.executeUpdate(); // Execute the delete operation
        }
    }

    private WaterMeter mapToWaterMeter(ResultSet resultSet) throws SQLException {
        WaterMeter waterMeter = new WaterMeter();
        waterMeter.setMeterId(resultSet.getInt("Meterid"));
        waterMeter.setWaterMeter(resultSet.getDouble("WaterMeter"));
        waterMeter.setMeterMonth(resultSet.getInt("MeterMonth"));
        waterMeter.setMeterYear(resultSet.getInt("MeterYear"));
        waterMeter.setMeterName(resultSet.getString("MeterName"));
        return waterMeter;
    }
     public Set<Integer> getDistinctMeterYears() throws SQLException {
        Set<Integer> years = new TreeSet<>();
        String sql = "SELECT DISTINCT MeterYear FROM WaterMeter";
        try (var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                years.add(resultSet.getInt("MeterYear"));
            }
        }
        return years;
    }
     public Set<Integer> getDistinctMeterMonths() throws SQLException {
        Set<Integer> months = new TreeSet<>();
        String sql = "SELECT DISTINCT MeterMonth FROM WaterMeter";
        try (var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                months.add(resultSet.getInt("MeterMonth"));
            }
        }
        return months;
    }
         public List<WaterMeter> getWaterMetersByMonthAndYear(int month, int year) throws SQLException {
        List<WaterMeter> waterMeters = new ArrayList<>();
        String sql = "SELECT Meterid, WaterMeter, MeterMonth, MeterYear, MeterName FROM WaterMeter WHERE MeterMonth = ? AND MeterYear = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, month);
            statement.setInt(2, year);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    WaterMeter waterMeter = new WaterMeter();
                    waterMeter.setMeterId(resultSet.getInt("Meterid"));
                    waterMeter.setWaterMeter(resultSet.getDouble("WaterMeter"));
                    waterMeter.setMeterMonth(resultSet.getInt("MeterMonth"));
                    waterMeter.setMeterYear(resultSet.getInt("MeterYear"));
                    waterMeter.setMeterName(resultSet.getString("MeterName"));
                    waterMeters.add(waterMeter); // Add to the list
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching WaterMeters for month " + month + " and year " + year + ": " + e.getMessage());
            throw e;
        }

        return waterMeters;
    }
         public List<WaterMeter> getUnpaidWaterMetersByMonthAndYear(int month, int year) throws SQLException {
        List<WaterMeter> unpaidWaterMeters = new ArrayList<>();

        String sql = """
            SELECT wm.*
            FROM WaterMeter wm
            LEFT JOIN UserMeter um ON wm.Meterid = um.Meterid
            WHERE wm.MeterMonth = ? AND wm.MeterYear = ?
            AND (um.PayCheck IS NULL OR um.PayCheck = 0)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WaterMeter waterMeter = mapToWaterMeter(rs);
                    unpaidWaterMeters.add(waterMeter); // Add to the list of unpaid WaterMeters
                }
            }
        }

        return unpaidWaterMeters; // Return the list of unpaid WaterMeters
    }
         public List<WaterMeter> getUnpaidWaterMetersByUserId(int userId) throws SQLException {
        List<WaterMeter> unpaidWaterMeters = new ArrayList<>();

        String sql = """
            SELECT wm.*
            FROM WaterMeter wm
            JOIN UserMeter um ON wm.Meterid = um.Meterid
            WHERE um.id = ? AND (um.PayCheck IS NULL OR um.PayCheck = 0)
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    WaterMeter waterMeter = new WaterMeter();
                    waterMeter.setMeterId(resultSet.getInt("Meterid"));
                    waterMeter.setWaterMeter(resultSet.getDouble("WaterMeter"));
                    waterMeter.setMeterMonth(resultSet.getInt("MeterMonth"));
                    waterMeter.setMeterYear(resultSet.getInt("MeterYear"));
                    waterMeter.setMeterName(resultSet.getString("MeterName"));

                    unpaidWaterMeters.add(waterMeter); // Add to the list of unpaid WaterMeters
                }
            }
        }

        return unpaidWaterMeters; // Return the list of unpaid WaterMeters
    }
         public List<WaterMeter> getUnpaidWaterMetersByUserIdAndMonthAndYear(int userId, int month, int year) throws SQLException {
    List<WaterMeter> unpaidWaterMeters = new ArrayList<>();

    String sql = """
        SELECT wm.*
        FROM WaterMeter wm
        JOIN UserMeter um ON wm.Meterid = um.Meterid
        WHERE um.id = ? AND wm.MeterMonth = ? AND wm.MeterYear = ?
          AND (um.PayCheck IS NULL OR um.PayCheck = 0)
    """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, userId);
        statement.setInt(2, month);
        statement.setInt(3, year);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                WaterMeter waterMeter = mapToWaterMeter(resultSet);
                unpaidWaterMeters.add(waterMeter); // Add to the list of unpaid WaterMeters
            }
        }
    }

    return unpaidWaterMeters; // Return the list of unpaid WaterMeters
}

}

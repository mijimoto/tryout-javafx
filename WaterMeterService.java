/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class WaterMeterService {
    private final WaterMeterDao waterMeterDao;
    private final UserMeterDao userMeterDao; // Initialize to prevent NullPointerException

    public WaterMeterService(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.waterMeterDao = new WaterMeterDao(connection);
        this.userMeterDao = new UserMeterDao(connection); // Proper initialization
    }

    public void addWaterMeter(WaterMeter waterMeter, int userId) throws SQLException {
        // Add a new water meter record
        waterMeterDao.addWaterMeter(waterMeter);

        // Get the ID of the newly added water meter
        int meterId = waterMeterDao.getLatestWaterMeterId();

        // Link the new water meter with the specified user
        UserMeter userMeter = new UserMeter();
        userMeter.setMeterId(meterId);
        userMeter.setUserId(userId);

        userMeterDao.addUserMeter(userMeter); // Update the UserMeter table
    }

    public List<WaterMeter> getAllWaterMeters() throws SQLException {
        return waterMeterDao.getAllWaterMeters(); // Get all water meter records
    }

    public List<WaterMeter> getWaterMetersByUserId(int userId) throws SQLException {
        return waterMeterDao.getWaterMetersByUserId(userId); // Get water meters by user ID
    }

   public void deleteWaterMeter(int meterId) throws SQLException {
        // Delete the corresponding UserMeter record
        userMeterDao.deleteUserMeterByMeterId(meterId);

        // Delete the WaterMeter record
        waterMeterDao.deleteWaterMeter(meterId);
    }
}

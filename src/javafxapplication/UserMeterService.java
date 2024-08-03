/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserMeterService {
    private final UserMeterDao userMeterDao;

    public UserMeterService(Connection connection) {
        this.userMeterDao = new UserMeterDao(connection); // Initialize UserMeterDao
    }

    public void addUserMeter(UserMeter userMeter) throws SQLException {
        if (userMeter == null) {
            throw new IllegalArgumentException("UserMeter must not be null");
        }
        userMeterDao.addUserMeter(userMeter); // Add UserMeter record
    }

    public List<UserMeter> getAllUserMeters() throws SQLException {
        return userMeterDao.getAllUserMeters(); // Get all UserMeter records
    }

    public List<UserMeter> getUserMetersByUserId(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        return userMeterDao.getUserMetersByUserId(userId); // Get UserMeter records by User ID
    }

    public void updatePayCheckStatus(int meterId, boolean payCheck) throws SQLException {
        userMeterDao.updatePayCheckStatus(meterId, payCheck); // Update the PayCheck status
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication;

public class WaterMeter {
    private int meterId;
    private double waterMeter;
    private int meterMonth;
    private int meterYear;
    private String meterName;

    // Getters
    public int getMeterId() {
        return meterId;
    }

    public double getWaterMeter() {
        return waterMeter;
    }

    public int getMeterMonth() {
        return meterMonth;
    }

    public int getMeterYear() {
        return meterYear;
    }

    public String getMeterName() {
        return meterName;
    }

    // Setters
    public void setMeterId(int meterId) {
        this.meterId = meterId;
    }

    public void setWaterMeter(double waterMeter) {
        this.waterMeter = waterMeter;
    }

    public void setMeterMonth(int meterMonth) {
        this.meterMonth = meterMonth;
    }

    public void setMeterYear(int meterYear) {
        this.meterYear = meterYear;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }
    
    private UserMeter userMeter; // Reference to UserMeter

    // Getter for UserMeter
    public UserMeter getUserMeter() {
        return userMeter;
    }

    // Setter for UserMeter
    public void setUserMeter(UserMeter userMeter) {
        this.userMeter = userMeter;
    }
    
}

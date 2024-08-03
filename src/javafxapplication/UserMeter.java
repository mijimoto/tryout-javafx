/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication;

public class UserMeter {
    private int meterId;
    private int userId;
    private boolean payCheck; // New field for PayCheck

    // Getters
    public int getMeterId() {
        return meterId;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isPayCheck() {
        return payCheck; // Getter for PayCheck
    }

    // Setters
    public void setMeterId(int meterId) {
        this.meterId = meterId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPayCheck(boolean payCheck) { // Setter for PayCheck
        this.payCheck = payCheck;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import java.sql.SQLException;
import java.net.URL;
import java.util.ResourceBundle;

public class NguoiDungController implements Initializable {
    @FXML
    private TextField accountName;
    
    @FXML
    private TextField password;
    
    @FXML
    private TextField name;
    
    @FXML
    private TextField age;
    
    @FXML
    private TextField address;
    
    @FXML
    private TextField phoneNumber;
    
    @FXML
    private TextField email;

    @FXML
    private Button editButton;
    
    @FXML
    private Button saveButton;

    private UserDao userDao; // Data Access Object
    private User currentUser; // Current user to display and edit

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDao = new UserDao(new DatabaseConnection().DatabaseConnection());
        if (userDao == null) {
            throw new IllegalStateException("UserDao is not initialized");
        }

        loadUserData(); // Load initial data into the text fields

        // Set text fields to non-editable by default
        setFieldsEditable(false);
    }

   private void loadUserData() {
    try {
        int userId = SessionManager.getInstance().getUserId(); // Get user ID from SessionManager
        currentUser = userDao.getUserById(userId); // Fetch the user using the ID

        if (currentUser != null) {
            accountName.setText(currentUser.getAccountName());
            password.setText(currentUser.getPassword());
            name.setText(currentUser.getName());
            age.setText(String.valueOf(currentUser.getAge()));
            address.setText(currentUser.getAddress());
            phoneNumber.setText(currentUser.getPhoneNumber());
            email.setText(currentUser.getEmail());
        } else {
            showAlert("Error", "User not found", "No user data available.");
        }
    } catch (SQLException e) {
        showAlert("Error", "Failed to load user data", e.getMessage());
    }
}


    private void setFieldsEditable(boolean isEditable) {
        accountName.setEditable(isEditable);
        password.setEditable(isEditable);
        name.setEditable(isEditable);
        age.setEditable(isEditable);
        address.setEditable(isEditable);
        phoneNumber.setEditable(isEditable);
        email.setEditable(isEditable);
    }

    @FXML
    void edit(ActionEvent event) {
        setFieldsEditable(true); // Allow editing
    }

    @FXML
private void save(ActionEvent event) {
    if (currentUser == null) {
        showAlert("Error", "Cannot save data", "No user data to save");
        return;
    }

    try {
        // Update user data with the current text field values
        currentUser.setAccountName(accountName.getText());
        currentUser.setPassword(password.getText());
        currentUser.setName(name.getText());
        currentUser.setAge(Integer.parseInt(age.getText()));
        currentUser.setAddress(address.getText());
        currentUser.setPhoneNumber(phoneNumber.getText());
        currentUser.setEmail(email.getText());

        // Update the user data in the database
        userDao.updateUser(currentUser); // This updates the existing user record

        showAlert("Success", "Data Saved", "User data has been updated");
        setFieldsEditable(false); // Disable editing
    } catch (SQLException e) {
        showAlert("Error", "Failed to save data", e.getMessage());
    } catch (NumberFormatException e) {
        showAlert("Error", "Invalid age format", "Age must be a number");
    }
}

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

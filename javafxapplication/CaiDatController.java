/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class CaiDatController implements Initializable {

    @FXML
    private Button logout; // Reference to the logout button
    @FXML
    private ChoiceBox<String> fontchooser; // Choice box for font selection

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the choice box with fonts
        fontchooser.getItems().addAll("Arial", "Verdana", "Tahoma"); // Available fonts

        // Add listener to handle font changes
        fontchooser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                changeFont(newVal); // Update font based on selection
            }
        });
    }

    private void changeFont(String font) {
        try {
            // Apply the selected font to the entire scene
            String fontStyle = String.format("-fx-font-family: '%s';", font);
            Scene currentScene = logout.getScene(); // Get the current scene
            currentScene.getRoot().setStyle(fontStyle); // Apply the custom style to the root node
        } catch (Exception e) {
            System.err.println("Error applying custom style: " + e.getMessage());
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            SessionManager.getInstance().setUserId(0); // Reset the session

            // Load the login scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication/DangNhap.fxml"));
            Parent root = loader.load(); // Load the login FXML

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); // Display the login screen

            JOptionPane.showMessageDialog(null, "Logout successful. You are now redirected to the login screen.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Logout failed: " + e.getMessage());
        }
    }
}

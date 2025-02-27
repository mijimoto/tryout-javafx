/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableColumn.SortType;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import java.util.logging.Logger;

public class QuanLiController implements Initializable {
    private static final Logger logger = Logger.getLogger(QuanLiController.class.getName());

    @FXML
    private TreeTableView<User> userTreeTable;

    @FXML
    private TreeTableColumn<User, Integer> idCol;

    @FXML
    private TreeTableColumn<User, String> accountNameCol;

    @FXML
    private TreeTableColumn<User, String> phoneNumberCol;

    @FXML
    private TreeTableColumn<User, String> emailCol;

    @FXML
    private Button sort;

    @FXML
    private Button delete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();
        populateUserTreeTable();

        // Set up button actions
        sort.setOnAction(event -> sortSelectedColumn());
        delete.setOnAction(event -> deleteSelectedUser());
    }

    private void setupColumns() {
        idCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        accountNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("accountName"));
        phoneNumberCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("phoneNumber"));
        emailCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("email"));
    }

    private void populateUserTreeTable() {
        Connection connection = null;
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            connection = dbConnection.DatabaseConnection();

            if (connection == null) {
                showError("Failed to establish a database connection.");
                return;
            }

            UserDao userDao = new UserDao(connection);

            // Get all users and add them to the TreeTableView
            List<User> users = userDao.getAllUsers();
            TreeItem<User> rootItem = new TreeItem<>(new User());

            for (User user : users) {
                TreeItem<User> userItem = new TreeItem<>(user);
                rootItem.getChildren().add(userItem);
            }

            userTreeTable.setRoot(rootItem);
            userTreeTable.setShowRoot(false);

        } catch (SQLException e) {
            showError("Error fetching user data from the database.");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close(); // Close the connection to avoid resource leaks
                } catch (SQLException e) {
                    logger.severe("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    private void sortSelectedColumn() {
        if (userTreeTable.getSortOrder().isEmpty()) {
            showError("No column selected for sorting.");
            return;
        }

        TreeTableColumn<User, ?> selectedColumn = userTreeTable.getSortOrder().get(0);
        if (selectedColumn.getSortType() == SortType.ASCENDING) {
            selectedColumn.setSortType(SortType.DESCENDING);
        } else {
            selectedColumn.setSortType(SortType.ASCENDING);
        }

        userTreeTable.sort(); // Reapply the sort order
    }

    @FXML
private void deleteSelectedUser() {
    TreeItem<User> selectedItem = userTreeTable.getSelectionModel().getSelectedItem();

    if (selectedItem == null) {
        showWarning("No Selection", "No User Selected", "Please select a user to delete.");
        return;
    }

    User userToDelete = selectedItem.getValue();
    Connection connection = null;
    try {
        DatabaseConnection dbConnection = new DatabaseConnection();
        connection = dbConnection.DatabaseConnection();

        UserDao userDao = new UserDao(connection);
        UserMeterDao userMeterDao = new UserMeterDao(connection);

        // Delete associated UserMeter records
        userMeterDao.deleteByUserId(userToDelete.getId());

        // Now delete the user
        userDao.deleteUser(userToDelete.getId());

        // Remove from the TreeTableView
        userTreeTable.getRoot().getChildren().remove(selectedItem);

    } catch (SQLException e) {
        showError("Failed to delete user from the database."+ e.getMessage());
    } finally {
        if (connection != null) {
            try {
                connection.close(); // Close the connection to avoid resource leaks
            } catch (SQLException e) {
                logger.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
}

    private void showError(String message) {
        logger.severe(message);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showWarning(String title, String header, String content) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
}
}

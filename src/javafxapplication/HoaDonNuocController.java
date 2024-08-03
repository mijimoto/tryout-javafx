/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class HoaDonNuocController implements Initializable {

    @FXML
    private TextField name;

    @FXML
    private TextField address;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField email;

    @FXML
    private TextField id;

    @FXML
    private ChoiceBox<Integer> WaterMonth;

    @FXML
    private ChoiceBox<Integer> WaterYear;

    @FXML
    private TreeTableView<WaterMeter> treeTableView;

    @FXML
    private TreeTableColumn<WaterMeter, String> Description;

    @FXML
    private TreeTableColumn<WaterMeter, Double> WaterMeterColumn;

    @FXML
    private TreeTableColumn<WaterMeter, String> WaterBill;

    @FXML
    private TreeTableColumn<WaterMeter, String> Tax;

    @FXML
    private TreeTableColumn<WaterMeter, String> Sum;

    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = new DatabaseConnection().DatabaseConnection();
        if (connection == null) {
            showAlert("Error", "Database connection failed");
            return;
        }

        // Set up cell factories for the TreeTableView
        Description.setCellValueFactory(new TreeItemPropertyValueFactory<>("meterName"));
        WaterMeterColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("waterMeter"));

        // Cell value factories for computed columns
        WaterBill.setCellValueFactory(item -> {
            double waterMeter = item.getValue().getValue().getWaterMeter();
            double waterBill = waterMeter * 1500;
            return new ReadOnlyObjectWrapper<>(String.format("%.2f Đ", waterBill));
        });

        Tax.setCellValueFactory(item -> {
            double waterMeter = item.getValue().getValue().getWaterMeter();
            double tax = waterMeter * 1500 * 0.05;
            return new ReadOnlyObjectWrapper<>(String.format("%.2f Đ", tax));
        });

        Sum.setCellValueFactory(item -> {
            double waterMeter = item.getValue().getValue().getWaterMeter();
            double waterBill = waterMeter * 1500;
            double tax = waterBill * 0.05;
            double sum = waterBill + tax;
            return new ReadOnlyObjectWrapper<>(String.format("%.2f Đ", sum));
        });

        setupChoiceBoxes();
        loadUserData();

        // Add listeners to update the TreeTableView when month or year is selected
        WaterMonth.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTreeTable());
        WaterYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTreeTable());
    }

    private void setupChoiceBoxes() {
        try {
            WaterMeterDao waterMeterDao = new WaterMeterDao(connection);

            Set<Integer> years = new TreeSet<>(waterMeterDao.getDistinctMeterYears());
            WaterYear.getItems().addAll(years.isEmpty() ? getDefaultYears() : years);

            Set<Integer> months = new TreeSet<>(waterMeterDao.getDistinctMeterMonths());
            WaterMonth.getItems().addAll(months.isEmpty() ? getDefaultMonths() : months);

        } catch (SQLException e) {
            showAlert("Error", "Failed to load choice box data: " + e.getMessage());
        }
    }

    private Set<Integer> getDefaultYears() {
        Set<Integer> defaultYears = new TreeSet<>();
        for (int i = 2000; i <= 2024; i++) {
            defaultYears.add(i);
        }
        return defaultYears;
    }

    private Set<Integer> getDefaultMonths() {
        Set<Integer> defaultMonths = new TreeSet<>();
        for (int i = 1; i <= 12; i++) {
            defaultMonths.add(i);
        }
        return defaultMonths;
    }

    private void updateTreeTable() {
    Integer selectedMonth = WaterMonth.getSelectionModel().getSelectedItem();
    Integer selectedYear = WaterYear.getSelectionModel().getSelectedItem();

    if (selectedMonth == null || selectedYear == null) {
        treeTableView.setRoot(null); // Clear the table if month or year isn't selected
        return;
    }

    try {
        WaterMeterDao waterMeterDao = new WaterMeterDao(connection);
        int userId = SessionManager.getInstance().getUserId(); // Get the user ID from SessionManager

        // Fetch unpaid water meters by user ID, month, and year
        List<WaterMeter> unpaidWaterMeters = waterMeterDao.getUnpaidWaterMetersByUserIdAndMonthAndYear(userId, selectedMonth, selectedYear);

        TreeItem<WaterMeter> root = new TreeItem<>(new WaterMeter()); // Root node doesn't need actual data

        for (WaterMeter waterMeter : unpaidWaterMeters) {
            TreeItem<WaterMeter> item = new TreeItem<>(waterMeter);
            root.getChildren().add(item);
        }

        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false); // Hide the root node

    } catch (SQLException e) {
        showAlert("Error", "Failed to update the TreeTableView: " + e.getMessage());
    }
}

    private void loadUserData() {
        try {
            int userId = SessionManager.getInstance().getUserId(); // Get user ID from SessionManager
            UserDao userDao = new UserDao(connection);
            User user = userDao.getUserById(userId);

            if (user != null) {
                id.setText(String.valueOf(user.getId()));
                name.setText(user.getName());
                address.setText(user.getAddress());
                phoneNumber.setText(user.getPhoneNumber());
                email.setText(user.getEmail());
            } else {
                showAlert("Error", "Could not retrieve user information.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load user data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

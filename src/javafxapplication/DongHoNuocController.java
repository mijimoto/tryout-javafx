/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.control.Alert.AlertType;

public class DongHoNuocController implements Initializable {

    @FXML
    private TextField MeterName;

    @FXML
    private TextField WaterMeter;

    @FXML
    private ChoiceBox<Integer> MeterMonth;

    @FXML
    private ChoiceBox<String> accountName;

    @FXML
    private ChoiceBox<Integer> MeterYear;

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private Button paid; // New Paid button

    @FXML
    private TreeTableView<WaterMeter> treeTable;

    @FXML
    private TreeTableColumn<WaterMeter, String> MeterNameCol;

    @FXML
    private TreeTableColumn<WaterMeter, Double> WaterMeterCol;

    @FXML
    private TreeTableColumn<WaterMeter, Integer> MeterMonthCol;

    @FXML
    private TreeTableColumn<WaterMeter, Integer> MeterYearCol;

    @FXML
    private TreeTableColumn<WaterMeter, String> Sum;

    @FXML
    private TreeTableColumn<WaterMeter, String> payCheckCol;

    private WaterMeterService waterMeterService;
    private UserDao userDao;
    private UserMeterDao userMeterDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = new DatabaseConnection().DatabaseConnection();
        if (connection == null) {
            showAlert("Error", "Database connection failed.");
            return;
        }

        userDao = new UserDao(connection);
    waterMeterService = new WaterMeterService(connection);
    userMeterDao = new UserMeterDao(connection);

        setupChoiceBoxes();
        populateUserNames();

        MeterNameCol.setCellValueFactory(param ->
            new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMeterName())
        );

        WaterMeterCol.setCellValueFactory(param ->
            new ReadOnlyObjectWrapper<>(param.getValue().getValue().getWaterMeter())
        );

        MeterMonthCol.setCellValueFactory(param ->
            new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMeterMonth())
        );

        MeterYearCol.setCellValueFactory(param ->
            new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMeterYear())
        );

        Sum.setCellValueFactory(param -> {
            double waterBill = param.getValue().getValue().getWaterMeter() * 1500;
            double tax = waterBill * 0.05;
            double sum = waterBill + tax;
            return new ReadOnlyObjectWrapper<>(String.format("%.2f Đ", sum));
        });

        payCheckCol.setCellValueFactory(param -> {
            WaterMeter waterMeter = param.getValue().getValue();
            UserMeter userMeter = waterMeter.getUserMeter();

            if (userMeter != null) {
                String payCheckStatus = userMeter.isPayCheck() ? "Yes" : "No"; // Display Yes or No
                return new ReadOnlyObjectWrapper<>(payCheckStatus);
            }
            return new ReadOnlyObjectWrapper<>("No");
        });

        accountName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadWaterMeterData(); // Update TreeTableView when user is selected
            }
        });

        add.setOnAction(this::addWaterMeter);
        delete.setOnAction(this::deleteWaterMeter);
        paid.setOnAction(this::togglePayCheck); // Action for Paid button
    }
    
    @FXML
    private void togglePayCheck(ActionEvent event) {
        TreeItem<WaterMeter> selectedItem = treeTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Warning", "Please select a water meter record to toggle.");
            return;
        }

        try {
            WaterMeter waterMeter = selectedItem.getValue();
            UserMeter userMeter = waterMeter.getUserMeter();
            
            if (userMeter != null) {
                boolean currentStatus = userMeter.isPayCheck();
                boolean newStatus = !currentStatus; // Toggle the status

                // Update the database
                userMeterDao.updatePayCheckStatus(waterMeter.getMeterId(), newStatus);

                // Update the TreeTableView
                userMeter.setPayCheck(newStatus);
                String payCheckStatus = newStatus ? "Yes" : "No";
                selectedItem.setValue(waterMeter); // Update the TreeTableItem

                showAlert("Success", "PayCheck status changed to " + payCheckStatus + ".");

            } else {
                showAlert("Error", "UserMeter data is missing.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to update PayCheck status: " + e.getMessage());
        }
    }

    private void setupChoiceBoxes() {
        for (int i = 1; i <= 12; i++) {
            MeterMonth.getItems().add(i);
        }
        for (int i = 2000; i <= 2024; i++) {
            MeterYear.getItems().add(i);
        }
    }

   private void populateUserNames() {
    try {
        List<User> users = userDao.getAllUsers();
        if (users != null) {
            List<String> accountNames = users.stream()
                                             .map(User::getAccountName) // Convert to String
                                             .collect(Collectors.toList());
            accountName.setItems(FXCollections.observableArrayList(accountNames));
        }
    } catch (SQLException e) {
        showAlert("Error", "Failed to load user names: " + e.getMessage());
    }
}

    private void loadWaterMeterData() {
    String selectedUser = accountName.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
        treeTable.setRoot(null);
        return;
    }

    try {
        User user = userDao.getUserByAccountName(selectedUser);
        if (user == null) {
            showAlert("Error", "User not found.");
            return;
        }

        List<WaterMeter> waterMeters = waterMeterService.getWaterMetersByUserId(user.getId());
        for (WaterMeter wm : waterMeters) {
            // Ensure the associated UserMeter is fetched and set in the WaterMeter
            UserMeter userMeter = userMeterDao.getUserMeterByMeterId(wm.getMeterId());
            wm.setUserMeter(userMeter);
        }

        TreeItem<WaterMeter> root = new TreeItem<>(new WaterMeter());
        for (WaterMeter waterMeter : waterMeters) {
            root.getChildren().add(new TreeItem<>(waterMeter));
        }

        treeTable.setRoot(root);
        treeTable.setShowRoot(false);

    } catch (SQLException e) {
        showAlert("Error", "Failed to load water meter data: " + e.getMessage());
    }
}
    @FXML
    private void addWaterMeter(ActionEvent event) {
        if (MeterName.getText().isEmpty() || WaterMeter.getText().isEmpty() ||
            MeterMonth.getValue() == null || MeterYear.getValue() == null ||
            accountName.getSelectionModel().getSelectedItem() == null) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        try {
            if (treeTable.getRoot() == null) {
                TreeItem<WaterMeter> root = new TreeItem<>(new WaterMeter());
                treeTable.setRoot(root);
                treeTable.setShowRoot(false); // Hide the root
            }

            WaterMeter newWaterMeter = new WaterMeter();
            newWaterMeter.setMeterName(MeterName.getText());
            newWaterMeter.setWaterMeter(Double.parseDouble(WaterMeter.getText()));
            newWaterMeter.setMeterMonth(MeterMonth.getValue());
            newWaterMeter.setMeterYear(MeterYear.getValue());

            String selectedAccountName = accountName.getSelectionModel().getSelectedItem();
            User user = userDao.getUserByAccountName(selectedAccountName);

            if (user == null) {
                showAlert("Error", "User not found.");
                return;
            }

            waterMeterService.addWaterMeter(newWaterMeter, user.getId());

            treeTable.getRoot().getChildren().add(new TreeItem<>(newWaterMeter));

            MeterName.clear();
            WaterMeter.clear();
            MeterMonth.setValue(null);
            MeterYear.setValue(null);

            showAlert("Success", "Water meter added successfully.");

        } catch (SQLException e) {
            showAlert("Error", "Failed to add water meter: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid water meter reading.");
        }
    }

    @FXML
    private void deleteWaterMeter(ActionEvent event) {
        TreeItem<WaterMeter> selectedItem = treeTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Warning", "Please select a record to delete.");
            return;
        }

        try {
            WaterMeter waterMeter = selectedItem.getValue();
            int meterId = waterMeter.getMeterId();

            waterMeterService.deleteWaterMeter(meterId);

            treeTable.getRoot().getChildren().remove(selectedItem);

            showAlert("Success", "Water meter deleted successfully.");

        } catch (SQLException e) {
            showAlert("Error", "Failed to delete water meter: " + e.getMessage());
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

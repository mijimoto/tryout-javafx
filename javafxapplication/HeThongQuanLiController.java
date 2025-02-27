/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.layout.AnchorPane;

public class HeThongQuanLiController implements Initializable {
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private PieChart piechart;
    @FXML
    private BarChart<String, Number> barchart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private ChoiceBox<Integer> MeterYear;
    @FXML
    private ChoiceBox<Integer> MeterMonth;

    private Connection connection;
    private WaterMeterDao waterMeterDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = new DatabaseConnection().DatabaseConnection();
            if (connection == null) {
                throw new SQLException("Database connection failed");
            }

            waterMeterDao = new WaterMeterDao(connection);

            setupChoiceBoxes(); // Initialize choice boxes

            // Get the current user ID from SessionManager
            int userId = SessionManager.getInstance().getUserId();

            // Event listeners for choice boxes
            MeterYear.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    populateBarChart(userId, newVal);
                    clearPieChart();
                }
            });

            MeterMonth.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && MeterYear.getSelectionModel().getSelectedItem() != null) {
                    int year = MeterYear.getSelectionModel().getSelectedItem();
                    populatePieChart(userId, year, newVal);
                }
            });

            setupScrollPane();
            configureBarChart(); // Improved BarChart configuration

        } catch (SQLException e) {
            showAlert("Error", "Initialization failed: " + e.getMessage());
        }
    }

    private void setupScrollPane() {
        scrollpane.setFitToWidth(true);
        scrollpane.setFitToHeight(false);
        scrollpane.setVvalue(0);
    }

    private void setupChoiceBoxes() {
        try {
            Set<Integer> years = waterMeterDao.getDistinctMeterYears();
            MeterYear.getItems().addAll(years);

            Set<Integer> months = waterMeterDao.getDistinctMeterMonths();
            MeterMonth.getItems().addAll(months);

        } catch (SQLException e) {
            showAlert("Error", "Failed to load choice box data: " + e.getMessage());
        }
    }

    private void populatePieChart(int userId, int year, int month) {
        try {
            piechart.getData().clear();

            List<WaterMeter> waterMeters = waterMeterDao.getWaterMetersByUserId(userId);

            for (WaterMeter waterMeter : waterMeters) {
                if (waterMeter.getMeterYear() == year && waterMeter.getMeterMonth() == month) {
                    String label = String.format("%s: %.2f", waterMeter.getMeterName(), waterMeter.getWaterMeter());
                    PieChart.Data data = new PieChart.Data(label, waterMeter.getWaterMeter());
                    piechart.getData().add(data);
                }
            }

        } catch (SQLException e) {
            showAlert("Error", "Failed to load data into PieChart: " + e.getMessage());
        }
    }

    private void populateBarChart(int userId, int year) {
        try {
            barchart.getData().clear(); 

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("WaterMeter Values");

            for (int month = 1; month <= 12; month++) {
                List<WaterMeter> waterMeters = waterMeterDao.getWaterMetersByUserId(userId);

                double sum = 0;

                for (WaterMeter wm : waterMeters) {
                    if (wm.getMeterYear() == year && wm.getMeterMonth() == month) {
                        sum += wm.getWaterMeter();
                    }
                }

                if (sum >= 0) {
                    series.getData().add(new XYChart.Data<>(String.valueOf(month), sum));
                }
            }

            barchart.getData().add(series);

        } catch (SQLException e) {
            showAlert("Error", "Failed to load data into BarChart: " + e.getMessage());
        }
    }

    private void configureBarChart() {
        xAxis.setTickLabelRotation(60); // Rotate x-axis labels
        xAxis.setTickLabelGap(10); // Add spacing between labels
        barchart.setPrefWidth(1000); // Extend chart width to reduce crowding
    }

    private void clearPieChart() {
        piechart.getData().clear(); 
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

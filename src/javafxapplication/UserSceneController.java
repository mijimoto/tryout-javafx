/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class UserSceneController {

    @FXML
    private BorderPane MainBorderPane; // Reference to the BorderPane in your FXML

    @FXML
    private Button UserHome;

    @FXML
    private Button HoaDonNuoc;

    @FXML
    private Button DongHoNuoc;

    @FXML
    private Button HeThongQuanLi;

    @FXML
    private Button CaiDat;

    @FXML
    private Button NguoiDung;

    @FXML
    public void initialize() {
       loadContent("UserHome.fxml");
        // Bind event handlers to buttons
        UserHome.setOnAction(e -> loadContent("UserHome.fxml"));
        HoaDonNuoc.setOnAction(e -> loadContent("HoaDonNuoc.fxml"));
        HeThongQuanLi.setOnAction(e -> loadContent("HeThongQuanLi.fxml"));
        CaiDat.setOnAction(e -> loadContent("CaiDat.fxml"));
        NguoiDung.setOnAction(e -> loadContent("NguoiDung.fxml"));
    }

    private void loadContent(String fxmlFile) {
        try {
            // Load the new FXML content
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();

            // Set the content in the center of the BorderPane
            MainBorderPane.setCenter(content);

        } catch (IOException ex) {
            ex.printStackTrace(); // Handle the exception as needed
        }
    }
}

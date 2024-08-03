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

public class AdminSceneController {

    @FXML
    private Button AdminHome;

    @FXML
    private Button CaiDat;

    @FXML
    private BorderPane MainBorderPane;

    @FXML
    private Button DongHoNuoc;
      
    @FXML
    private Button NguoiDung;

    @FXML
    private Button QuanLi;
    @FXML
    public void initialize() {
        loadContent("AdminHome.fxml");
        // Bind event handlers to buttons
        DongHoNuoc.setOnAction(e -> loadContent("DongHoNuoc.fxml"));
        AdminHome.setOnAction(e -> loadContent("AdminHome.fxml"));
        QuanLi.setOnAction(e -> loadContent("QuanLi.fxml"));
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


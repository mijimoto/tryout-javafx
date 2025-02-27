/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication;



import static com.gluonhq.charm.glisten.application.AppManager.initialize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DangNhap.fxml"));
        Parent root = loader.load();

        // Create a new Scene with the loaded root
        Scene scene = new Scene(root);// Adjust width and height to match your layout

       

        // Set the primary stage's title, scene, and other properties
        primaryStage.setTitle("Water Management System");
        primaryStage.setScene(scene);
        
        // Display the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}

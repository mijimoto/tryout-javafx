/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserHomeController implements Initializable {
    @FXML
    private TextField notification;
    
    @FXML
    private ImageView webimage1;

    @FXML
    private ImageView webimage2;

    @FXML
    private ImageView webimage3;
    
    private WaterMeterDao waterMeterDao;
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
           try {
            connection = new DatabaseConnection().DatabaseConnection();
            if (connection == null) {
                throw new SQLException("Database connection failed");
            }

            waterMeterDao = new WaterMeterDao(connection);}
           catch (SQLException e) {
            e.printStackTrace();
        }
        setUnpaidWaterMeterNotification();
        // Initialize click handlers
        webimage1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openWebpage("https://www.bushmantanks.com.au/blog/tips-to-save-water-at-home/");
        });

        webimage2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openWebpage("https://pmmag.com/articles/103758-matt-michel-18-ways-to-recruit-plumbers");
        });

        webimage3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openWebpage("https://asmaindustrial.com/brass-screwed-water-meter/");
        });

        // Hide all images initially
        webimage1.setOpacity(0);
        webimage2.setOpacity(0);
        webimage3.setOpacity(0);

        // Timeline to rotate between images
        Timeline timeline = new Timeline();
        double fadeDuration = 4.0; // seconds
        double visibleDuration = 4.0; // seconds

        // Fade-in and out for webimage1
        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(0),
                event -> {
                    webimage2.setVisible(false);
                     webimage3.setVisible(false);
                    webimage1.setVisible(true);
                    
                    webimage1.setOpacity(1); // Set initial opacity
                },
                new KeyValue(webimage1.opacityProperty(), 1) // Fade-in
        ));

        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(fadeDuration), // Start of fade-out
                new KeyValue(webimage1.opacityProperty(), 0) // Fade-out
        ));

        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(fadeDuration + visibleDuration), // Fade-in for webimage2
                event -> {
                    webimage1.setVisible(false);
                    webimage3.setVisible(false);
                    webimage2.setVisible(true);
                    webimage2.setOpacity(1); // Set initial opacity
                },
                new KeyValue(webimage2.opacityProperty(), 1) // Fade-in
        ));

        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(2 * fadeDuration + visibleDuration), // Start of fade-out for webimage2
                new KeyValue(webimage2.opacityProperty(), 0) // Fade-out
        ));

        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(2 * fadeDuration + 2 * visibleDuration), // Fade-in for webimage3
                event -> {
                     webimage1.setVisible(false);
                    webimage2.setVisible(false);
                    webimage3.setVisible(true);
                    webimage3.setOpacity(1); // Set initial opacity
                },
                new KeyValue(webimage3.opacityProperty(), 1) // Fade-in
        ));

        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(3 * fadeDuration + 2 * visibleDuration), // Start of fade-out for webimage3
                new KeyValue(webimage3.opacityProperty(), 0) // Fade-out
        ));

        timeline.setCycleCount(Timeline.INDEFINITE); // Loop the timeline
        timeline.play(); // Start the timeline
    }

    private void openWebpage(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void setUnpaidWaterMeterNotification() {
        try {
            int userId = SessionManager.getInstance().getUserId(); // Get user ID
            List<WaterMeter> unpaidWaterMeters = waterMeterDao.getUnpaidWaterMetersByUserId(userId); // Get unpaid meters

            if (unpaidWaterMeters.isEmpty()) {
                notification.setVisible(false); // Hide the notification if no unpaid meters
            } else {
                int unpaidCount = unpaidWaterMeters.size(); // Get count
                String message = String.format("You have %d unpaid WaterMeters", unpaidCount); // Format the message
                notification.setText(message); // Set the message
                notification.setVisible(true); // Ensure the notification is visible
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

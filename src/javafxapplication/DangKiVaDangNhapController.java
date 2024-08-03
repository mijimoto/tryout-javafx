package javafxapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javax.swing.JOptionPane;

public class DangKiVaDangNhapController implements Initializable {
    // Fields
    @FXML
    private Scene scene;
    @FXML
    private Stage stage;
    @FXML
    private Parent root;
    @FXML
    private AnchorPane rootpane;
    @FXML
    private TextField accountName;
    @FXML
    private PasswordField password;
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
    private Button signupbutton;
    @FXML
    private Button signinbutton;
    @FXML
    private Button loadsignupbutton;
    @FXML
    private Button loadsigninbutton;

    private UserDao userDao; // Data Access Object
    private Connection connection;
    
    
     @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get a database connection
       connection = new DatabaseConnection().DatabaseConnection();
        if (connection != null) {
            userDao = new UserDao(connection); // Initialize UserDao with a valid connection
        } else {
            System.err.println("Error: Database connection is null.");
        }
    }
    
    public DangKiVaDangNhapController() {
        // Default constructor for FXML
    }
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao; // Dependency injection method
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection; // Dependency injection for Connection
    }

    public DangKiVaDangNhapController(Connection connection) {
        this(); // Call the default constructor
        this.userDao = new UserDao(connection); // Initialize UserDao with connection
    }

    @FXML
    void signup(ActionEvent event) {
        try {
            User newUser = new User();
            newUser.setAccountName(accountName.getText());
            newUser.setPassword(password.getText());
            newUser.setName(name.getText());
            newUser.setAge(Integer.parseInt(age.getText()));
            newUser.setAddress(address.getText());
            newUser.setPhoneNumber(phoneNumber.getText());
            newUser.setEmail(email.getText());

            // Validate inputs
            if (newUser.getAccountName().isEmpty() || newUser.getPassword().isEmpty() ||
                newUser.getName().isEmpty() || newUser.getAge() == 0 ||
                newUser.getAddress().isEmpty() || newUser.getPhoneNumber().isEmpty() ||
                newUser.getEmail().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Sign Up failed: All fields must be filled.");
            } else {
                userDao.addUser(newUser);
                JOptionPane.showMessageDialog(null, "Sign Up successful.");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Sign Up failed: " + e.getMessage());
        }
    }

  @FXML
void signin(ActionEvent event) {
    if (userDao == null) {
        JOptionPane.showMessageDialog(null, "Error: UserDao not initialized.");
        return;
    }

    try {
        String enteredAccountName = accountName.getText();
        String enteredPassword = password.getText();

        User user = userDao.getUser(enteredAccountName, enteredPassword);

        if (user != null) {
            JOptionPane.showMessageDialog(null, "Sign-In successful.");

            // Store the user ID in the SessionManager
            SessionManager.getInstance().setUserId(user.getId());

            String fxmlFile;
            if ("admin".equalsIgnoreCase(user.getAccountName())) {
                fxmlFile = "AdminScene.fxml";
            } else {
                fxmlFile = "UserScene.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (stage == null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            }

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            JOptionPane.showMessageDialog(null, "Sign-In failed: Invalid username or password.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Sign-In failed: " + e.getMessage());
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error loading scene: " + e.getMessage());
    }
}

    @FXML
    void loadsignin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DangNhap.fxml"));
            Parent root = loader.load(); // Ensure proper loading
            if (stage == null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Initialize stage
            }
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading Sign-In scene: " + e.getMessage());
        }
    }

@FXML
void loadsignup(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DangKi.fxml"));
        Parent root = loader.load(); // Ensure proper loading

        // Ensure the new controller has the correct data
        DangKiVaDangNhapController controller = loader.getController();
        controller.setUserDao(this.userDao); // Pass the UserDao instance
        controller.setConnection(this.connection); // Pass the connection

        if (stage == null) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current stage
        }

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error loading Sign-Up scene: " + e.getMessage());
    }
}

}

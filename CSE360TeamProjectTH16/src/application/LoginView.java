package application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class LoginView {
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Hyperlink createAccountLink;
    private Label errorLabel;

    public LoginView() {
        // Initialize UI components
    	Image image = new Image("logo.png");
    	ImageView icon = new ImageView(image);
    	icon.setFitWidth(150);
        icon.setFitHeight(150);
		
        Text title = new Text("Health");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 45));
        title.setFill(Color.DARKOLIVEGREEN);
        Text title1 = new Text("Nest");
        title1.setFont(Font.font("Georgia", FontWeight.BOLD, 50));
        title1.setFill(Color.GREENYELLOW);
        HBox container = new HBox(title, title1);
        

        Label usernameLabel = new Label("HealthNest ID:");
        usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        loginButton = new Button("Login");
        loginButton.setDisable(true);

        createAccountLink = new Hyperlink("New to HealthNest? Create your account here.");

        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        // Set up event handlers
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateFields();
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateFields();
        });
        loginButton.setOnAction(e -> handleLogin());
        createAccountLink.setOnAction(e -> handleCreateAccount());

        // Arrange components in a grid layout
        GridPane grid = new GridPane();
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(0));

        vbox.getChildren().addAll(icon, container);
        
        grid.add(vbox, 0, 0);
        GridPane.setHalignment(vbox, HPos.CENTER);
        
        grid.add(usernameLabel, 0, 3);
        grid.add(usernameField, 1, 3);
        grid.add(passwordLabel, 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(loginButton, 1, 5);
        grid.add(createAccountLink, 0, 6);
        grid.add(errorLabel, 0, 7);

        view = new VBox(grid);
    }

    private void validateFields() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isValid = !username.isEmpty() && !password.isEmpty();
        loginButton.setDisable(!isValid);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Perform authentication logic
        User authenticatedUser = authenticatedUser(username, password);
        
        if (authenticatedUser != null) {
        	// Navigate to the appropriate view based on user role
            String userRole = authenticatedUser.getRole();
            switch (userRole) {
                case "Doctor":
                    // Navigate to DoctorView
                    navigateToDoctorView(authenticatedUser);
                    break;
                case "Nurse":
                    // Navigate to NurseView
                    navigateToNurseView(authenticatedUser);
                    break;
                case "Patient":
                    // Navigate to PatientView
                    navigateToPatientView(authenticatedUser);
                    break;
                default:
                    // Handle unknown role
                    System.out.println("Unknown user role: " + userRole);
                    break;
            }
        } else {
            errorLabel.setText("Incorrect HealthNest ID or password.");
            errorLabel.setVisible(true);
        }
    }

    private User authenticatedUser(String username, String password) {
        // Read user credentials from the file
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 7) {
                    String storedUsername = userData[4];
                    String storedPassword = userData[5];
                    
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    	// Create a User object with the authenticated user's details
                        String firstName = userData[0];
                        String lastName = userData[1];
                        LocalDate dob = LocalDate.parse(userData[2]);
                        String phone = userData[3];
                        String email = userData[4];
                        String role = userData[6];
                        return new User(firstName, lastName, dob, phone, email, password, role);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file read fails
            AlertDialog.showErrorDialog("Loading Error", "Load Users Failed", "Unable to load users.");
        }
        return null;
    }
    
    private void navigateToDoctorView(User user) {
        // Create an instance of DoctorView and navigate to it
        DoctorView doctorView = new DoctorView(user);
        Scene scene = new Scene(doctorView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    private void navigateToNurseView(User user) {
        // Create an instance of NurseView and navigate to it
    	NurseView nurseView = new NurseView(user);
        Scene scene = new Scene(nurseView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    private void navigateToPatientView(User user) {
        // Create an instance of PatientView and navigate to it
    	PatientView patientView = new PatientView(user);
    	Scene scene = new Scene(patientView.getView(), 800, 600);
    	Stage stage = (Stage) view.getScene().getWindow();
    	stage.setScene(scene);
    }

    private void handleCreateAccount() {
        // Navigate to the account creation view
        CreateAccountView createAccountView = new CreateAccountView();
        Scene scene = new Scene(createAccountView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    public VBox getView() {
        return view;
    }
}
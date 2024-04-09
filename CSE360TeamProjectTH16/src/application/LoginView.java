package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginView {
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Hyperlink createAccountLink;
    private Label errorLabel;

    public LoginView() {
        // Initialize UI components
        Text title = new Text("HealthNest");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

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
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);
        grid.add(createAccountLink, 1, 4);
        grid.add(errorLabel, 1, 5);

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
        // If successful, load the appropriate view based on user role
        if (authenticateUser(username, password)) {
            // Load doctor, nurse, or patient view based on user role
            // You can use a separate method or class to handle the navigation
            // For example: NavigationManager.loadView(userRole);
        } else {
            errorLabel.setText("Incorrect HealthNest ID or password.");
            errorLabel.setVisible(true);
        }
    }

    private boolean authenticateUser(String username, String password) {
        // Implement authentication logic here
        // You can use a database or a service to validate the credentials
        // For simplicity, let's assume authentication is successful if username and password are not empty
        return !username.isEmpty() && !password.isEmpty();
    }

    private void handleCreateAccount() {
        // Navigate to the account creation view
        // You can use a separate method or class to handle the navigation
        // For example: NavigationManager.loadView("CreateAccountView");
    }

    public VBox getView() {
        return view;
    }
}
package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class CreateAccountView {
    private VBox view;
    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dobPicker;
    private TextField phoneField;
    private TextField emailField;
    private PasswordField passwordField;
    private RadioButton doctorRadioButton;
    private RadioButton nurseRadioButton;
    private RadioButton patientRadioButton;
    private Button createAccountButton;

    public CreateAccountView() {
        // Initialize UI components
        Label title = new Label("Create Your Account");

        Label firstNameLabel = new Label("First Name:");
        firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        lastNameField = new TextField();

        Label dobLabel = new Label("Date of Birth:");
        dobPicker = new DatePicker();

        Label phoneLabel = new Label("Phone Number:");
        phoneField = new TextField();

        Label emailLabel = new Label("Email Address:");
        emailField = new TextField();
        
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        
        Label roleLabel = new Label("Role:");
        ToggleGroup roleToggleGroup = new ToggleGroup();

        doctorRadioButton = new RadioButton("Doctor");
        doctorRadioButton.setToggleGroup(roleToggleGroup);

        nurseRadioButton = new RadioButton("Nurse");
        nurseRadioButton.setToggleGroup(roleToggleGroup);

        patientRadioButton = new RadioButton("Patient");
        patientRadioButton.setToggleGroup(roleToggleGroup);
        patientRadioButton.setSelected(true);

        createAccountButton = new Button("Sign Up!");
        createAccountButton.setOnAction(e -> handleCreateAccount());

        // Arrange components in a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(dobLabel, 0, 3);
        grid.add(dobPicker, 1, 3);
        grid.add(phoneLabel, 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(emailLabel, 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(passwordLabel, 0, 6);
        grid.add(passwordField, 1, 6);
        grid.add(roleLabel, 0, 7);
        grid.add(doctorRadioButton, 1, 7);
        grid.add(nurseRadioButton, 1, 8);
        grid.add(patientRadioButton, 1, 9);
        grid.add(createAccountButton, 1, 10);

        view = new VBox(grid);
    }

    private void handleCreateAccount() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        LocalDate dob = dobPicker.getValue();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        
        String role = "";
        if (doctorRadioButton.isSelected()) {
            role = "Doctor";
        } else if (nurseRadioButton.isSelected()) {
            role = "Nurse";
        } else if (patientRadioButton.isSelected()) {
            role = "Patient";
        }
        

        // Perform validation
        if (firstName.isEmpty() || lastName.isEmpty() || dob == null || phone.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            // Display an error message if any field is empty
            // You can use an Alert or a Label to show the message
        	AlertDialog.showErrorDialog("Creating Error", "Creating Failed", "All fields must have values.");
            return;
        }

        // Create a User object with the entered details
        User newUser = new User(firstName, lastName, dob, phone, email, password, role);

        // Save the user details to a file
        saveUserToFile(newUser);

        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void saveUserToFile(User user) {
        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write(user.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file write fails
            AlertDialog.showErrorDialog("Creating Error", "Creating User Failed", "Unable to create a new user.");
        }
    }

    public VBox getView() {
        return view;
    }
}
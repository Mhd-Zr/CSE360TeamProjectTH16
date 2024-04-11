package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DoctorView {
    private VBox view;
    private User currentUser;

    public DoctorView(User user) {
        currentUser = user;
        
        // Initialize UI components
        Label titleLabel = new Label("Doctor Portal");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Consistent font styling with LoginView
        
        Button messagesButton = new Button("Messages");
        messagesButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14)); // Apply font styling to buttons
        messagesButton.setOnAction(e -> handleMessagesButton());
        
        Button patientRecordsButton = new Button("Patient Records");
        patientRecordsButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14)); // Apply font styling to buttons
        patientRecordsButton.setOnAction(e -> handlePatientRecordsButton());
        
        Button prescriptionsButton = new Button("Medical Prescriptions");
        prescriptionsButton.setOnAction(e -> handlePrescriptionsButton());

        Button logoutButton = new Button("Logout");
        logoutButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14)); // Apply font styling to buttons
        logoutButton.setOnAction(e -> handleLogoutButton());
        
        // Arrange components in a VBox layout for consistency
        view = new VBox(10);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(titleLabel, messagesButton, patientRecordsButton, prescriptionsButton, logoutButton);
    }
    
    private void handleMessagesButton() {
        navigateToView(new MessagingView(currentUser));
    }

    private void handlePatientRecordsButton() {
        navigateToView(new PatientRecordsView(currentUser));
    }
    
    private void handlePrescriptionsButton() {
        PrescriptionView prescriptionView = new PrescriptionView(currentUser);
        Scene scene = new Scene(prescriptionView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void handleLogoutButton() {
        // Navigate back to the login view using a utility method for scene switching
        navigateToView(new LoginView());
    }
    
    // Utility method to switch views to reduce code duplication
    private void navigateToView(Object view) {
        Scene scene = new Scene(((HasView) view).getView(), 800, 600);
        Stage stage = (Stage) this.view.getScene().getWindow();
        stage.setScene(scene);
    }
    
    public VBox getView() {
        return view;
    }
    
    // An interface that any view passed to navigateToView should implement
    interface HasView {
        VBox getView();
    }
}

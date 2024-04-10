package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoctorView {
    private VBox view;
    private User currentUser;

    public DoctorView(User user) {
    	currentUser = user;
    	
        // Initialize UI components
        Label titleLabel = new Label("Doctor Portal");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button messagesButton = new Button("Messages");
        messagesButton.setOnAction(e -> handleMessagesButton());

        Button patientRecordsButton = new Button("Patient Records");
        patientRecordsButton.setOnAction(e -> handlePatientRecordsButton());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogoutButton());

        // Arrange components in a VBox layout
        view = new VBox(10);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(titleLabel, messagesButton, patientRecordsButton, logoutButton);
    }

    private void handleMessagesButton() {
    	MessagingView messagingView = new MessagingView(currentUser);
        Scene scene = new Scene(messagingView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void handlePatientRecordsButton() {
    	PatientRecordsView patientRecordsView = new PatientRecordsView(currentUser);
        Scene scene = new Scene(patientRecordsView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void handleLogoutButton() {
        // Navigate back to the login view
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    public VBox getView() {
        return view;
    }
}
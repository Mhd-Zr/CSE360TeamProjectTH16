package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientView {
    private VBox view;
    private User currentPatient;

    public PatientView(User user) {
    	currentPatient = user;

        // Initialize UI components
        Label titleLabel = new Label("Patient Portal");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label welcomeLabel = new Label("Welcome, " + currentPatient.getFirstName() + " " + currentPatient.getLastName());
        welcomeLabel.setStyle("-fx-font-size: 18px;");

        Button patientRecordsButton = new Button("Personal Records");
        patientRecordsButton.setOnAction(e -> handlePatientRecordsButton());
        
        Button messagesButton = new Button("Messages");
        messagesButton.setOnAction(e -> handleMessagesButton());
        
        Button prescriptionsButton = new Button("Medical Prescriptions");
        prescriptionsButton.setOnAction(e -> handlePrescriptionsButton());
        
        Button appointmentsButton = new Button("Appointments");
        appointmentsButton.setOnAction(e -> handleAppointmentsButton());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogoutButton());

        // Arrange components in a VBox layout
        view = new VBox(10);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(titleLabel, welcomeLabel, patientRecordsButton, messagesButton, prescriptionsButton,
                appointmentsButton, logoutButton);
    }

    private void handleMessagesButton() {
    	MessagingView messagingView = new MessagingView(currentPatient);
        Scene scene = new Scene(messagingView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void handlePatientRecordsButton() {
        // TODO: Implement the patient records functionality
        System.out.println("Patient Records button clicked");
    }
    
    private void handlePrescriptionsButton() {
        // TODO: Implement the prescriptions functionality
        System.out.println("Medical Prescriptions button clicked");
    }
    
    private void handleAppointmentsButton() {
        AppointmentSchedulingView appointmentSchedulingView = new AppointmentSchedulingView(currentPatient);
        Scene scene = new Scene(appointmentSchedulingView.getView(), 800, 600);
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
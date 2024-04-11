package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NurseView {
    private VBox view;
    private User currentUser;

    public NurseView(User user) {
        this.currentUser = user;
        
        // Initialize UI components
        Label titleLabel = new Label("Nurse Portal");
        // Remove inline style and use CSS class
        titleLabel.getStyleClass().add("title-label");

        Button messagesButton = new Button("Messages");
        messagesButton.getStyleClass().add("button");
        messagesButton.setOnAction(e -> handleMessagesButton());

        Button patientQuestionnaireButton = new Button("Patient Questionnaire");
        patientQuestionnaireButton.getStyleClass().add("button");
        patientQuestionnaireButton.setOnAction(e -> handlePatientQuestionnaireButton());

        Button patientRecordsButton = new Button("Patient Records");
        patientRecordsButton.getStyleClass().add("button");
        patientRecordsButton.setOnAction(e -> handlePatientRecordsButton());

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button");
        logoutButton.setOnAction(e -> handleLogoutButton());

        // Arrange components in a VBox layout
        view = new VBox(10);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(titleLabel, messagesButton, patientQuestionnaireButton, patientRecordsButton, logoutButton);
    }

    private void handleMessagesButton() {
    	MessagingView messagingView = new MessagingView(currentUser);
        Scene scene = new Scene(messagingView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    private void handlePatientQuestionnaireButton() {
    	PatientQuestionnaireView questionnaireView = new PatientQuestionnaireView(currentUser);
        Scene scene = new Scene(questionnaireView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }
    
    private void handlePatientRecordsButton() {
        PatientRecordsView patientRecordsView = new PatientRecordsView(currentUser);
        Scene scene = new Scene(patientRecordsView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    private void handleLogoutButton() {
        // Navigate back to the login view
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    public VBox getView() {
        return view;
    }
}

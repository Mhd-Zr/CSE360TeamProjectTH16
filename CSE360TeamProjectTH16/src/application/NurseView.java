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

    public NurseView() {
        // Initialize UI components
        Label titleLabel = new Label("Nurse Portal");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button messagesButton = new Button("Messages");
        messagesButton.setOnAction(e -> handleMessagesButton());

        Button patientQuestionnaireButton = new Button("Patient Questionnaire");
        patientQuestionnaireButton.setOnAction(e -> handlePatientQuestionnaireButton());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogoutButton());

        // Arrange components in a VBox layout
        view = new VBox(10);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(titleLabel, messagesButton, patientQuestionnaireButton, logoutButton);
    }

    private void handleMessagesButton() {
        // TODO: Implement the messaging functionality
        System.out.println("Messages button clicked");
    }

    private void handlePatientQuestionnaireButton() {
        // TODO: Implement the patient questionnaire functionality
        System.out.println("Patient Questionnaire button clicked");
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
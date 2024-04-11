package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class PatientQuestionnaireView {
    private VBox view;
    private TextField patientNameField;
    private TextField patientAgeField;
    private TextArea allergiesField;
    private TextArea concernsField;
    private User currentUser;

    public PatientQuestionnaireView(User user) {
        currentUser = user;

        // Initialize UI components
        Label titleLabel = new Label("Patient Questionnaire");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label patientNameLabel = new Label("Patient Name:");
        patientNameField = new TextField();

        Label patientAgeLabel = new Label("Patient Age:");
        patientAgeField = new TextField();

        Label allergiesLabel = new Label("Known Allergies:");
        allergiesField = new TextArea();

        Label concernsLabel = new Label("Health Concerns:");
        concernsField = new TextArea();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmitButton());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a grid layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(patientNameLabel, 0, 0);
        gridPane.add(patientNameField, 1, 0);
        gridPane.add(patientAgeLabel, 0, 1);
        gridPane.add(patientAgeField, 1, 1);
        gridPane.add(allergiesLabel, 0, 2);
        gridPane.add(allergiesField, 1, 2);
        gridPane.add(concernsLabel, 0, 3);
        gridPane.add(concernsField, 1, 3);
        gridPane.add(submitButton, 1, 4);
        gridPane.add(backButton, 0, 4);

        view = new VBox(10);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(titleLabel, gridPane);
    }

    private void handleSubmitButton() {
        String patientName = patientNameField.getText();
        String patientAge = patientAgeField.getText();
        String allergies = allergiesField.getText();
        String concerns = concernsField.getText();

        // Save the questionnaire data to a file
        saveQuestionnaireData(patientName, patientAge, allergies, concerns);

        // Clear the input fields
        patientNameField.clear();
        patientAgeField.clear();
        allergiesField.clear();
        concernsField.clear();
    }

    private void handleBackButton() {
        // Navigate back to the nurse view
        NurseView nurseView = new NurseView(currentUser);
        Scene scene = new Scene(nurseView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    private void saveQuestionnaireData(String patientName, String patientAge, String allergies, String concerns) {
        try (FileWriter writer = new FileWriter("questionnaire_data.txt", true)) {
            writer.write("Patient Name: " + patientName + "\n");
            writer.write("Patient Age: " + patientAge + "\n");
            writer.write("Known Allergies: " + allergies + "\n");
            writer.write("Health Concerns: " + concerns + "\n");
            writer.write("Submitted By: " + currentUser.getEmail() + "\n");
            writer.write("-----------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file write fails
            AlertDialog.showErrorDialog("Saving Error", "Saving Questionnaire Failed", "Unable to save quiestionnaire.");
        }
    }

    public VBox getView() {
        return view;
    }
}
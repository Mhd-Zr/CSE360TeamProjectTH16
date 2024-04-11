package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OnePatientRecordsView {
    private BorderPane view;
    private TextArea recordsArea;
    private User currentPatient;

    public OnePatientRecordsView(User user) {
        currentPatient = user;

        // Initialize UI components
        Label titleLabel = new Label("My Records");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        recordsArea = new TextArea();
        recordsArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a BorderPane layout
        VBox recordsBox = new VBox(10);
        recordsBox.getChildren().addAll(new Label("Records:"), recordsArea);

        view = new BorderPane();
        view.setTop(titleLabel);
        view.setCenter(recordsBox);
        view.setBottom(backButton);
        view.setPadding(new Insets(20));
    }

    private void handleBackButton() {
        PatientView patientView = new PatientView(currentPatient);
        Scene scene = new Scene(patientView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private boolean loadPatientRecords() {
        String patientFile = "patient_records/" + currentPatient.getFirstName() + " " + currentPatient.getLastName() + ".txt";
        File file = new File(patientFile);

        if (file.exists()) {
            try {
                String records = new String(Files.readAllBytes(Paths.get(patientFile)));
                recordsArea.setText(records);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                AlertDialog.showErrorDialog("Error", "Failed to Load Records", "Failed to load patient records. Please try again.");
            }
        } else {
            AlertDialog.showErrorDialog("Error", "Records Not Found", "No records found for the patient.");
        }

        return false;
    }

    public BorderPane getView() {
        boolean recordsLoaded = loadPatientRecords();
        if (!recordsLoaded) {
            handleBackButton();
        }
        return view;
    }
}
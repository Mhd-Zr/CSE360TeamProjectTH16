package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PatientPrescriptionView {
    private BorderPane view;
    private ListView<String> prescriptionList;
    private User currentPatient;

    public PatientPrescriptionView(User patient) {
        currentPatient = patient;

        // Initialize UI components
        Label titleLabel = new Label("My Prescriptions");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        prescriptionList = new ListView<>();
        prescriptionList.setOnMouseClicked(e -> handlePrescriptionClick());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a BorderPane layout
        VBox listBox = new VBox(10);
        listBox.getChildren().addAll(new Label("Prescriptions:"), prescriptionList);

        view = new BorderPane();
        view.setTop(titleLabel);
        view.setCenter(listBox);
        view.setBottom(backButton);
        view.setPadding(new Insets(20));
    }

    private void handlePrescriptionClick() {
        String selectedPrescription = prescriptionList.getSelectionModel().getSelectedItem();
        if (selectedPrescription != null) {
            AlertDialog.showInformationDialog("Prescription Details", "Selected Prescription", selectedPrescription);
        }
    }

    private void handleBackButton() {
        PatientView patientView = new PatientView(currentPatient);
        Scene scene = new Scene(patientView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void loadPrescriptions() {
        prescriptionList.getItems().clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get("prescriptions.txt"));
            StringBuilder prescriptionBuilder = new StringBuilder();
            boolean isPrescriptionFound = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("Patient:")) {
                    String patientName = line.substring(line.indexOf(":") + 2);
                    if (patientName.equals(currentPatient.getFirstName() + " " + currentPatient.getLastName())) {
                        isPrescriptionFound = true;
                        prescriptionBuilder.append(line).append("\n");
                    } else {
                        if (isPrescriptionFound) {
                            prescriptionList.getItems().add(prescriptionBuilder.toString());
                            prescriptionBuilder.setLength(0);
                            isPrescriptionFound = false;
                        }
                    }
                } else if (isPrescriptionFound) {
                    prescriptionBuilder.append(line).append("\n");
                }

                if (i == lines.size() - 1 && isPrescriptionFound) {
                    prescriptionList.getItems().add(prescriptionBuilder.toString());
                }
            }

            if (prescriptionList.getItems().isEmpty()) {
                prescriptionList.setPlaceholder(new Label("No prescriptions found for the current patient."));


            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showErrorDialog("Error", "Failed to Load Prescriptions", "Failed to load prescriptions. Please try again.");
        }
    }


    public BorderPane getView() {
        loadPrescriptions();
        return view;
    }
}
package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PrescriptionView {
    private BorderPane view;
    private TextField patientNameField;
    private TextArea prescriptionField;
    private ListView<String> prescriptionList;
    private User currentDoctor;

    public PrescriptionView(User doctor) {
        currentDoctor = doctor;

        // Initialize UI components
        Label titleLabel = new Label("Prescription Management");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label patientNameLabel = new Label("Patient Name:");
        patientNameField = new TextField();

        Label prescriptionLabel = new Label("Prescription:");
        prescriptionField = new TextArea();

        Button addButton = new Button("Add Prescription");
        addButton.setOnAction(e -> handleAddPrescription());

        prescriptionList = new ListView<>();
        prescriptionList.setOnMouseClicked(e -> handlePrescriptionClick());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a BorderPane layout
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(patientNameLabel, 0, 0);
        inputGrid.add(patientNameField, 1, 0);
        inputGrid.add(prescriptionLabel, 0, 1);
        inputGrid.add(prescriptionField, 1, 1);
        inputGrid.add(addButton, 1, 2);

        VBox listBox = new VBox(10);
        listBox.getChildren().addAll(new Label("Prescriptions:"), prescriptionList);

        view = new BorderPane();
        view.setTop(titleLabel);
        view.setLeft(inputGrid);
        view.setCenter(listBox);
        view.setBottom(backButton);
        view.setPadding(new Insets(20));
    }

    private void handleAddPrescription() {

        String patientName = patientNameField.getText();

        String prescription = prescriptionField.getText();



        if (!patientName.isEmpty() && !prescription.isEmpty()) {

            String prescriptionEntry = "Doctor: " + currentDoctor.getFirstName() + " " + currentDoctor.getLastName() +

                    "\nPatient: " + patientName +

                    "\nPrescription: " + prescription +

                    "\n-----------------------------";



            try (FileWriter writer = new FileWriter("prescriptions.txt", true)) {

                writer.write(prescriptionEntry + "\n");

                AlertDialog.showInformationDialog("Prescription Added", "Success", "Prescription added successfully.");

                clearFields();

                prescriptionList.getItems().add(prescriptionEntry); // Add the new prescription entry to the list view

            } catch (IOException e) {

                e.printStackTrace();

                AlertDialog.showErrorDialog("Error", "Prescription Addition Failed", "Failed to add prescription. Please try again.");

            }

        } else {

            AlertDialog.showWarningDialog("Warning", "Incomplete Information", "Please enter both patient name and prescription.");

        }

    }


    private void handlePrescriptionClick() {
        String selectedPrescription = prescriptionList.getSelectionModel().getSelectedItem();
        if (selectedPrescription != null) {
            AlertDialog.showInformationDialog("Prescription Details", "Selected Prescription", selectedPrescription);
        }
    }

    private void handleBackButton() {
        DoctorView doctorView = new DoctorView(currentDoctor);
        Scene scene = new Scene(doctorView.getView(), 800, 600);
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

                if (line.startsWith("Doctor:")) {

                    String doctorName = line.substring(line.indexOf(":") + 2);

                    if (doctorName.equals(currentDoctor.getFirstName() + " " + currentDoctor.getLastName())) {


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

            }



            if (isPrescriptionFound) {

                prescriptionList.getItems().add(prescriptionBuilder.toString());

            }

        } catch (IOException e) {

            e.printStackTrace();

            AlertDialog.showErrorDialog("Error", "Failed to Load Prescriptions", "Failed to load prescriptions. Please try again.");

        }

    }
    private void clearFields() {
        patientNameField.clear();
        prescriptionField.clear();
    }

    public BorderPane getView() {
        loadPrescriptions();
        return view;
    }
}
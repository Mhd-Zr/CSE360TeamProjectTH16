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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PatientRecordsView {
    private BorderPane view;
    private ListView<String> patientList;
    private TextArea recordsArea;
    private TextField searchField;
    private TextField newPatientField;
    private User currentUser;

    public PatientRecordsView(User user) {
        currentUser = user;

        // Initialize UI components
        Label titleLabel = new Label("Patient Records");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        patientList = new ListView<>();
        patientList.setOnMouseClicked(e -> handlePatientClick());

        recordsArea = new TextArea();
        recordsArea.setEditable(false);

        searchField = new TextField();
        searchField.setPromptText("Search patient");
        searchField.setOnAction(e -> handleSearchButton());

        newPatientField = new TextField();
        newPatientField.setPromptText("Enter new patient name");

        Button createButton = new Button("Create New Patient");
        createButton.setOnAction(e -> handleCreateButton());
        
        Button updateButton = new Button("Update Records");
        updateButton.setOnAction(e -> handleUpdateButton());
        
        if (currentUser.getRole().equals("Doctor")) {
        	newPatientField.setVisible(true);
            updateButton.setVisible(true);
            createButton.setVisible(true);
        } else if (currentUser.getRole().equals("Nurse")) {
        	newPatientField.setVisible(false);
        	updateButton.setVisible(false);
            createButton.setVisible(false);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a BorderPane layout
        GridPane searchBox = new GridPane();
        searchBox.setHgap(10);
        searchBox.add(new Label("Search:"), 0, 0);
        searchBox.add(searchField, 1, 0);

        VBox newPatientBox = new VBox(10);
        newPatientBox.getChildren().addAll(newPatientField, createButton);
        
        VBox recordsBox = new VBox(10);
        recordsBox.getChildren().addAll(recordsArea, updateButton);

        view = new BorderPane();
        view.setTop(titleLabel);
        view.setLeft(patientList);
        view.setCenter(recordsBox);
        view.setBottom(new VBox(10, searchBox, newPatientBox, backButton));
        view.setPadding(new Insets(20));
    }

    private void handlePatientClick() {
        String selectedPatient = patientList.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            displayPatientRecords(selectedPatient);
        }
    }

    private void handleSearchButton() {
        String searchTerm = searchField.getText();
        searchPatients(searchTerm);
    }
    
    private void handleCreateButton() {
        String newPatientName = newPatientField.getText();
        if (!newPatientName.isEmpty()) {
            createNewPatientRecord(newPatientName);
            newPatientField.clear();
            loadPatientList();
        }
    }

    private void handleUpdateButton() {
        String selectedPatient = patientList.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            String updatedRecords = recordsArea.getText();
            updatePatientRecords(selectedPatient, updatedRecords);
        }
    }

    private void handleBackButton() {
        // Navigate back to the doctor view
        DoctorView doctorView = new DoctorView(currentUser);
        Scene scene = new Scene(doctorView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void searchPatients(String searchTerm) {
        patientList.getItems().clear();
        File folder = new File("patient_records");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String patientName = file.getName().replace(".txt", "");
                if (patientName.toLowerCase().contains(searchTerm.toLowerCase())) {
                    patientList.getItems().add(patientName);
                }
            }
        }
    }

    private void displayPatientRecords(String patientName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("patient_records", patientName + ".txt"));
            StringBuilder records = new StringBuilder();
            for (String line : lines) {
                records.append(line).append("\n");
            }
            recordsArea.setText(records.toString());
            
            // Set editability based on user role
            if (currentUser.getRole().equals("Doctor")) {
                recordsArea.setEditable(true);
            } else if (currentUser.getRole().equals("Nurse")) {
                recordsArea.setEditable(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file read fails
            AlertDialog.showErrorDialog("Loading Error", "Loading Patient Records Failed", "Unable to load patient records.");
        }
    }
    
    private void createNewPatientRecord(String patientName) {
        try {
            File folder = new File("patient_records");
            if (!folder.exists()) {
                folder.mkdirs(); // Create the directory if it doesn't exist
            }
            File patientFile = new File("patient_records", patientName + ".txt");
            if (patientFile.createNewFile()) {
                System.out.println("New patient record created: " + patientName);
            } else {
                System.out.println("Patient record already exists: " + patientName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file creation fails
            AlertDialog.showErrorDialog("Saving Error", "Saving Patient Record Failed", "Unable to save patient records.");
        }
    }

    private void updatePatientRecords(String patientName, String updatedRecords) {
        try (FileWriter writer = new FileWriter("patient_records/" + patientName + ".txt")) {
            writer.write(updatedRecords);
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file write fails
            AlertDialog.showErrorDialog("Updating Error", "Updating Patient Record Failed", "Unable to update patient record.");
        }
    }

    public BorderPane getView() {
        loadPatientList();
        return view;
    }

    private void loadPatientList() {
        patientList.getItems().clear();
        File folder = new File("patient_records");
        if (!folder.exists()) {
            folder.mkdirs(); // Create the directory if it doesn't exist
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String patientName = file.getName().replace(".txt", "");
                patientList.getItems().add(patientName);
            }
        }
    }
}
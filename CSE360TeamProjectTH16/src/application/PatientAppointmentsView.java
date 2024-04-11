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

public class PatientAppointmentsView {
    private BorderPane view;
    private ListView<String> appointmentList;
    private User currentPatient;

    public PatientAppointmentsView(User user) {
        currentPatient = user;

        // Initialize UI components
        Label titleLabel = new Label("My Appointments");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        appointmentList = new ListView<>();
        appointmentList.setOnMouseClicked(e -> handleAppointmentClick());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a BorderPane layout
        VBox listBox = new VBox(10);
        listBox.getChildren().addAll(new Label("Appointments:"), appointmentList);

        view = new BorderPane();
        view.setTop(titleLabel);
        view.setCenter(listBox);
        view.setBottom(backButton);
        view.setPadding(new Insets(20));
    }

    private void handleAppointmentClick() {
        String selectedAppointment = appointmentList.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            AlertDialog.showInformationDialog("Appointment Details", "Selected Appointment", selectedAppointment);
        }
    }

    private void handleBackButton() {
        PatientView patientView = new PatientView(currentPatient);
        Scene scene = new Scene(patientView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void loadPatientAppointments() {
        appointmentList.getItems().clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get("appointments.txt"));
            for (int i = 0; i < lines.size(); i += 6) {
                String patientLine = lines.get(i);
                String patientName = patientLine.substring(patientLine.indexOf(":") + 2);

                if (patientName.equals(currentPatient.getFirstName() + " " + currentPatient.getLastName())) {
                    String appointmentEntry = String.join("\n", lines.subList(i, i + 5));
                    appointmentList.getItems().add(appointmentEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showErrorDialog("Error", "Failed to Load Appointments", "Failed to load appointments. Please try again.");
        }
    }

    public BorderPane getView() {
        loadPatientAppointments();
        return view;
    }
}
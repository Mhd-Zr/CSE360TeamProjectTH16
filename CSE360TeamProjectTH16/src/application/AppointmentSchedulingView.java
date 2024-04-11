package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class AppointmentSchedulingView {
    private BorderPane view;
    private DatePicker datePicker;
    private ListView<String> availableTimesList;
    private User currentUser;

    public AppointmentSchedulingView(User user) {
        currentUser = user;

        // Initialize UI components
        Label titleLabel = new Label("Appointment Scheduling");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        datePicker = new DatePicker();
        datePicker.setOnAction(e -> handleDateSelection());

        availableTimesList = new ListView<>();
        availableTimesList.setOnMouseClicked(e -> handleTimeSelection());

        Button scheduleButton = new Button("Schedule Appointment");
        scheduleButton.setOnAction(e -> handleScheduleButton());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a BorderPane layout
        GridPane dateBox = new GridPane();
        dateBox.setHgap(10);
        dateBox.add(new Label("Select Date:"), 0, 0);
        dateBox.add(datePicker, 1, 0);

        VBox timesBox = new VBox(10);
        timesBox.getChildren().addAll(new Label("Available Times:"), availableTimesList);

        view = new BorderPane();
        view.setTop(titleLabel);
        view.setLeft(dateBox);
        view.setCenter(timesBox);
        view.setBottom(new VBox(10, scheduleButton, backButton));
        view.setPadding(new Insets(20));
    }

    private void handleDateSelection() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            loadAvailableTimes(selectedDate);
        }
    }

    private void handleTimeSelection() {
        String selectedTime = availableTimesList.getSelectionModel().getSelectedItem();
        if (selectedTime != null) {
            System.out.println("Selected time: " + selectedTime);
        }
    }

    private void handleScheduleButton() {
    	LocalDate selectedDate = datePicker.getValue();
        String selectedTime = availableTimesList.getSelectionModel().getSelectedItem();
        if (selectedDate != null && selectedTime != null) {
            scheduleAppointment(selectedDate, selectedTime);
            clearFields();
            loadAvailableTimes(selectedDate);
        }
    }

    private void handleBackButton() {
        // Navigate back to the patient view
        PatientView patientView = new PatientView(currentUser);
        Scene scene = new Scene(patientView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        stage.setScene(scene);
    }

    private void loadAvailableTimes(LocalDate date) {
    	availableTimesList.getItems().clear();
        String fileName = "available_times_" + date.toString() + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(fileName));
                availableTimesList.getItems().addAll(lines);
            } catch (IOException e) {
                e.printStackTrace();
                // Display an error message if file read fails
            }
        } else {
            // Create a new file with default available times if it doesn't exist
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("09:00\n10:00\n11:00\n12:00\n13:00\n14:00\n15:00\n16:00");
                availableTimesList.getItems().addAll("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");
            } catch (IOException e) {
                e.printStackTrace();
                // Display an error message if file creation fails
            }
        }
    }

    private void scheduleAppointment(LocalDate date, String time) {
    	String appointmentDetails = "Patient: " + currentUser.getFirstName() + " " + currentUser.getLastName() +
                "\nDate: " + date +
                "\nTime: " + time;

        try (FileWriter writer = new FileWriter("appointments.txt", true)) {
            writer.write(appointmentDetails + "\n");
            writer.write("-----------------------------\n");
            System.out.println("Appointment scheduled successfully!");
            removeSelectedTime(date, time);
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file write fails
        }
    }
    
    private void removeSelectedTime(LocalDate date, String time) {
        String fileName = "available_times_" + date.toString() + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(fileName));
                lines.remove(time);
                Files.write(Paths.get(fileName), lines);
            } catch (IOException e) {
                e.printStackTrace();
                // Display an error message if file update fails
            }
        }
    }

    private void clearFields() {
        datePicker.setValue(null);
        availableTimesList.getSelectionModel().clearSelection();
    }

    public BorderPane getView() {
        return view;
    }
}
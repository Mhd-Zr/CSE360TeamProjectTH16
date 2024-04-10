package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class MessagingView {
    private BorderPane view;
    private ListView<String> conversationList;
    private TextArea messageArea;
    private TextField recipientField;
    //private TextField messageField;
    private TextArea messageField;
    private Button sendButton;
    private User currentUser;

    public MessagingView(User user) {
        currentUser = user;

        // Initialize UI components
        Label titleLabel = new Label("Messaging");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        conversationList = new ListView<>();
        conversationList.setPrefWidth(200);
        conversationList.setPadding(new Insets(0, 10, 0, 0));
        conversationList.setOnMouseClicked(e -> handleConversationClick());

        messageArea = new TextArea();
        messageArea.setEditable(false);

        recipientField = new TextField();
        recipientField.setPromptText("Enter recipient username");

        //messageField = new TextField();
        //messageField.setPromptText("Enter your message");
        Label messageLabel = new Label("Message:");
        messageField = new TextArea();
        messageField.setPromptText("Enter your message");
        messageField.setPrefRowCount(3);

        sendButton = new Button("Send");
        sendButton.setOnAction(e -> handleSendButton());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        // Arrange components in a BorderPane layout
        HBox recipientBox = new HBox(10);
        recipientBox.getChildren().addAll(new Label("Recipient:"), recipientField);

        GridPane messageBox = new GridPane();
        messageBox.setHgap(10);
        messageBox.setVgap(10);
        messageBox.add(messageLabel, 0, 0);
        messageBox.add(messageField, 0, 1);
        messageBox.add(sendButton, 0, 2);

        VBox messagePane = new VBox(10);
        messagePane.getChildren().addAll(messageArea, recipientBox, messageBox);

        view = new BorderPane();
        view.setTop(titleLabel);
        view.setLeft(conversationList);
        view.setCenter(messagePane);
        view.setBottom(backButton);
        view.setPadding(new Insets(20));
    }

    private void handleConversationClick() {
        String selectedConversation = conversationList.getSelectionModel().getSelectedItem();
        if (selectedConversation != null) {
            recipientField.setText(selectedConversation);
            displayMessages(selectedConversation);
        }
    }

    private void handleSendButton() {
        String recipient = recipientField.getText();
        String message = messageField.getText();

        if (!recipient.isEmpty() && !message.isEmpty()) {
            sendMessage(recipient, message);
            messageField.clear();
        }
    }

    private void handleBackButton() {
        // Navigate back to the appropriate user portal based on the user's role
        if (currentUser.getRole().equals("Doctor")) {
            navigateToDoctorView(currentUser);
        } else if (currentUser.getRole().equals("Nurse")) {
            navigateToNurseView(currentUser);
        } else if (currentUser.getRole().equals("Patient")) {
            navigateToPatientView(currentUser);
        }
    }

    private void sendMessage(String recipient, String message) {
        String senderUsername = currentUser.getEmail();
        String messageEntry = senderUsername + "," + recipient + "," + message;

        try {
            if (!Files.exists(Paths.get("messages.txt"))) {
                Files.createFile(Paths.get("messages.txt"));
            }
            Files.write(Paths.get("messages.txt"), (messageEntry + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            loadConversations();
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error message if file write fails
        }
    }

    private void loadConversations() {
        List<String> conversations = new ArrayList<>();

        if (Files.exists(Paths.get("messages.txt"))) {
            try {
                List<String> lines = Files.readAllLines(Paths.get("messages.txt"));
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String sender = parts[0];
                        String recipient = parts[1];

                        if (sender.equals(currentUser.getEmail()) && !conversations.contains(recipient)) {
                            conversations.add(recipient);
                        } else if (recipient.equals(currentUser.getEmail()) && !conversations.contains(sender)) {
                            conversations.add(sender);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Display an error message if file read fails
            }
        }

        conversationList.getItems().clear();
        if (conversations.isEmpty()) {
        	Label noMessagesLabel = new Label("No messages found!");
            noMessagesLabel.setStyle("-fx-alignment: center;");
            conversationList.setPlaceholder(noMessagesLabel);
        } else {
            conversationList.getItems().addAll(conversations);
        }
    }

    private void displayMessages(String recipient) {
        List<String> messages = new ArrayList<>();

        if (Files.exists(Paths.get("messages.txt"))) {
            try {
                List<String> lines = Files.readAllLines(Paths.get("messages.txt"));
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String sender = parts[0];
                        String messageRecipient = parts[1];
                        String message = parts[2];

                        if ((sender.equals(currentUser.getEmail()) && messageRecipient.equals(recipient))
                                || (sender.equals(recipient) && messageRecipient.equals(currentUser.getEmail()))) {
                            messages.add(sender + ": " + message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Display an error message if file read fails
            }
        }

        messageArea.clear();
        if (messages.isEmpty()) {
            messageArea.setText("No messages");
        } else {
            for (String message : messages) {
                messageArea.appendText(message + "\n");
            }
        }
    }

    private void navigateToDoctorView(User user) {
        DoctorView doctorView = new DoctorView(user);
        Scene scene = new Scene(doctorView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    private void navigateToNurseView(User user) {
        NurseView nurseView = new NurseView(user);
        Scene scene = new Scene(nurseView.getView(), 800, 600);
        Stage stage = (Stage) view.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
    }

    private void navigateToPatientView(User user) {
		/*
		 * PatientView patientView = new PatientView(user);
		 * Scene scene = new Scene(patientView.getView(), 800, 600);
		 * Stage stage = (Stage) view.getScene().getWindow();
		 * scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		 * stage.setScene(scene);
		 */
    }

    public BorderPane getView() {
        loadConversations();
        return view;
    }
}
package frontend;

import backend.Customer;
import backend.TicketPool;
import backend.Vendor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import backend.Configuration;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the main frontend (GUI) of the Ticket Management System using JavaFX.
 * This application provides a graphical user interface (GUI) for:
 * - User input to configure system parameters (e.g., total tickets, release rate).
 * - Starting and stopping the system with vendors and customers.
 * - Saving and loading configurations to/from files.
 * - Displaying real-time system status and updates.
 */
public class TicketManagementApp extends Application {

    // Input fields
    private TextField totalTicketsField;
    private TextField ticketReleaseRateField;
    private TextField customerRetrievalRateField;
    private TextField maxTicketCapacityField;
    private TextField fileNameField;

    // Status area
    private TextArea statusArea;

    // Lists to hold running threads
    private List<Thread> vendorThreadsList = new ArrayList<>();
    private List<Thread> customerThreadsList = new ArrayList<>();

    // Configuration object
    private Configuration config;

    /**
     * Sets up the GUI of the Ticket Management System.
     * Includes:
     * - Input fields for users to type-in parameters.
     * - Buttons for starting and stopping the system, and saving and loading configurations.
     * - A status area to provide real-time updates on the system to the user.
     *
     * @param primaryStage the primary window for the JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {
        // Input Section: to get user input
        GridPane inputLayout = new GridPane();
        inputLayout.setPadding(new Insets(10));
        inputLayout.setHgap(10);
        inputLayout.setVgap(10);

        // Create Labels and TextFields
        Label totalTicketsLabel = new Label("Total Tickets:");
        totalTicketsField = new TextField();
        Label ticketReleaseRateLabel = new Label("Ticket Release Rate:");
        ticketReleaseRateField = new TextField();
        Label customerRetrievalRateLabel = new Label("Customer Retrieval Rate:");
        customerRetrievalRateField = new TextField();
        Label maxTicketCapacityLabel = new Label("Maximum Ticket Capacity:");
        maxTicketCapacityField = new TextField();
        Label fileNameLabel = new Label("File Name (To Save Configuration):");
        fileNameField = new TextField();

        // Add text Labels to input layout grid
        inputLayout.add(totalTicketsLabel, 0, 0);
        inputLayout.add(ticketReleaseRateLabel, 0, 1);
        inputLayout.add(customerRetrievalRateLabel, 0, 2);
        inputLayout.add(maxTicketCapacityLabel, 0, 3);
        inputLayout.add(fileNameLabel, 0, 4);

        // Add TextFields to input section
        inputLayout.add(totalTicketsField, 1, 0);
        inputLayout.add(ticketReleaseRateField, 1, 1);
        inputLayout.add(customerRetrievalRateField, 1, 2);
        inputLayout.add(maxTicketCapacityField, 1, 3);
        inputLayout.add(fileNameField, 1, 4);

        // Control Section: includes buttons to start and stop the system
        HBox controlLayout = new HBox(10);
        controlLayout.setPadding(new Insets(10));
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button saveToFileButton = new Button("Save configuration to file");
        Button loadFromFileButton = new Button("Load configuration from file");
        controlLayout.getChildren().addAll(startButton, stopButton, saveToFileButton, loadFromFileButton);

        // Status Section: projects real-time status updates of the system to users
        statusArea = new TextArea();
        // To prevent user from editing status box (read-only feature)
        statusArea.setEditable(false);
        statusArea.setPrefHeight(200);

        // Organize input, control and status sections in a vertical layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(inputLayout, controlLayout, statusArea);

        // Scene and Stage
        Scene scene = new Scene(mainLayout, 475, 475);
        primaryStage.setTitle("Ticket Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Button Actions
        startButton.setOnAction(e -> {
            try {
                // Get values inputted by user from text fields
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int ticketReleaseRate = Integer.parseInt(ticketReleaseRateField.getText());
                int customerRetrievalRate = Integer.parseInt(customerRetrievalRateField.getText());
                int maxTicketCapacity = Integer.parseInt(maxTicketCapacityField.getText());


                // Input validation
                if (totalTickets <= 0 || ticketReleaseRate <= 0 || customerRetrievalRate <= 0 || maxTicketCapacity <= 0) {
                    statusArea.appendText("Error! All values must be a positive integer. Please try again.\n");
                    return;
                }
                if (totalTickets < maxTicketCapacity) {
                    statusArea.appendText("Max ticket capacity cannot exceed total tickets. Please try again.\n");
                    return;
                }

                // Initialize Configuration object with inputted values from user
                config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

                // Initialize TicketPool class
                TicketPool ticketPool = new TicketPool(maxTicketCapacity);

                // Start vendor threads
                for (int i = 0; i < ticketReleaseRate; i++) {
                    Vendor vendor = new Vendor(ticketPool, totalTickets / ticketReleaseRate);
                    Thread vendorThread = new Thread(vendor);
                    vendorThread.start();
                    // Add vendor threads to vendorThreadsList
                    vendorThreadsList.add(vendorThread);
                }

                // Start customer threads
                for (int i = 0; i < customerRetrievalRate; i++) {
                    Customer customer = new Customer(ticketPool, maxTicketCapacity / customerRetrievalRate);
                    Thread customerThread = new Thread(customer);
                    customerThread.start();
                    // Add customer threads to customerThreadsList
                    customerThreadsList.add(customerThread);
                }

                // Status area system updates
                statusArea.appendText("System Started\n");
                statusArea.appendText("Configuration Details:\n");
                statusArea.appendText("Total tickets: " + totalTickets + "\n");
                statusArea.appendText("Ticket release rate: " + ticketReleaseRate + "\n");
                statusArea.appendText("Customer retrieval rate: " + customerRetrievalRate + "\n");
                statusArea.appendText("Maximum ticket capacity: " + maxTicketCapacity + "\n");

            } catch (NumberFormatException numberFormatException) {
                statusArea.appendText("Error! Please enter positive integer values in provided fields.\n");
            } catch (Exception exception) {
                statusArea.appendText("An error occurred..." + exception + "\n");
            }
        });

        // Stop button to halt the system
        stopButton.setOnAction(e -> {
            // Stop all vendor threads
            for (Thread vendorThread:vendorThreadsList) {
                vendorThread.interrupt();
            }
            // Stop all customer threads
            for (Thread customerThread:customerThreadsList) {
                customerThread.interrupt();
            }

            // Clear the lists of threads
            vendorThreadsList.clear();
            customerThreadsList.clear();
            statusArea.appendText("System Stopped\n");
        });

        // Save Configuration To File Button (to save the configuration parameters to a file)
        saveToFileButton.setOnAction(e -> {
            String fileName = fileNameField.getText();
            // Check to see if user has entered configuration parameters in the TextFields
            if (config == null) {
                statusArea.appendText("Error: Please start the system before saving or loading configurations.\n");
                return;
            } else {
                // Check if file is empty
                if (!fileName.isEmpty()) {
                    config.saveConfiguration(fileName);
                    statusArea.appendText("Configuration saved to file: " + fileName + "\n");
                } else {
                    statusArea.appendText("Please enter a valid file name.\n");
                }
            }
        });

        // Load Configuration From File Button (To us a preexisting configuration)
        loadFromFileButton.setOnAction(e -> {
            String fileName = fileNameField.getText();
            // Check to see if user has entered configuration parameters in the TextFields
            if (config == null) {
                statusArea.appendText("Error: Please enter a valid file name.\n");
                return;
            } else {
                // Check if file is empty
                if (!fileName.isEmpty()) {
                    Configuration loadedConfig = config.loadConfiguration(fileName);
                    if (loadedConfig != null) {
                        totalTicketsField.setText(String.valueOf(loadedConfig.getTotalTickets()));
                        ticketReleaseRateField.setText(String.valueOf(loadedConfig.getTicketReleaseRate()));
                        customerRetrievalRateField.setText(String.valueOf(loadedConfig.getCustomerRetrievalRate()));
                        maxTicketCapacityField.setText(String.valueOf(loadedConfig.getMaxTicketCapacity()));
                        statusArea.appendText("Configuration loaded from file: " + fileName + "\n");
                    } else {
                        statusArea.appendText("Failed to load configuration from file. Please try again.\n");
                    }
                } else {
                    statusArea.appendText("Please enter a valid file name. \n");
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

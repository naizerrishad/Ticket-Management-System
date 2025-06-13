package backend;

import com.google.gson.Gson;
import java.io.*;

/**
 * Represents the configuration settings, set by the user, for the ticket management system.
 * This class stores the total tickets, ticket release rate, customer retrieval rate, and maximum ticket capacity.
 * Methods to save and load the configuration from/to a file using JSON format, using the Gson library, have also been implemented.
 */
public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Constructor for the Configuration class with totalTickets, ticketReleaseRate, customerRetrievalRate and maxTicketCapacity passed into the parameters to initialize the configuration.
     */
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Getter for totalTickets.
     *
     * @return The total number of tickets.
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Getter for ticketReleaseRate.
     *
     * @return The ticket release rate.
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Getter for customerRetrievalRate.
     *
     * @return The customer retrieval rate.
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Getter for maxTicketCapacity.
     *
     * @return The maximum ticket capacity.
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * This method saves the configuration object to a file in JSON format.
     * This method converts the configuration into a JSON string and writes it to a file.
     *
     * @param fileName The name of the file to save the configuration to.
     */
    public void saveConfiguration(String fileName) {
        //Create a Gson object
        Gson gson = new Gson();

        //Convert configuration object into JSON format
        String json = gson.toJson(this);
        File myFile = new File(fileName);

        //Try catch block which contains logic to write configuration to a file
        try {
            FileWriter fileWriter = new FileWriter(myFile);

            //Write JSON format into file
            fileWriter.write(json);
            //Close the file
            fileWriter.close();
            System.out.println("Configuration successfully save to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving configuration to file..." + e);
        }
    }

    /**
     * Method to load the configuration object from a file.
     * This method reads the JSON data from the specified file and converts it into a Configuration object.
     *
     * @param fileName The name of the file to load the configuration from.
     * @return The loaded Configuration object, or {@code null} if an error occurs.
     */
    public Configuration loadConfiguration(String fileName) {
        //Create a Gson object
        Gson gson = new Gson();

        File myFile = new File(fileName);

        if (myFile.exists() && myFile.canRead()) {
            try {
                FileReader fileReader = new FileReader(fileName);
                //Deserialize JSON format into configuration object and assign to variable
                Configuration config = gson.fromJson(fileReader, Configuration.class);
                System.out.println("Configuration loaded successfully from file: " + fileName);
                //Close the file
                fileReader.close();
                return config;
            } catch (IOException e) {
                System.out.println("Error loading configuration from file..." + e);
                //return null if loading fails
                return null;
            }
        } else {
            System.out.println("File does not exist.");
            return null;
        }
    }
}

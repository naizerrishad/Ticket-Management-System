package backend;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Command-line interface (CLI) for the ticket management system.
 * This class handles user inputs for configuration parameters and starts the system.
 */
public class CLI {
    public static void main(String[] args) {
        // Create the scanner object to get inputs from user
        Scanner scanner = new Scanner(System.in);

        // Configuration variables to be inputted by the user
        int totalTickets;
        int ticketReleaseRate;
        int customerRetrievalRate;
        int maxTicketCapacity;

        // Get user input for totalTickets and validate it
        while (true) {
            try {
                System.out.println("Please enter the total number of tickets: ");
                totalTickets = scanner.nextInt();
                if (totalTickets <= 0) {
                    System.out.println("Total number of tickets must be a positive integer!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }

        // Get user input for ticketReleaseRate and validate it
        while (true) {
            try {
                System.out.println("Please enter the ticket release rate: ");
                ticketReleaseRate = scanner.nextInt();
                if (ticketReleaseRate <= 0) {
                    System.out.println("Ticket release rate must be a positive integer!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }

        // Get user input for customerRetrievalRate and validate it
        while (true) {
            try {
                System.out.println("Please enter the customer retrieval rate: ");
                customerRetrievalRate = scanner.nextInt();
                if (customerRetrievalRate <= 0) {
                    System.out.println("Customer retrieval rate must be a positive integer!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }

        // Get user input for maxTicketCapacity and validate it
        while (true) {
            try {
                System.out.println("Please enter the maximum number of tickets: ");
                maxTicketCapacity = scanner.nextInt();
                if (maxTicketCapacity > totalTickets) {
                    System.out.println("Maximum number of tickets cannot exceed total number of tickets!");
                } else if (maxTicketCapacity <= 0) {
                    System.out.println("Maximum number of tickets must be a positive integer!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }

        // Create an instance of the Configuration class using inputs as parameters
        Configuration config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        // Initialize the TicketPool object using maxTicketCapacity in the constructor
        TicketPool ticketPool = new TicketPool(maxTicketCapacity);

        // Create and start the vendor threads
        for (int i = 0; i<ticketReleaseRate; i++) {
            Vendor vendor = new Vendor(ticketPool, totalTickets / ticketReleaseRate);
            Thread vendorThread = new Thread(vendor);
            vendorThread.start();
        }

        // Create and start the customer threads
        for (int i = 0; i<customerRetrievalRate; i++) {
            Customer customer = new Customer(ticketPool, maxTicketCapacity / customerRetrievalRate);
            Thread customerThread = new Thread(customer);
            customerThread.start();
        }

        // Let system run for 10 seconds

        try {
            System.out.println("System running... (Press Ctrl+C to halt the system)");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("System halted.");
        }

        // Display the users configuration inputs
        System.out.println("Configuration inputs received successfully!");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);

        // Close the scanner object
        scanner.close();
    }
}
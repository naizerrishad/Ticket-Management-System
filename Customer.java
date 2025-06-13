package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Customer that retrieves tickets from the TicketPool, using the removeTicket() method.
 * Customer waits for the vendor to add tickets to the pool if it's empty, and retrieves tickets once the vendor has added tickets to the pool.
 * This class implements the Runnable interface.
 */
public class Customer implements Runnable{
    private TicketPool ticketPool;
    private int numOfOrderedTickets;
    private List<Object> ticketList = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructor for the Customer class with TicketPool object and numOfOrderedTickets to be passed in the parameters.
     *
     * @param ticketPool Ticket pool which tickets will be retrieved from.
     * @param numOfOrderedTickets The number of ordered tickets for the customer to receive.
     */
    public Customer(TicketPool ticketPool, int numOfOrderedTickets) {
        this.ticketPool = ticketPool;
        this.numOfOrderedTickets = numOfOrderedTickets;
    }

    /**
     * Retrieves tickets from the TicketPool.
     * If the pool is empty, the customer waits for a vendor to add more tickets.
     * This method runs in a separate thread for each customer.
     * The customer stops removing tickets if interrupted.
     *
     * @see TicketPool#removeTicket()
     */
    @Override
    public void run() {
        try {
            // Wait for vendor to add tickets to the pool if empty.
            while (ticketPool.ticketList.isEmpty()) {
                System.out.println("Ticket pool is empty. Waiting for vendor to load tickets...");
                synchronized (ticketPool) {
                    ticketPool.wait();
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Customer thread was interrupted while waiting for tickets.");
                    // Halt thread if interrupted
                    return;  // Stop the thread if it's interrupted
                }
            }

            // Retrieve the ordered number of tickets and return it to customer
            for (int i = 0; i < numOfOrderedTickets; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Customer thread was interrupted during ticket extraction.");
                    return;  // Stop the thread if it's interrupted
                }

                String ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    // Add ticket if ticket is null
                    ticketList.add(ticket);
                }
                // Simulate time taken for customer to retrieve ticket
                Thread.sleep(1000);
            }
            System.out.println("Tickets successfully extracted from ticket pool!");

            // Notify all vendor waiting vendor threads that space is available in the ticket pool
            synchronized (ticketPool) {
                ticketPool.notifyAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Customer thread interrupted.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }
    }

    /**
     * Getter for list of tickets retrieved by the customer.
     *
     * @return A list of tickets retrieved by the customer.
     */
    public List<Object> getTicketList() {
        return ticketList;
    }
}

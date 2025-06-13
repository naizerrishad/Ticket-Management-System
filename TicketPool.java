package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a pool of tickets for vendors and customers to interact with.
 * Vendors add tickets to the pool
 * Customers retrieve tickets from the pool by removing them.
 * Synchronization for thread-safe access to the pool.
 */

public class TicketPool {
    private int maxTicketCapacity;

    /**
     * A thread-safe list to store tickets.
     */
    protected List<Object> ticketList = Collections.synchronizedList(new ArrayList<>());

    /**
     * Counter to generate ticket numbers.
     */
    private int ticketCounter = 1;

    /**
     * Constructor for TicketPool class with maxTicketCapacity to be passed in the parameters.
     *
     * @param maxTicketCapacity the maximum number of tickets the pool can hold.
     */
    public TicketPool(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Adds tickets to the ticket pool.
     * Vendor threads wait if adding the tickets would exceed the pool's maximum capacity.
     *
     * @param count the number of tickets to add
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void addTickets(int count) {
        try {
            while (ticketList.size() + count > maxTicketCapacity) {
                System.out.println("Cannot add tickets beyond maximum capacity: " + maxTicketCapacity);
                wait();
            }
            for (int i = 0; i < count; i++) {
                ticketList.add("Ticket no." + ticketCounter++);
            }
            System.out.println("Tickets (" + count + ") added to ticket pool successfully!");
            // Notify all waiting customer threads that tickets are available in the ticketPool.
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor thread interrupted.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }
    }

    /**
     * Removes and returns tickets from the ticket pool to customer.
     * Customer threads wait if the pool is empty, until vendor adds more tickets.
     *
     * @return the ticket retrieved from the pool, or {@code null} if interrupted while waiting
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized String removeTicket() {
        while (ticketList.isEmpty()) {
            try {
                System.out.println("Ticket pool is empty. Please wait until a vendor adds more tickets the pool");
                wait();
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Error while waiting to remove ticket.");
                return null;
            }
        }

        String ticket = (String) ticketList.remove(0);
        System.out.println("Your ticket (" + ticket + ") has been retrieved from the pool");
        // Notify all waiting vendor threads that space is available in the ticketPool.
        notifyAll();
        return ticket;
    }
}

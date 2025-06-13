package backend;

/**
 * Represents a Vendor that adds tickets to the TicketPool at a specified rate using the addTickets() method in the TicketPool class.
 * Vendor adds tickets to the pool in batches and waits for the ticket pool to have capacity when at maximum capacity.
 * This class implements the Runnable interface
 */
public class Vendor implements Runnable{
    private TicketPool ticketPool;
    private int ticketsPerBatch;

    /**
     * Constructor for the Vendor class with TicketPool object and ticketsPerBatch to be passed in the parameters.
     *
     * @param ticketPool Ticket pool which tickets are added to.
     * @param ticketsPerBatch The number of tickets to add per batch.
     */
    public Vendor(TicketPool ticketPool, int ticketsPerBatch) {
        this.ticketPool = ticketPool;
        this.ticketsPerBatch = ticketsPerBatch;
    }

    /**
     * Adds tickets to the TicketPool in batches.
     * This method runs in a separate thread for each vendor.
     * The vendor will wait if the pool has reached maximum capacity.
     *
     * @see TicketPool#addTickets(int)
     */
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Vendor is adding tickets to ticket pool...");
                ticketPool.addTickets(ticketsPerBatch);
                // Simulate delay in adding tickets
                Thread.sleep(1000);

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Vendor thread was interrupted while sleeping or adding tickets.");
                    // Exit loop if thread is interrupted
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor thread interrupted.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }
    }
}

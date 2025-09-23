package Entity;

public class Ticket {
    // just like pnr every ticket has id
    private int ticketId;
    private User user;
    private Train train;
    private int seatBooked;
    // since every pnr is unique so take a counter which get increments for evrybody instead using in constructor separately so make it static
    private static int counter=1001;

    public Ticket(User user, Train train, int seatBooked) {
        this.ticketId = counter++;
        this.user = user;
        this.train = train;
        this.seatBooked = seatBooked;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getSeatBooked() {
        return seatBooked;
    }

    public void setSeatBooked(int seatBooked) {
        this.seatBooked = seatBooked;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Ticket.counter = counter;
    }

    @Override
    public String toString() {
        return "Ticket ID: "+ticketId +
            " | Train:"+train.getName()+" | Route:"+train.getSource()+" -> "+train.getDestination()+
            "| Seats: "+seatBooked+" | Booked By: "+user.getFullName();
    }
}
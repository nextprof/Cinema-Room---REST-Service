package com.cinema;

import java.util.UUID;

public class Ticket {

    private UUID token;

    private Seat ticket;

    Ticket(){};

    public Ticket(UUID token, Seat ticket) {
        this.token = token;
        this.ticket = ticket;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Seat getTicket() {
        return ticket;
    }

    public void setTicket(Seat ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "token=" + token +
                ", ticket=" + ticket +
                '}';
    }
}

package com.cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.util.stream.Collectors;

@RestController
public class SeatController {

    List<Seat> seats = new ArrayList<>(initAvailableSeats());
    List<Ticket> boughtTickets = new ArrayList<>();

    final int total_rows = 9;
    final int total_columns = 9;


    @GetMapping("/seats")
    public Map<String, ?> getSeats() {
        return getAvailableSeatsInfo();
    }

    @PostMapping(value="/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat seat) {

        if(seatOutOfBounds(seat)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error("The number of a row or a column is out of bounds!"));
        }

        Seat toPurchaseSeat = getSeat(seat.getRow(), seat.getColumn());
        if(!toPurchaseSeat.isEnabled())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error("The ticket has been already purchased!"));
        }

        toPurchaseSeat.setEnabled(false);

        Ticket newTicket = new Ticket(UUID.randomUUID(), toPurchaseSeat);
        boughtTickets.add(newTicket);

        return ResponseEntity.status(HttpStatus.OK)
                .body(newTicket);

    }

    @PostMapping(value="/return")
    public ResponseEntity<?> returnTicket(@RequestBody Ticket ticket) {

        for (Ticket boughtTicket : boughtTickets) {

            if (boughtTicket.getToken().equals(ticket.getToken())) {

                boughtTicket.getTicket().setEnabled(true);
                boughtTickets.remove(boughtTicket);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("returned_ticket", boughtTicket.getTicket()));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Error("Wrong token!"));
    }

    public List<Seat> initAvailableSeats() {
        List<Seat> seats = new ArrayList<>();

        for(int i = 1; i <= total_rows; i++)
            for (int j = 1; j <= total_columns; j++)
                seats.add(new Seat(i, j, i <= 4 ? 10 : 8,true));
        return seats;
    }
    public Map<String, ?> getAvailableSeatsInfo() {
        return Map.of(
                "total_rows", total_rows,
                "available_seats", getAvailableSeats(),
                "total_columns", total_columns
        );
    }
    private List<Seat> getAvailableSeats() {
        return seats.stream()
                .filter(Seat::isEnabled)
                .collect(Collectors.toList());
    }


    public Seat getSeat(int row, int col) {
       List<Seat> seatList = seats.stream()
                .filter(p -> p.getRow()==row && p.getColumn()==col)
                .collect(Collectors.toList());
        return seatList.get(0);
    }


    public boolean seatOutOfBounds(Seat seat)
    {
        return seat.getRow() < 1
                || seat.getRow() > total_rows
                || seat.getColumn() < 1
                || seat.getColumn() > total_columns;
    }

}

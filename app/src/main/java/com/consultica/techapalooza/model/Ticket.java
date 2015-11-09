package com.consultica.techapalooza.model;


import java.util.List;

public class Ticket {

    String id;
    String code;
    String date;
    boolean verified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public class TicketResponse {
        Data data;

        class Data {
            boolean canReedem;
            List<Ticket> tickets;
        }

        public List<Ticket> getTickets(){
            return data.tickets;
        }
    }

    public class TicketPriceResponse {
        public Data data;

        class Data {
            int price; // in cents
            String currency;
        }

        public int getPrice(){
            return data.price;
        }
    }
}

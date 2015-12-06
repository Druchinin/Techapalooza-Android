package com.consultica.techapalooza.model;


import com.google.gson.annotations.Expose;

import java.util.List;

public class Ticket {

    @Expose
    String id;
    @Expose
    String code;
    @Expose
    String date;
    @Expose
    boolean verified;
    @Expose
    int ticketId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTicketId() {
        return ticketId+"";
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public class TicketResponse {
        @Expose
        Data data;

        class Data {
            @Expose
            boolean canRedeem;
            @Expose
            List<Ticket> tickets;
        }

        public List<Ticket> getTickets() {
            return data.tickets;
        }

        public boolean canReedem() {
            return data.canRedeem;
        }
    }

    public class TicketPriceResponse {
        @Expose
        public Data data;

        class Data {
            @Expose
            int price; // in cents
            @Expose
            String currency;
        }

        public int getPrice() {
            return data.price;
        }
    }
}

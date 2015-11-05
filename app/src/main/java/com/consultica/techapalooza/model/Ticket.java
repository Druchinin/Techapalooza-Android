package com.consultica.techapalooza.model;


import java.util.List;

public class Ticket {

    private String id;
    private String code;
    private String date;
    private boolean verified;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public class TicketResponse {
        public Data data;

        class Data {
            boolean canReedem;
            List<Ticket> tickets;
        }
    }
}

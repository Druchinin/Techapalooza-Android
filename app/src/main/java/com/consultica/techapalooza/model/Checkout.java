package com.consultica.techapalooza.model;

import java.io.Serializable;

public class Checkout implements Serializable {

    private String band;
    private int numberOfTickets;

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

}

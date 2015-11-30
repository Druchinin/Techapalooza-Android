package com.consultica.techapalooza.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Band implements Serializable{

    private String id;
    private String name;
    private String logo;
    private String description;
    private String date;

    public Band(){}

    public Band(String id, String name, String logo, String description, String date) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHours() {
        Calendar calendar = Schedule.getCalendarFromISO(date);
        return String.valueOf(calendar.get(Calendar.HOUR));
    }

    public String getMin() {
        Calendar calendar = Schedule.getCalendarFromISO(date);
        return String.valueOf(calendar.get(Calendar.MINUTE));
    }

    public String getAM_PM() {
        Calendar calendar = Schedule.getCalendarFromISO(date);
        if (calendar.get(Calendar.AM_PM) == 1) return "pm";
        return "am";
    }

    public class BandResponse {

        public Data data;

        class Data {
            List<Item> bands;
        }

        class Item {
            String id;
            String name;
            String logo;
            String description;
            String starts_at;
        }

        public List<Band> getAllBands() {
            List<Band> list = new ArrayList<>();

            for (Item item : data.bands){
                Band band = new Band(item.id, item.name, item.logo, item.description, item.starts_at);
                list.add(band);
            }

            return list;
        }
    }
}

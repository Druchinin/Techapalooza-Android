package com.consultica.techapalooza.model;

import com.consultica.techapalooza.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Schedule {
    private String id;
    private String starts_at;
    private String name;
    private int amountOfBand = 0;
    private String band_Id;
    private String band_name;
    private int lineColor = R.color.vertLineIndicatorNormal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(String starts_at) {
        this.starts_at = starts_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountOfBand() {
        return amountOfBand;
    }

    public void setAmountOfBand(int amountOfBand) {
        this.amountOfBand = amountOfBand;
    }

    public String getBand_Id() {
        return band_Id;
    }

    public void setBand_Id(String band_Id) {
        this.band_Id = band_Id;
    }

    public String getBand_name() {
        if (band_name != null)
            return band_name;
        else return "";
    }

    public void setBand_name(String band_name) {
        this.band_name = band_name;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getHours() {
        Calendar cal = getCalendarFromISO(starts_at);
        return cal.get(Calendar.HOUR);
    }

    public int getMinutes() {
        Calendar cal = getCalendarFromISO(starts_at);
        return cal.get(Calendar.MINUTE);
    }

    public String getAM_PM(){
        Calendar cal = getCalendarFromISO(starts_at);
        if (cal.get(Calendar.AM_PM) == 1){
            return "pm";
        }
        return "am";
    }

    public static Calendar getCalendarFromISO(String datestring) {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.getDefault());
        try {
            Date date = dateformat.parse(datestring);
            date.setHours(date.getHours());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public class ScheduleResponse {

        public Data data;

        class Data {
            List<Item> schedule;
        }

        class Item {
            String id;
            String starts_at;
            String name;
            ResponseBand band;
        }

        class ResponseBand {
            String id;
            String name;

            public String getBandName(){
                return name;
            }
        }



        public List<Schedule> getAllSchedule() {
            List<Schedule> list = new ArrayList<>();

            for (Item item : data.schedule) {
                Schedule schedule = new Schedule();
                schedule.setId(item.id);
                schedule.setStarts_at(item.starts_at);
                schedule.setName(item.name);
                if (item.band != null) {
                    schedule.setAmountOfBand(1);
                    schedule.setBand_Id(item.band.id);
                    schedule.setBand_name(item.band.name);
                }

                list.add(schedule);
            }

            return list;
        }


    }

}

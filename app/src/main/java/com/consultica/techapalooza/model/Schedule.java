package com.consultica.techapalooza.model;

import com.consultica.techapalooza.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Schedule {
    String id;
    String starts_at;
    String name;
    int amountOfBand = 0;
    String band_Id;
    String band_name;
    int lineColor = R.color.vertLineIndicatorNormal;

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
}

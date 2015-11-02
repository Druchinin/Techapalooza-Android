package com.consultica.techapalooza.network;

import com.consultica.techapalooza.model.Schedule;

import java.util.ArrayList;
import java.util.List;

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

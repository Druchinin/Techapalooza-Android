package com.consultica.techapalooza.ui.fragments.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.ScheduleListAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.Schedule;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.ui.fragments.lineup.BandDetailsFragment;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleListFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.ScheduleListFragment";

    private View view;
    private DBMaster dbMaster;
    private ScheduleListAdapter adapter;

    private List<Schedule> data;

    private static ScheduleListFragment instance;

    public static ScheduleListFragment getInstance() {
        if (instance == null)
            instance = new ScheduleListFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        setup();

        return view;
    }

    private void setup() {
        dbMaster = DBMaster.getInstance(getActivity());

        data = dbMaster.getAllSchedule();

        if (data.size() > 0) {

            for (Schedule schedule : data) {
                if (isNow(schedule.getStarts_at(), schedule.getEnds_at())) {
                    schedule.setNow(true);
                } else {
                    schedule.setNow(false);
                }
            }

            adapter = new ScheduleListAdapter(getActivity(), data);
            adapter.setOnItemClickListener(new ScheduleListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                    Schedule schedule = adapter.getItem(position);

                    if (schedule.getBand_Id() != null) {
                        Band band = dbMaster.getBand(schedule.getBand_Id());



                        BandDetailsFragment.getInstance().setBand(band);
                        BandDetailsFragment.getInstance().show(getActivity().getSupportFragmentManager(), true);
                        
                    }
                }
            });

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.schedule_recycle_view);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);

            setupTimer(data);
        }
    }

    private void setupTimer(List<Schedule> data) {

        for (Schedule schedule : data) {

            Calendar calendar = Schedule.getCalendarFromISO(schedule.getStarts_at());

            Timer timerStart = new Timer();
            timerStart.schedule(new MyTimerTask(schedule, adapter), calendar.getTime());

            calendar = Schedule.getCalendarFromISO(schedule.getEnds_at());

            Timer timerEnd = new Timer();
            timerEnd.schedule(new MyTimerTask(schedule, adapter), calendar.getTime());
        }
    }

    private boolean isNow(String starts_At, String ends_At) {

        Calendar current = Calendar.getInstance();
        Calendar start = Schedule.getCalendarFromISO(starts_At);
        Calendar end = Schedule.getCalendarFromISO(ends_At);

        return current.after(start) && current.before(end);
    }

    @Override
    public String getName() {
        return ScheduleListFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.schedule_container;
    }

    class MyTimerTask extends TimerTask {

        private Schedule schedule;
        private ScheduleListAdapter adapter;

        public MyTimerTask(Schedule schedule, ScheduleListAdapter adapter) {
            this.schedule = schedule;
            this.adapter = adapter;
        }

        @Override
        public void run() {
            if (isNow(schedule.getStarts_at(), schedule.getEnds_at())) {
                schedule.setNow(true);
            } else {
                schedule.setNow(false);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

}

package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapter.ScheduleListAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleListFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.ScheduleListFragment";

    private View view;
    private FragmentTransaction transaction;
    int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        DBMaster dbMaster = DBMaster.getInstance(getActivity());

        List<Schedule> data = dbMaster.getAllSchedule();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.schedule_recycle_view);
        ScheduleListAdapter adapter = new ScheduleListAdapter(getActivity(), data);
        adapter.setOnItemClickListener(new ScheduleListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.schedule_container, new BandDetailsFragment(), BandDetailsFragment.TAG);
                tr.addToBackStack(BandDetailsFragment.TAG);
                tr.commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.changeVerticalIndicatorState(2);
        adapter.notifyDataSetChanged();


        return view;
    }
}

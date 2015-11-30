package com.consultica.techapalooza.ui.fragments.schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.DBMaster;

public class ScheduleFragment extends Fragment {

    private View view;

    private static ScheduleFragment instance;


    public static ScheduleFragment getInstance() {
        if (instance == null)
            instance = new ScheduleFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        setup();

        return view;
    }

    private void setup() {

        if (DBMaster.getInstance(getActivity()).getAllSchedule().size() > 0) {
            ScheduleListFragment.getInstance().show(getActivity().getSupportFragmentManager());
        }
    }

}

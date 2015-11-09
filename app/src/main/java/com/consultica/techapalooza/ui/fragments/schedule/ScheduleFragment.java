package com.consultica.techapalooza.ui.fragments.schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.DBMaster;

public class ScheduleFragment extends Fragment {

    View view;

    public static ScheduleFragment getInstance(){
        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        setup();

        return view;
    }

    private void setup() {

        if (DBMaster.getInstance(getActivity()).getAllSchedule().size() > 0) {

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.add(R.id.schedule_container, new ScheduleListFragment(), "Schedule List");
            ft.commit();

        }
    }
}

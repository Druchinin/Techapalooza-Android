package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapter.ScheduleListAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.Schedule;

import java.util.List;

public class ScheduleListFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.ScheduleListFragment";

    private View view;
    private FragmentTransaction transaction;
    int count;

    private DBMaster dbMaster;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        dbMaster = DBMaster.getInstance(getActivity());

        List<Schedule> data = dbMaster.getAllSchedule();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.schedule_recycle_view);
        final ScheduleListAdapter adapter = new ScheduleListAdapter(getActivity(), data);
        adapter.setOnItemClickListener(new ScheduleListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Schedule schedule = adapter.getItem(position);

                if (schedule.getBand_Id() != null) {
                    Band band = dbMaster.getBand(schedule.getBand_Id());

                    BandDetailsFragment fragment = new BandDetailsFragment();
                    fragment.setBand(band);

                    FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                    tr.replace(R.id.schedule_container, fragment, BandDetailsFragment.TAG);
                    tr.addToBackStack(BandDetailsFragment.TAG);
                    tr.commit();
                } else {
                    Toast.makeText(getActivity(), "Can't get description", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.changeVerticalIndicatorState(2);
        adapter.notifyDataSetChanged();


        return view;
    }
}

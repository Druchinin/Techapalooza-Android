package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapter.ScheduleListAdapter;
import com.consultica.techapalooza.model.Band;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.ScheduleListFragment";

    private View view;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        List<Band> data = new ArrayList<>();
        for (int i = 0; i<10; i++){
            Band band = new Band();
            band.hour = "6";
            band.minutes = "30";
            band.moon = "am";
            band.name = "Doors open";

            data.add(band);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.schedule_recycle_view);
        ScheduleListAdapter adapter = new ScheduleListAdapter(getActivity(), data);
        adapter.setOnItemClickListener(new ScheduleListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.schedule_container, new BandDetailsFragment(), BandDetailsFragment.TAG);
                tr.addToBackStack(null);
                tr.commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}

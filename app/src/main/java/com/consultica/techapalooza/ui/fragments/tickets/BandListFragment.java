package com.consultica.techapalooza.ui.fragments.tickets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.BandListAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;

import java.util.List;

public class BandListFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.tickets.BandListFragment";

    private View view;

    private DBMaster dbMaster;
    private BandListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_band_list, container, false);

        dbMaster = DBMaster.getInstance(getActivity());

        List<Band> data = dbMaster.getAllBands();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.band_recycle_view);
        adapter = new BandListAdapter(getActivity(), data);
        adapter.setOnItemClickListener(new BandListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Band band = adapter.getItem(position);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();

                NumberOfTicketsFragment fragment = new NumberOfTicketsFragment();
                fragment.setBand(band);

                tr.replace(R.id.fragment_tickets_container, fragment, NumberOfTicketsFragment.TAG);
                tr.addToBackStack(NumberOfTicketsFragment.TAG);
                tr.commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }


}

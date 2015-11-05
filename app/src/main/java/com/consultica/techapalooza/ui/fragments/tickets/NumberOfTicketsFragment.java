package com.consultica.techapalooza.ui.fragments.tickets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Band;

public class NumberOfTicketsFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.tickets.NumberOfTicketsFragment";

    private View view;
    private Band band;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_number_of_tickets, container, false);

        return view;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}

package com.consultica.techapalooza.ui.fragments.venue;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;

/**
 * Created by dimadruchinin on 21.11.15.
 */
public class VenueFragmentContainer extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.venue.VenueFragmentContainer";

    private View view;

    private static VenueFragmentContainer instance;

    public static VenueFragmentContainer getInstance() {
        if (instance == null)
            instance = new VenueFragmentContainer();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venue_container, container, false);

        VenueFragment.getInstance().show(getActivity().getSupportFragmentManager());

        return view;
    }

}

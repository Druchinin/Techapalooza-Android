package com.consultica.techapalooza.ui.fragments.venue;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.VenueFragment";

    private View view;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venue, container, false);

        init(savedInstanceState);

        return view;
    }

    private void init(Bundle savedInstanceState) {

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());

        final LatLng THE_FORT_GARRY_HOTEL = new LatLng(49.8879446,-97.1367691);
        GoogleMap map = mapView.getMap();

        if (map != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(THE_FORT_GARRY_HOTEL, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            map.addMarker(new MarkerOptions().position(THE_FORT_GARRY_HOTEL).title("The Fort Garry Hotel"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }
}

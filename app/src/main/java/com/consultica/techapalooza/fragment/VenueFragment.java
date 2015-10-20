package com.consultica.techapalooza.fragment;

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
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venue, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());

        final LatLng THE_FORT_GARRY_HOTEL = new LatLng(49.8879446,-97.1367691);
        map = mapView.getMap();

        if (map != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(THE_FORT_GARRY_HOTEL, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            map.addMarker(new MarkerOptions().position(THE_FORT_GARRY_HOTEL).title("The Fort Garry Hotel"));
        }
        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

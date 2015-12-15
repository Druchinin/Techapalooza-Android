package com.consultica.techapalooza.ui.fragments.venue;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.ui.activities.AboutActivity;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.ui.fragments.about.AboutApp;
import com.consultica.techapalooza.ui.fragments.about.AboutCancerCare;
import com.consultica.techapalooza.ui.fragments.about.AboutConsultica;
import com.consultica.techapalooza.ui.fragments.about.AboutTechapalooza;
import com.consultica.techapalooza.utils.FontFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.VenueFragment";

    private View view;

    private Typeface typeface;

    private MapView mapView;

    private static VenueFragment instance;

    public static VenueFragment getInstance() {
        if (instance == null)
            instance = new VenueFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venue, container, false);

        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        init(savedInstanceState);
        setupViews();

        return view;
    }

    private void setupViews() {

        TextView tv_open = (TextView) view.findViewById(R.id.tv_open);
        TextView tv_00 = (TextView) view.findViewById(R.id.tv_00);
        TextView tv_open_pm = (TextView) view.findViewById(R.id.tv_open_pm);
        TextView tv_bands_pm = (TextView) view.findViewById(R.id.tv_bands_pm);
        TextView tv_bands_00 = (TextView) view.findViewById(R.id.tv_bands_00);
        TextView tv_bands = (TextView) view.findViewById(R.id.tv_bands);
        TextView tv_fort_garry_hotel = (TextView) view.findViewById(R.id.tv_fort_garry_hotel);
        TextView tv_broadway_text = (TextView) view.findViewById(R.id.tv_broadway_text);
        TextView tv_winnipeg_text = (TextView) view.findViewById(R.id.tv_winnipeg_text);
        TextView tv_MB_R3C_0R3_text = (TextView) view.findViewById(R.id.tv_MB_R3C_0R3_text);
        TextView btn_about_techapalooza = (TextView) view.findViewById(R.id.btn_about_techapalooza);
        TextView btn_about_cancer_care = (TextView) view.findViewById(R.id.btn_about_cancer_care);
        TextView btn_about_consultica = (TextView) view.findViewById(R.id.btn_about_consultica);
        TextView btn_about_app = (TextView) view.findViewById(R.id.btn_about_app);

        tv_open.setTypeface(typeface);
        tv_00.setTypeface(typeface);
        tv_open_pm.setTypeface(typeface);
        tv_bands_pm.setTypeface(typeface);
        tv_bands_00.setTypeface(typeface);
        tv_bands.setTypeface(typeface);
        tv_fort_garry_hotel.setTypeface(typeface);
        tv_broadway_text.setTypeface(typeface);
        tv_winnipeg_text.setTypeface(typeface);
        tv_MB_R3C_0R3_text.setTypeface(typeface);
        btn_about_techapalooza.setTypeface(typeface);
        btn_about_cancer_care.setTypeface(typeface);
        btn_about_consultica.setTypeface(typeface);
        btn_about_app.setTypeface(typeface);

        btn_about_techapalooza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("what", AboutTechapalooza.class.getSimpleName()));
            }
        });

        btn_about_cancer_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("what", AboutCancerCare.class.getSimpleName()));
            }
        });

        btn_about_consultica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("what", AboutConsultica.class.getSimpleName()));
            }
        });

        btn_about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("what", AboutApp.class.getSimpleName()));
            }
        });
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

    @Override
    public String getName() {
        return VenueFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.venue_container;
    }
}

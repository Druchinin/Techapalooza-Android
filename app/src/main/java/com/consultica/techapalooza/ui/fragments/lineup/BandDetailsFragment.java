package com.consultica.techapalooza.ui.fragments.lineup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.LineUpGalleryAdapter;
import com.consultica.techapalooza.model.Band;
import com.squareup.picasso.Picasso;

public class BandDetailsFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.BandDetailsFragment";

    private View view;

    private Band band;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_band_details, container, false);

        setupInfo();

        return view;
    }

    private void setupInfo() {
        ImageView band_det_logo_image = (ImageView) view.findViewById(R.id.band_det_logo_image);

        TextView band_det_name_tv = (TextView) view.findViewById(R.id.band_det_name_tv);
        TextView band_det_hour_tv = (TextView) view.findViewById(R.id.band_det_hour_tv);
        TextView band_det_min_tv = (TextView) view.findViewById(R.id.band_det_min_tv);
        TextView band_det_hour_am_pm = (TextView) view.findViewById(R.id.band_det_hour_am_pm);
        TextView band_det_description = (TextView) view.findViewById(R.id.band_det_description);

        if (band != null){
            Picasso.with(getActivity()).load(LineUpGalleryAdapter.URL + band.getLogo()).into(band_det_logo_image);
            band_det_name_tv.setText(band.getName());
            band_det_hour_tv.setText(band.getHours());
            band_det_min_tv.setText(band.getMin());
            band_det_hour_am_pm.setText(band.getAM_PM());
            band_det_description.setText(band.getDescription());
        }
    }

    public void setBand(Band band) {
        this.band = band;
    }
}

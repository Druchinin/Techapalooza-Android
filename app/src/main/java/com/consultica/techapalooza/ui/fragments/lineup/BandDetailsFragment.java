package com.consultica.techapalooza.ui.fragments.lineup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.LineUpGalleryAdapter;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.FontFactory;
import com.squareup.picasso.Picasso;

public class BandDetailsFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.BandDetailsFragment";

    public static final String URL = "https://techapalooza.consulti.ca";

    private View view;

    private Band band;

    private static BandDetailsFragment instance;

    public static BandDetailsFragment getInstance() {
        if (instance == null)
            instance = new BandDetailsFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_band_details, container, false);

        setupInfo();

        return view;
    }

    private void setupInfo() {
        ImageView band_det_logo_image = (ImageView) view.findViewById(R.id.band_det_logo_image);

        TextView band_det_name_tv = (TextView) view.findViewById(R.id.band_det_name_tv);
        band_det_name_tv.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

        TextView band_det_hour_tv = (TextView) view.findViewById(R.id.band_det_hour_tv);
        band_det_hour_tv.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

        TextView band_det_min_tv = (TextView) view.findViewById(R.id.band_det_min_tv);
        band_det_min_tv.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

        TextView band_det_hour_am_pm = (TextView) view.findViewById(R.id.band_det_hour_am_pm);
        band_det_hour_am_pm.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

        TextView band_det_description = (TextView) view.findViewById(R.id.band_det_description);
        band_det_description.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_LIGHT));

        Picasso.with(App.getInstance())
                .load(URL + band.getLogo())
                .placeholder(R.drawable.image_placeholder)
                .into(band_det_logo_image);

        band_det_name_tv.setText(band.getName());
        band_det_hour_tv.setText(band.getHours());

        String min = String.valueOf(band.getMin());
        if (min.length() == 1)
            min = "0" + min;
        band_det_min_tv.setText(min);

        band_det_hour_am_pm.setText(band.getAM_PM());
        band_det_description.setText(band.getDescription());
    }



    public void setBand(Band band) {
        this.band = band;
    }

    @Override
    public String getName() {
        return BandDetailsFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.band_details_container;
    }
}

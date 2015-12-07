package com.consultica.techapalooza.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.ui.fragments.lineup.BandDetailsFragment;
import com.consultica.techapalooza.utils.FontFactory;

public class BandDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.band_details_toolbar);
        setSupportActionBar(toolbar);

        TextView textView;

        Intent intent = getIntent();
        Band band = null;
        if (intent.hasExtra("band")) {
            band = (Band) intent.getSerializableExtra("band");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            textView = (TextView) findViewById(R.id.toolbar_title_band_details);
            textView.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

            if (band != null){
                textView.setText("Band");

                BandDetailsFragment.getInstance().setBand(band);
                BandDetailsFragment.getInstance().show(getSupportFragmentManager());
            }

        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

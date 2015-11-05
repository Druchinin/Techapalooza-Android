package com.consultica.techapalooza.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.Schedule;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.ServerCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private DBMaster dbMaster;
    private boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splash_background = (ImageView) findViewById(R.id.splash_background);
        Picasso.with(this).load(R.drawable.dreamstime_xxl).fit().centerCrop().into(splash_background);

        ImageView splash_logo = (ImageView) findViewById(R.id.splash_logo);
        Picasso.with(this).load(R.drawable.dummy_art_image).into(splash_logo);

        dbMaster = DBMaster.getInstance(this);

        if (state) {
            Client.getAPI().getSchedule(new ServerCallback<Schedule.ScheduleResponse>() {
                @Override
                public void success(Schedule.ScheduleResponse response) {
                    dbMaster.clearSchedule();

                    List<Schedule> list = response.getAllSchedule();
                    for (Schedule s : list) {
                        dbMaster.insertSchedule(s);
                    }

                    List<Band> bands = dbMaster.getAllBands();
                    Log.d("Bands in DB", "Count = " + bands.size());
                    for (Band band : bands) {
                        Log.d("Bands in DB", "Id: " + band.getId()
                                        + " Name: " + band.getName()
                                        + " LogoPath: " + band.getLogo()
                                        + " Descr: " + band.getDescription()
                                        + " Date: " + band.getDate()
                        );
                    }
                }
            });

            Client.getAPI().getBandList(new ServerCallback<Band.BandResponse>() {
                @Override
                public void success(Band.BandResponse response) {
                    dbMaster.clearBands();

                    List<Band> list = response.getAllBands();
                    for (Band band : list) {
                        dbMaster.insertBand(band);
                    }

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}

package com.consultica.techapalooza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Schedule;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.ScheduleCallback;
import com.consultica.techapalooza.network.ScheduleResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private DBMaster dbMaster;
    private boolean state = false;

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
            Client.getAPI().getSchedule(new ScheduleCallback<ScheduleResponse>() {
                @Override
                public void success(ScheduleResponse response) {
                    List<Schedule> list = response.getAllSchedule();
                    for (Schedule s : list) {
                        dbMaster.insertSchedule(s.getId(), s.getStarts_at(), s.getName(), s.getAmountOfBand(), s.getBand_Id(), s.getBand_name());
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

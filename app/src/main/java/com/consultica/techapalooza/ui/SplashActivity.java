package com.consultica.techapalooza.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.News;
import com.consultica.techapalooza.model.Schedule;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.ServerCallback;
import com.nestlean.sdk.Nestlean;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends AppCompatActivity {

    private DBMaster dbMaster;
    private boolean hasNews, hasSchedule, hasBands;
    private Handler handler;
    private int attempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Nestlean.init(SplashActivity.this, "842d9562aaf24200372231e36c22e64a");

        init();
    }

    private void init() {
        initHandler();

        ImageView splash_background = (ImageView) findViewById(R.id.splash_background);
        Picasso.with(this).load(R.drawable.dreamstime_xxl).fit().centerCrop().into(splash_background);

        ImageView splash_logo = (ImageView) findViewById(R.id.splash_logo);
        Picasso.with(this).load(R.drawable.dummy_art_image).into(splash_logo);

        dbMaster = DBMaster.getInstance(this);

        if (isNetworkConnected()) {
            writeScheduleToDatabase();
            writeBandsToDatabase();
            writeNewsToDatabase();

            handler.sendEmptyMessageDelayed(1, 1000);

        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void writeNewsToDatabase() {
        Client.getAPI().getNews(new Callback<News.NewsResponse>() {
            @Override
            public void success(News.NewsResponse newsResponse, Response response) {
                dbMaster.clearNews();

                List<News> list = newsResponse.getAllNews();
                for (News news : list) {
                    dbMaster.insertNews(news);
                }

                hasNews = true;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void writeBandsToDatabase() {
        Client.getAPI().getBandList(new ServerCallback<Band.BandResponse>() {
            @Override
            public void success(Band.BandResponse response) {
                dbMaster.clearBands();

                List<Band> list = response.getAllBands();
                for (Band band : list) {
                    dbMaster.insertBand(band);
                }

                hasBands = true;
            }
        });
    }

    private void writeScheduleToDatabase() {
        Client.getAPI().getSchedule(new ServerCallback<Schedule.ScheduleResponse>() {
            @Override
            public void success(Schedule.ScheduleResponse response) {
                dbMaster.clearSchedule();

                List<Schedule> list = response.getAllSchedule();
                for (Schedule s : list) {
                    dbMaster.insertSchedule(s);
                }

                hasSchedule = true;
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                if (msg.what == 1) {
                    if (hasNews && hasSchedule && hasBands) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        if (attempts < 3) {
                            handler.sendEmptyMessageDelayed(1, 1000);
                            attempts++;
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                }
            }
        };
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}

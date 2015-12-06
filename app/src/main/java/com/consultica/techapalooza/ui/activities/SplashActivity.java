package com.consultica.techapalooza.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.News;
import com.consultica.techapalooza.model.Schedule;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.ServerCallback;
import com.consultica.techapalooza.network.SignInResponse;
import com.nestlean.sdk.Nestlean;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
            loginAndCheckTickets();

            handler.sendEmptyMessageDelayed(1, 1000);

        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void loginAndCheckTickets(){
        String email = FakeDB.getInstance(this).getEmail();
        String password = FakeDB.getInstance(this).getPassword();

        if (email != null || !email.equals("")){
            if ( password != null || !password.equals("")){

                Client.getAPI().signIn(email, password, new Callback<SignInResponse>() {
                    @Override
                    public void success(SignInResponse signInResponse, Response response) {
                        if (signInResponse.getUserId() != null){
                            
                            FakeDB.getInstance(SplashActivity.this).saveUserId(signInResponse.getUserId());

                            Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {

                                @Override
                                public void success(Ticket.TicketResponse ticketResponse, Response response) {
                                    DBMaster.getInstance(SplashActivity.this).clearTickets();

                                    boolean canRedeem = ticketResponse.canReedem();
                                    FakeDB.getInstance(SplashActivity.this).saveCanRedeem(canRedeem);

                                    for (Ticket ticket : ticketResponse.getTickets()){
                                        dbMaster.insertTicket(ticket);
                                    }

                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
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
                    Picasso.with(SplashActivity.this)
                            .load("https://techapalooza.consulti.ca" + news.getImage())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                    });
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

                    Picasso.with(SplashActivity.this)
                            .load("https://techapalooza.consulti.ca" + band.getLogo())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });

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

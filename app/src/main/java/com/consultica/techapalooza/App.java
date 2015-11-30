package com.consultica.techapalooza;

import android.app.Application;

import com.consultica.techapalooza.network.MyHttpImageLoader;
import com.consultica.techapalooza.utils.Constants;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.stripe.Stripe;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application{

    static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initCalligraphy();
        initStripe();
        initPicasso();
    }

    private void initPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new MyHttpImageLoader(this));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(false);
        Picasso.setSingletonInstance(built);
    }

    private void initStripe() {
        Stripe.apiKey = Constants.STRIPE_PUBLISHABLE_KEY;
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/PT_Sans-Narrow-Web-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}

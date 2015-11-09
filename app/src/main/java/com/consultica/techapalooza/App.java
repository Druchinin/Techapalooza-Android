package com.consultica.techapalooza;

import android.app.Application;

import com.consultica.techapalooza.utils.Constants;
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

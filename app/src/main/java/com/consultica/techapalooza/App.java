package com.consultica.techapalooza;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application{

    static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        instance = this;
    }
}

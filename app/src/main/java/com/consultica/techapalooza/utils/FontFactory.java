package com.consultica.techapalooza.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.consultica.techapalooza.App;

/**
 * Created by dimadruchinin on 29.11.15.
 */
public class FontFactory {

    public static final String FONT_CAPTURE_IT = "fonts/Capture_it.ttf";
    public static final String FONT_SANS_NARROW_WEB_REG = "fonts/PT_Sans-Narrow-Web-Regular.ttf";
    public static final String FONT_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String FONT_ROBOTO_REGULAR = "fonts/roboto-regular.ttf";
    public static final String FONT_ROBOTO_MONO_REGULAR = "fonts/RobotoMono-Regular.ttf";
    public static final String FONT_TRADE_GOTHIC_LT_CN18 = "fonts/tradegothicltstd-cn18.otf";
    public static final String FONT_TRADE_GOTHIC_LT_CN18_OBL = "fonts/tradegothicltstd-cn18obl.otf";

    private static Context context = App.getInstance();

    public static Typeface getTypeface(String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}

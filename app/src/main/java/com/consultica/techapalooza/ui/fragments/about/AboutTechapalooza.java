package com.consultica.techapalooza.ui.fragments.about;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.ui.fragments.BaseFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dimadruchinin on 21.11.15.
 */
public class AboutTechapalooza extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.about";

    private static AboutTechapalooza instance;

    public static AboutTechapalooza getInstance() {
        if (instance == null)
            instance = new AboutTechapalooza();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_techapalooza, container, false);

        WebView webView = (WebView) view.findViewById(R.id.wv_about_techapalooza);
        String text = "<html><body>" + "<p align=\"justify\">" + loadAssetTextAsString(getActivity(), "About_Techapalooza.html") + "</p>" + "</body></html>";
        webView.loadData(text, "text/html", "utf-8");
        webView.setBackgroundColor(Color.TRANSPARENT);
        final WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(18);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBlockNetworkImage(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setGeolocationEnabled(false);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSaveFormData(false);

        return view;
    }

    private String loadAssetTextAsString(Context context, String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ( (str = in.readLine()) != null ) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e("Assets read error", "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("Assets read error", "Error closing asset " + name);
                }
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return AboutTechapalooza.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.about_container;
    }
}

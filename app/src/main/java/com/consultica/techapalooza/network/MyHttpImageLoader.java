package com.consultica.techapalooza.network;

import android.content.Context;

import com.consultica.techapalooza.App;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


public class MyHttpImageLoader extends OkHttpDownloader {

    private static final String HOST_NAME = "https://techapalooza.consulti.ca";

    public MyHttpImageLoader(Context context) {
        super(getUnsafeOkHttpClient());

    }

    static OkHttpClient getUnsafeOkHttpClient() {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setCache(new Cache(App.getInstance().getCacheDir(), Integer.MAX_VALUE));
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (hostname.equals(HOST_NAME))
                        return true;
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.consultica.techapalooza.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.consultica.techapalooza.App;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.client.Header;

public class Interceptor implements RequestInterceptor {

    public static final String PREF = "Interceptor";
    public static final String COOKIE = "connect.sid";

    private String mRequestHeader;
    private String mRequestHeaderBody;
    private String cookie;
    private static Interceptor instance;

    public static Interceptor getInstance() {
        if (instance == null) {
            instance = new Interceptor();
            instance.loadCookie();
        }
        return instance;
    }

    private Interceptor() {
        mRequestHeader = "Authorization";
    }

    public void addCookie(Cookie cookie) {
        if (cookie.getName().equals("connect.sid")) {
            this.cookie = cookie.getValue();
        }
    }

    public void clearCookie() {
        SharedPreferences pref = App.getInstance().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        pref.edit().clear().apply();
        cookie =null;
    }

    @Override
    public void intercept(RequestInterceptor.RequestFacade facade) {
//        facade.addHeader(mRequestHeader, mRequestHeaderBody);
        if (cookie != null) {
            facade.addHeader("Cookie", COOKIE + "=" + cookie);
        }
    }

    private void saveCookie(String cookie) {
        SharedPreferences pref = App.getInstance().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        pref.edit().putString(COOKIE, cookie).apply();
    }

    private void loadCookie() {
        SharedPreferences pref = App.getInstance().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        cookie = pref.getString(COOKIE, null);
    }

    public void buildUserIdCookieFromString(List<Header> headers) {
        for (Header header : headers){
            if ("Set-Cookie".equals(header.getName())){
                String name = header.getValue().substring(0, header.getValue().indexOf("="));
                String value = header.getValue().substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(";", name.length()));
                BasicClientCookie cookie = new BasicClientCookie(name, value);
                addCookie(cookie);
                saveCookie(cookie.getValue());
            }
        }
    }

    public String getCookie() {
        return cookie;
    }
}

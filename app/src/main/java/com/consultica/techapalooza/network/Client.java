package com.consultica.techapalooza.network;

import android.content.Context;
import android.net.ConnectivityManager;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.BuildConfig;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.News;
import com.consultica.techapalooza.model.Schedule;
import com.consultica.techapalooza.model.Ticket;
import com.squareup.okhttp.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public class Client {

    private static final String URL = "https://techapalooza/";

    private static Client instance;
    private API api;

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public static API getAPI() {
        return getInstance().api;
    }

    private Client() {

        OkHttpClient client = new OkHttpClient();
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                if (hostname.equals("techapalooza"))
                    return true;
                return false;
            }
        });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(Interceptor.getInstance())
                .setEndpoint(URL)
                .setClient(new OkClient(client))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();

        api = restAdapter.create(API.class);
    }

    public interface API {

        //USER
        @FormUrlEncoded
        @POST("/api/users/signup")
        void signUp(
                @Field("name") String name,
                @Field("email") String email,
                @Field("password") String password,
                Callback<SignInResponse> cb);

        @FormUrlEncoded
        @POST("/api/users/signin")
        void signIn(
                @Field("email") String email,
                @Field("password") String password,
                Callback<SignInResponse> cb);

        @GET("/api/users/me")
        void getCurrentUser(Callback<SignInResponse> cb);

        @POST("/api/users/logout")
        void logOut(Callback<SignInResponse> cb);

        // SCHEDULE
        @GET("/api/schedule")
        void getSchedule(ServerCallback<Schedule.ScheduleResponse> cb);

        // BANDS
        @GET("/api/bands")
        void getBandList(Callback<Band.BandResponse> cb);

        //TICKETS
        @GET("/api/tickets/price")
        void getTicketPrice(Callback<Ticket.TicketPriceResponse> cb);

        @GET("/api/tickets")
        void getTicketsList(Callback<Ticket.TicketResponse> cb);

        @FormUrlEncoded
        @POST("/api/tickets/purchase")
        void purchaseTicket(
                @Field("band") String band,
                @Field("numberOfTickets") int number,
                @Field("token") String stripeToken,
                Callback<Ticket.TicketResponse> cb);

        @FormUrlEncoded
        @POST("/api/tickets/redeem")
        void redeemPromoCode(
                @Field("band") String band,
                @Field("code") String code,
                Callback<Ticket.TicketResponse> cb);

        @GET("/api/news")
        void getNews(Callback<News.NewsResponse> cb);
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}

package com.consultica.techapalooza.net;

import com.consultica.techapalooza.BuildConfig;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public class Client {

    private String url = "http://restcountries.eu/";

    private static Client instance;
    private API api;

    public static Client getInstance() {
        if (instance==null) {
            instance = new Client();
        }
        return instance;
    }

    public static API getAPI() {
        return getInstance().api;
    }

    private Client() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();

        api = restAdapter.create(API.class);
    }

    public interface API {

        //USER
        @POST("/api/users/signup")
        void signUp(
                @Query("email") String email,
                @Query("password") String password,
                Callback cb);

        @POST("/api/users/signin")
        void signIn(
                @Query("email") String email,
                @Query("password") String password,
                Callback cb);

        @POST("/api/users/logout")
        void logOut(Callback cb);

        // SCHEDULE
        @GET("/api/schedule")
        void getSchedule(Callback cb);

        // BANDS
        @GET("/api/bands")
        void getBandList(Callback cb);

        //TICKETS
        @GET("/api/tickets/price")
        void getTicketPrice(Callback cb);

        @GET("/api/tickets")
        void getTicketsList(Callback cb);

        @POST("/api/tickets/purchase")
        void purchaseTicket(
                @Query("band") String band,
                @Query("numberOfTickets") int number,
                @Query("token") String stripeToken,
                Callback cb);

        @POST("/api/tickets/redeem")
        void redeemPromoCode(
                @Query("code") String code,
                Callback cb);
    }
}

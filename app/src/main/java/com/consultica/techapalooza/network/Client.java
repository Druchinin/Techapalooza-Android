package com.consultica.techapalooza.network;

import com.consultica.techapalooza.BuildConfig;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public class Client {

    private String url = "https://techapalooza/";

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
                .setRequestInterceptor(Interceptor.getInstance())
                .setEndpoint(url)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();

        api = restAdapter.create(API.class);
    }

    public interface API {

        //USER
        @FormUrlEncoded
        @POST("/api/users/signup")
        void signUp(
                @Field ("name") String name,
                @Field ("email") String email,
                @Field ("password") String password,
                Callback<SignInResponse> cb);

        @FormUrlEncoded
        @POST("/api/users/signin")
        void signIn(
                @Field ("email") String email,
                @Field ("password") String password,
                Callback<SignInResponse> cb);

        @GET("/api/users/me")
        void getCurrentUser(Callback<SignInResponse> cb);

        @POST("/api/users/logout")
        void logOut(Callback cb);

        // SCHEDULE
        @GET("/api/schedule")
        void getSchedule(ScheduleCallback<ScheduleResponse> cb);

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

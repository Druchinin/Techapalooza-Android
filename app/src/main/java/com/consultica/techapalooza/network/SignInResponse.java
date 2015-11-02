package com.consultica.techapalooza.network;

public class SignInResponse {

    public Data data;

    class Data{
        User user;
    }

    class User{
        String id;
        String email;
        boolean isAdmin;
        boolean hasCustomerId;
    }

    public String getUserId(){
        return data.user.id;
    }

    public String getUserEmail(){
        return data.user.email;
    }
}

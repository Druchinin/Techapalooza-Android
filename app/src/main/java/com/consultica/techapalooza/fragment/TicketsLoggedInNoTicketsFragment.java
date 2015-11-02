package com.consultica.techapalooza.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.SignInResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsLoggedInNoTicketsFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoggedInNoTicketsFragment";

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_logged_in_no_tickets, container, false);

        Client.getAPI().getCurrentUser(new Callback<SignInResponse>() {
            @Override
            public void success(SignInResponse signInResponse, Response response) {
                Log.d("Current User", "User-Id: "+signInResponse.getUserId()+" User-Email: "+ signInResponse.getUserEmail());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Current User", "Error: "+ error.getMessage());
            }
        });

        return view;
    }
}

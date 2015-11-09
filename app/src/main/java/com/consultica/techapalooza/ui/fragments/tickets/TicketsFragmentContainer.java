package com.consultica.techapalooza.ui.fragments.tickets;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.MainActivity;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsFragmentContainer extends Fragment {

    private String currentUserEmail;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        init();

        return view;
    }

    private void init() {

        SharedPreferences pref = App.getInstance().getSharedPreferences(MainActivity.USER_PREF, Context.MODE_PRIVATE);

        if (isNetworkConnected()) {

            if (!pref.getString("email", "null").equals("null")) {

                currentUserEmail = pref.getString("email", "null");
                currentUserId = pref.getString("id", "null");

                Client.getAPI().getCurrentUser(new Callback<SignInResponse>() {
                    @Override
                    public void success(SignInResponse signInResponse, Response response) {
                        if (signInResponse.getUserId().equals(currentUserId)
                                && signInResponse.getUserEmail().equals(currentUserEmail)) {

                            startTicketsMainFragment();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            } else {
                startTicketsMainFragment();
            }

        } else {
            startTicketsMainFragment();
        }
    }

    private void startTicketsMainFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_tickets_container, new TicketsMainFragment(), TicketsMainFragment.TAG);
        transaction.commit();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}

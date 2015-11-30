package com.consultica.techapalooza.ui.fragments.tickets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.SignInResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsFragmentContainer extends Fragment {

    private String currentUserEmail;
    private String currentUserId;

    private static TicketsFragmentContainer instance;

    public static TicketsFragmentContainer getInstance() {
        if (instance == null)
            instance = new TicketsFragmentContainer();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        init();

        return view;
    }

    private void init() {

        String email = FakeDB.getInstance(getActivity()).getEmail();
        String password = FakeDB.getInstance(getActivity()).getPassword();

        Client.getAPI().signIn(email, password, new Callback<SignInResponse>() {
            @Override
            public void success(SignInResponse signInResponse, Response response) {
                if (signInResponse.getUserId() != null){
                    FakeDB.getInstance(getActivity()).saveUserId(signInResponse.getUserId());

                    if (TicketsLoggedInFragment.getInstance().hasTickets()) {
                        TicketsLoggedInFragment.getInstance().show(getFragmentManager());
                    } else {
                        TicketsLoggedInNoTicketsFragment.getInstance().show(getFragmentManager());
                    }
                } else {
                    TicketsMainFragment.getInstance().show(getFragmentManager());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                TicketsMainFragment.getInstance().show(getFragmentManager());
            }
        });

    }
}

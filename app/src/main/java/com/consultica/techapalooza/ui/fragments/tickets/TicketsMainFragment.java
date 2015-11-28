package com.consultica.techapalooza.ui.fragments.tickets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.activities.LoginActivity;
import com.consultica.techapalooza.ui.activities.MainActivity;
import com.consultica.techapalooza.ui.activities.RegistrationActivity;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsMainFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsMainFragment";

    public static final int REQUEST_LOGIN = 1;
    public static final int REQUEST_SIGN_UP = 2;

    private View view;

    private static TicketsMainFragment instance;

    public static TicketsMainFragment getInstance() {
        if (instance == null)
            instance = new TicketsMainFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_main, container, false);

        init();

        return view;
    }

    private void init() {

        String email = FakeDB.getInstance(getActivity()).getEmail();
        String password = FakeDB.getInstance(getActivity()).getPassword();


        if (!email.equals("") && !password.equals("")){

            Client.getAPI().getCurrentUser(new Callback<SignInResponse>() {
                @Override
                public void success(SignInResponse signInResponse, Response response) {
                    checkTickets();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Get user", "Fail to get user from server");
                }
            });
        }

        setupView();
    }

    private void setupView() {
        ImageView fragment_tickets_main_logo = (ImageView) view.findViewById(R.id.fragment_tickets_main_logo);
        Picasso.with(getActivity()).load(R.drawable.tickets_outline_icon).into(fragment_tickets_main_logo);

        Button btnLogin = (Button) view.findViewById(R.id.btn_tickets_sign_in);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), REQUEST_LOGIN);
            }
        });

        Button btnSignUp = (Button) view.findViewById(R.id.btn_tickets_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), RegistrationActivity.class), REQUEST_SIGN_UP);
            }
        });
    }

    private void checkTickets() {
        Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
            @Override
            public void success(Ticket.TicketResponse ticketResponse, Response response) {
                List<Ticket> list = ticketResponse.getTickets();

                if (list.size() > 0) {
                    startTicketsLoggedInFragment(list);
                } else {
                    startNoTicketsFragment();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void startNoTicketsFragment() {
        TicketsLoggedInNoTicketsFragment.getInstance().show(getActivity().getSupportFragmentManager());
    }

    private void startTicketsLoggedInFragment(List<Ticket> list) {
        TicketsLoggedInFragment.getInstance().setTickets(list);
        TicketsLoggedInFragment.getInstance().show(getFragmentManager());
    }

    @Override
    public String getName() {
        return TicketsMainFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.fragment_tickets_container;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN){
            if (resultCode == Activity.RESULT_OK){
//                TicketsLoggedInFragment.getInstance().checkTickets();
                TicketsLoggedInFragment.getInstance().show(getFragmentManager());
            }
        } else if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == Activity.RESULT_OK){
                TicketsLoggedInFragment.getInstance().checkTickets();
                TicketsLoggedInFragment.getInstance().show(getFragmentManager());
            }
        }
    }
}

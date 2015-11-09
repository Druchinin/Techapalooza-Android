package com.consultica.techapalooza.ui.fragments.tickets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsMainFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsMainFragment";

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_main, container, false);

        init();

        return view;
    }

    private void init() {
        final SharedPreferences pref = App.getInstance().getSharedPreferences(MainActivity.USER_PREF, Context.MODE_PRIVATE);

        if (!pref.getString("email", "null").equals("null")){

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
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_tickets_container, new TicketsLoginFragment(), TicketsLoginFragment.TAG);
                ft.addToBackStack(TicketsLoginFragment.TAG);
                ft.commit();
            }
        });

        Button btnSignUp = (Button) view.findViewById(R.id.btn_tickets_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_tickets_container, new RegistrationFragment(), RegistrationFragment.TAG);
                ft.addToBackStack(RegistrationFragment.TAG);
                ft.commit();
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_tickets_container, new TicketsLoggedInNoTicketsFragment(), TicketsLoggedInNoTicketsFragment.TAG);
        transaction.commit();
    }

    private void startTicketsLoggedInFragment(List<Ticket> list) {
        TicketsLoggedInFragment fragment = new TicketsLoggedInFragment();
        fragment.setTickets(list);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_tickets_container, fragment, TicketsLoggedInFragment.TAG);
        transaction.commit();
    }
}

package com.consultica.techapalooza.ui.fragments.tickets;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.activities.LoginActivity;
import com.consultica.techapalooza.ui.activities.RegistrationActivity;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.FontFactory;
import com.flurry.android.FlurryAgent;
import com.nestlean.sdk.Nestlean;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsMainFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsMainFragment";

    public static final int REQUEST_LOGIN = 1;
    public static final int REQUEST_SIGN_UP = 2;

    private View view;

    private Typeface typeface;

    private TextView tv_title_tickets_main, tv_sign_up_tickets_main;

    private static TicketsMainFragment instance;

    public static TicketsMainFragment getInstance() {
        if (instance == null)
            instance = new TicketsMainFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_main, container, false);

        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        init();

        Nestlean.event("Tickets");
        FlurryAgent.logEvent("Tickets");

        return view;
    }

    private void init() {

        tv_title_tickets_main = (TextView) view.findViewById(R.id.tv_title_tickets_main);
        tv_title_tickets_main.setTypeface(typeface);

        tv_sign_up_tickets_main = (TextView) view.findViewById(R.id.tv_sign_up_tickets_main);
        tv_sign_up_tickets_main.setTypeface(typeface);

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
        btnLogin.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), REQUEST_LOGIN);
                Nestlean.event("TicketsLogin");
                FlurryAgent.logEvent("TicketsLogin");
            }
        });

        Button btnSignUp = (Button) view.findViewById(R.id.btn_tickets_sign_up);
        btnSignUp.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), RegistrationActivity.class), REQUEST_SIGN_UP);

                Nestlean.event("TicketsSignUp");
                FlurryAgent.logEvent("TicketsSignUp");
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
                Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
                    @Override
                    public void success(Ticket.TicketResponse ticketResponse, Response response) {
                        TicketsLoggedInFragment.getInstance().setCanRedeem(ticketResponse.canReedem());
                        TicketsLoggedInFragment.getInstance().setTickets(ticketResponse.getTickets());
                        TicketsLoggedInFragment.getInstance().show(getFragmentManager());
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        } else if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == Activity.RESULT_OK){

                final String email = FakeDB.getInstance(getContext()).getEmail();
                String password = FakeDB.getInstance(getContext()).getPassword();

                Client.getAPI().signIn(email, password, new Callback<SignInResponse>() {
                    @Override
                    public void success(SignInResponse signInResponse, Response response) {
                        Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
                            @Override
                            public void success(Ticket.TicketResponse ticketResponse, Response response) {
                                FakeDB.getInstance(getContext()).saveCanRedeem(ticketResponse.canReedem());

                                TicketsLoggedInFragment.getInstance().setCanRedeem(ticketResponse.canReedem());
                                TicketsLoggedInFragment.getInstance().setTickets(ticketResponse.getTickets());
                                TicketsLoggedInFragment.getInstance().show(getFragmentManager());

                                Bundle bundle = new Bundle();
                                bundle.putString("User", email);
                                bundle.putString("TicketsCount", String.valueOf(ticketResponse.getTickets().size()));
                                Nestlean.event("TicketsPerUser", bundle);

                                Map<String, String> map = new HashMap<>();
                                map.put("User", email);
                                map.put("TicketsCount", String.valueOf(ticketResponse.getTickets().size()));
                                FlurryAgent.logEvent("TicketsPerUser", map);

                                ticketResponse.getTickets().size();
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

            }
        }
    }
}

package com.consultica.techapalooza.ui.fragments.tickets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Checkout;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.tickets.CheckoutFragment";

    private View view;

    private Button btn_frag_checkout_minus, btn_frag_checkout_plus, btn_frag_checkout;
    private TextView tv_frag_checkout_count;
    private TextView tv_frag_checkout_price;

    private Checkout checkout;
    private int oneTicketPrice, count = 1;

    private static CheckoutFragment instance;

    public static CheckoutFragment getInstance() {
        if (instance == null)
            instance = new CheckoutFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkout, container, false);

        init();

        return view;
    }

    private void init() {
        if (getArguments() != null)
            checkout = (Checkout) getArguments().getSerializable("checkout");
        else {
            checkout = new Checkout();
            checkout.setBand("null");
        }

        ImageView checkout_logo_iv = (ImageView) view.findViewById(R.id.checkout_logo_iv);
        Picasso.with(getActivity()).load(R.drawable.techapalooza_logo).into(checkout_logo_iv);

        tv_frag_checkout_count = (TextView) view.findViewById(R.id.et_frag_checkout_count);
        tv_frag_checkout_price = (TextView) view.findViewById(R.id.tv_frag_checkout_price);

        Client.getAPI().getTicketPrice(new Callback<Ticket.TicketPriceResponse>() {
            @Override
            public void success(Ticket.TicketPriceResponse ticketPriceResponse, Response response) {
                oneTicketPrice = ticketPriceResponse.getPrice();
                setupTotalPice();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        btn_frag_checkout_minus = (Button) view.findViewById(R.id.btn_frag_checkout_minus);
        btn_frag_checkout_plus = (Button) view.findViewById(R.id.btn_frag_checkout_plus);
        btn_frag_checkout = (Button) view.findViewById(R.id.btn_frag_checkout);

        setupActions();
    }

    private void setupActions() {

        btn_frag_checkout_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(tv_frag_checkout_count.getText().toString()) > 1) {
                    count--;
                    setupTotalPice();
                }
            }
        });

        btn_frag_checkout_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                setupTotalPice();
            }
        });

        btn_frag_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkout.setNumberOfTickets(count);

                Bundle bundle = new Bundle();
                bundle.putSerializable("checkout", checkout);

                CardInfoFragment cardFragment = new CardInfoFragment();
                cardFragment.setArguments(bundle);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_tickets_container, cardFragment, CardInfoFragment.TAG);
                tr.addToBackStack(CardInfoFragment.TAG);
                tr.commit();
            }
        });
    }

    private void setupTotalPice() {
        int total = count * (oneTicketPrice/100);
        tv_frag_checkout_count.setText(count + "");
        tv_frag_checkout_price.setText("$" + total);
    }
}

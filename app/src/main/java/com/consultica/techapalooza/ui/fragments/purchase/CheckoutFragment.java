package com.consultica.techapalooza.ui.fragments.purchase;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.FontFactory;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutFragment extends BaseFragment {

    private View view;

    private Button btn_frag_checkout_minus, btn_frag_checkout_plus, btn_frag_checkout;
    private TextView tv_frag_checkout_tickets, tv_frag_checkout_total, tv_frag_checkout_count, tv_frag_checkout_price;

    private Checkout checkout;
    private int oneTicketPrice, count = 1;

    private Typeface typeface;

    private static CheckoutFragment instance;

    public static CheckoutFragment getInstance() {
        if (instance == null)
            instance = new CheckoutFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkout, container, false);

        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity.getSupportActionBar() != null) {
            TextView textView = (TextView) activity.findViewById(R.id.toolbar_title_purchase);
            textView.setText("Set number of tickets");
        }

        if (checkout == null) {
            checkout = new Checkout();
            checkout.setBand("");
        }

        setupViews();

        return view;
    }

    private void setupViews() {

        ImageView checkout_logo_iv = (ImageView) view.findViewById(R.id.checkout_logo_iv);
        Picasso.with(getActivity()).load(R.drawable.techapalooza_logo).into(checkout_logo_iv);

        tv_frag_checkout_tickets = (TextView) view.findViewById(R.id.tv_frag_checkout_tickets);
        tv_frag_checkout_tickets.setTypeface(typeface);

        tv_frag_checkout_total = (TextView) view.findViewById(R.id.tv_frag_checkout_total);
        tv_frag_checkout_total.setTypeface(typeface);

        tv_frag_checkout_count = (TextView) view.findViewById(R.id.tv_frag_checkout_count);
        tv_frag_checkout_count.setTypeface(typeface);

        tv_frag_checkout_price = (TextView) view.findViewById(R.id.tv_frag_checkout_price);
        tv_frag_checkout_price.setTypeface(typeface);

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
        btn_frag_checkout.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));

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
                CardInfoFragment.getInstance().setCheckout(checkout);
                CardInfoFragment.getInstance().show(getFragmentManager(), true);

            }
        });
    }

    private void setupTotalPice() {

        try {
            int dollars = oneTicketPrice / 100;
            int cents = oneTicketPrice % 100;
            String price = dollars + "." + cents;

            if (price.indexOf(",") != -1) {
                price = price.replaceAll(",", "\\.");
            }

            double correctOneTicketPrice = Double.parseDouble(price);

            double total = count * (correctOneTicketPrice);

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(1);

            tv_frag_checkout_count.setText(count + "");
            tv_frag_checkout_price.setText("$" + nf.format(total));
            checkout.setTotalPrice(nf.format(total));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String getName() {
        return CheckoutFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.purchase_container;
    }

    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

}

package com.consultica.techapalooza.ui.fragments.tickets;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.ui.activities.PurchaseActivity;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.FontFactory;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsLoggedInNoTicketsFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoggedInNoTicketsFragment";
    private static final int REQUEST_PURCHASE = 1;

    private View view;

    private Typeface typeface;

    private TextView no_tickets_redeem_coupon;

    private static TicketsLoggedInNoTicketsFragment instance;

    public static TicketsLoggedInNoTicketsFragment getInstance() {
        if (instance == null)
            instance = new TicketsLoggedInNoTicketsFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_logged_in_no_tickets, container, false);

        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        init();

        return view;
    }

    private void init() {
        TextView tv_title_no_tickets = (TextView) view.findViewById(R.id.tv_title_no_tickets);
        tv_title_no_tickets.setTypeface(typeface);

        ImageView no_tickets_logo = (ImageView) view.findViewById(R.id.no_tickets_logo);
        Button no_tickets_purchase = (Button) view.findViewById(R.id.no_tickets_purchase);
        no_tickets_purchase.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));

        no_tickets_redeem_coupon = (TextView) view.findViewById(R.id.no_tickets_redeem_coupon);
        no_tickets_redeem_coupon.setTypeface(typeface);

        if (FakeDB.getInstance(getContext()).getCanRedeem()) {
            no_tickets_redeem_coupon.setVisibility(View.VISIBLE);
        } else {
            no_tickets_redeem_coupon.setVisibility(View.GONE);
        }

        Picasso.with(getActivity()).load(R.drawable.tickets_outline_icon).into(no_tickets_logo);

        no_tickets_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PurchaseActivity.class).putExtra("what", PurchaseActivity.WHAT_PURCHASE), REQUEST_PURCHASE);
            }
        });

        no_tickets_redeem_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PurchaseActivity.class).putExtra("what", PurchaseActivity.WHAT_REDEEM), REQUEST_PURCHASE);
            }
        });
    }

    public void setCanRedeem(boolean canReedem) {
        if (canReedem) {
            no_tickets_redeem_coupon.setVisibility(View.VISIBLE);
        } else {
            no_tickets_redeem_coupon.setVisibility(View.GONE);
        }
    }

    @Override
    public String getName() {
        return TicketsLoggedInNoTicketsFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.fragment_tickets_container;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_PURCHASE){
                Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
                    @Override
                    public void success(Ticket.TicketResponse ticketResponse, Response response) {
                        FakeDB.getInstance(getContext()).saveCanRedeem(ticketResponse.canReedem());
                        if (!ticketResponse.getTickets().isEmpty()) {
                            TicketsLoggedInFragment.getInstance().setTickets(ticketResponse.getTickets());
                            TicketsLoggedInFragment.getInstance().show(getFragmentManager());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }
}

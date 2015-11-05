package com.consultica.techapalooza.ui.fragments.tickets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.SignInResponse;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TicketsLoggedInNoTicketsFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoggedInNoTicketsFragment";

    private View view;
    private Button no_tickets_purchase;
    private TextView no_tickets_redeem_coupon;
    private ImageView no_tickets_logo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_logged_in_no_tickets, container, false);

        no_tickets_logo = (ImageView) view.findViewById(R.id.no_tickets_logo);

        Picasso.with(getActivity()).load(R.drawable.tickets_outline_icon).into(no_tickets_logo);

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

        no_tickets_purchase = (Button) view.findViewById(R.id.no_tickets_purchase);
        no_tickets_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_tickets_container, new BandListFragment(), BandListFragment.TAG);
                tr.addToBackStack(BandListFragment.TAG);
                tr.commit();
            }
        });

        no_tickets_redeem_coupon = (TextView) view.findViewById(R.id.no_tickets_redeem_coupon);
        no_tickets_redeem_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}

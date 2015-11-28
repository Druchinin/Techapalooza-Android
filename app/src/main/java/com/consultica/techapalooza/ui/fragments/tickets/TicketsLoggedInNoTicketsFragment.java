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
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.squareup.picasso.Picasso;

public class TicketsLoggedInNoTicketsFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoggedInNoTicketsFragment";

    private View view;

    private static TicketsLoggedInNoTicketsFragment instance;

    public static TicketsLoggedInNoTicketsFragment getInstance() {
        if (instance == null)
            instance = new TicketsLoggedInNoTicketsFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_logged_in_no_tickets, container, false);

        init();

        return view;
    }

    private void init() {
        ImageView no_tickets_logo = (ImageView) view.findViewById(R.id.no_tickets_logo);
        Button no_tickets_purchase = (Button) view.findViewById(R.id.no_tickets_purchase);
        TextView no_tickets_redeem_coupon = (TextView) view.findViewById(R.id.no_tickets_redeem_coupon);

        Picasso.with(getActivity()).load(R.drawable.tickets_outline_icon).into(no_tickets_logo);

        no_tickets_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(BandListFragment.BUNDLE_FROM, R.id.tv_tickets_purchase_more);

                startBandList(bundle);
            }
        });

        no_tickets_redeem_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(BandListFragment.BUNDLE_FROM, R.id.tv_tickets_redeem);

                startBandList(bundle);
            }
        });
    }

    private void startBandList(Bundle bundle) {
        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        BandListFragment fragment = new BandListFragment();
        fragment.setArguments(bundle);

        tr.replace(R.id.fragment_tickets_container, fragment, BandListFragment.TAG);
        tr.addToBackStack(BandListFragment.TAG);
        tr.commit();
    }

    @Override
    public String getName() {
        return TicketsLoggedInNoTicketsFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.fragment_tickets_container;
    }
}

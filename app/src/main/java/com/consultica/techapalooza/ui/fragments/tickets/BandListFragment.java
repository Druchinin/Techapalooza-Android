package com.consultica.techapalooza.ui.fragments.tickets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.BandListAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.Checkout;
import com.consultica.techapalooza.ui.fragments.BaseFragment;

import java.util.List;

public class BandListFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.tickets.BandListFragment";
    public static final String BUNDLE_FROM = "Button";

    private View view;

    private DBMaster dbMaster;
    private BandListAdapter adapter;
    int buttonFrom;

    private static BandListFragment instance;

    public static BandListFragment getInstance() {
        if (instance == null)
            instance = new BandListFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_band_list, container, false);

        setup();

        return view;
    }

    private void setup() {

        if (!getArguments().isEmpty()){
            if (getArguments().getInt(BUNDLE_FROM) != 0){
                buttonFrom = getArguments().getInt(BUNDLE_FROM);
            }
        }

        dbMaster = DBMaster.getInstance(getActivity());

        setupRecycleView();
    }

    private void setupRecycleView() {

        List<Band> data = dbMaster.getAllBands();

        adapter = new BandListAdapter(getActivity(), data);
        adapter.setOnItemClickListener(new BandListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Band band = adapter.getItem(position);
                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();

                if (buttonFrom == R.id.tv_tickets_purchase_more) {
                    CheckoutFragment fragment = new CheckoutFragment();
                    Checkout checkout = new Checkout();
                    checkout.setBand(band.getId());
                    bundle.putSerializable("checkout", checkout);

                    fragment.setArguments(bundle);
                    tr.replace(R.id.fragment_tickets_container, fragment, CheckoutFragment.TAG);
                    tr.addToBackStack(CheckoutFragment.TAG);

                } else if (buttonFrom == R.id.tv_tickets_redeem){
                    bundle.putString("band", band.getId());

                    RedeemFragment fragment = new RedeemFragment();
                    fragment.setArguments(bundle);
                    tr.replace(R.id.fragment_tickets_container, fragment, RedeemFragment.TAG);
                    tr.addToBackStack(RedeemFragment.TAG);
                }

                tr.commit();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.band_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }



    @Override
    public String getName() {
        return BandListFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.fragment_tickets_container;
    }
}

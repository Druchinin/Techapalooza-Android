package com.consultica.techapalooza.ui.fragments.purchase;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.BandListAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.model.Checkout;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.FontFactory;

import java.util.List;

public class BandListFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.purchase.BandListFragment";

    private View view;

    private DBMaster dbMaster;
    private BandListAdapter adapter;
    int buttonFrom;

    private Band band;

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

        dbMaster = DBMaster.getInstance(getActivity());

        setupRecycleView();
    }

    private void setupRecycleView() {

        List<Band> data = dbMaster.getAllBands();

        Button btn_frag_band_list = (Button) view.findViewById(R.id.btn_frag_band_list);
        btn_frag_band_list.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));
        btn_frag_band_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();
                checkout.setBand("");

                CheckoutFragment.getInstance().setCheckout(checkout);
                CheckoutFragment.getInstance().show(getFragmentManager(), true);
            }
        });

        adapter = new BandListAdapter(getActivity(), data);
        adapter.setOnItemClickListener(new BandListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                band = adapter.getItem(position);
                Checkout checkout = new Checkout();
                checkout.setBand(band.getId());
                CheckoutFragment.getInstance().setCheckout(checkout);
                CheckoutFragment.getInstance().show(getFragmentManager(), true);
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
        return R.id.purchase_container;
    }
}

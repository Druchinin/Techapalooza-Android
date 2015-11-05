package com.consultica.techapalooza.ui.fragments.lineup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.LineUpGalleryAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;

import java.util.List;

public class LineUpGalleryFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.LineUpGalleryFragment";
    private View view;
    private LineUpGalleryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_up_galery, container, false);

        List<Band> data = DBMaster.getInstance(getActivity()).getAllBands();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_line_up_gallery);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new LineUpGalleryAdapter(getActivity(), data);
        adapter.setOnItemClickListener(new LineUpGalleryAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Band band = adapter.getItem(position);

                BandDetailsFragment fragment = new BandDetailsFragment();
                fragment.setBand(band);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.line_up_fragment_container, fragment, BandDetailsFragment.TAG);
                tr.addToBackStack(BandDetailsFragment.TAG);
                tr.commit();
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}

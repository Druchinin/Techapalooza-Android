package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapter.LineUpGalleryAdapter;
import com.consultica.techapalooza.model.Band;

import java.util.ArrayList;
import java.util.List;

public class LineUpGalleryFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.LineUpGalleryFragment";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_up_galery, container, false);

        List<Band> data = new ArrayList<>();
        for (int i = 0; i<10; i++){
            Band band = new Band();
            band.imagePath = "http://techapalooza.ca/wp/wp-content/uploads/2015/10/Bill_1.jpg";
            data.add(band);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_line_up_gallery);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        LineUpGalleryAdapter adapter = new LineUpGalleryAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

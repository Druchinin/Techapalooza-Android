package com.consultica.techapalooza.ui.fragments.lineup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.LineUpGalleryAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.ui.activities.BandDetailsActivity;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.flurry.android.FlurryAgent;
import com.nestlean.sdk.Nestlean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineUpGalleryFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.LineUpGalleryFragment";
    private View view;
    private LineUpGalleryAdapter adapter;
    private List<Band> data;

    private static LineUpGalleryFragment instance;

    public static LineUpGalleryFragment getInstance() {
        if (instance == null)
            instance = new LineUpGalleryFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_up_galery, container, false);

        setup();

        return view;
    }

    private void setup() {
        data = DBMaster.getInstance(getActivity()).getAllBands();

        if (data.size() > 0) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_line_up_gallery);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            adapter = new LineUpGalleryAdapter(getActivity(), data);
            adapter.setOnItemClickListener(new LineUpGalleryAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                    Band band = adapter.getItem(position);

                    if (!hasProblems(band)) {
                        startActivity(new Intent(getActivity(), BandDetailsActivity.class).putExtra("band", band));
                        Bundle bundle = new Bundle();
                        bundle.putString("band", band.getName());
                        Nestlean.event("LineUpBandClick", bundle);

                        Map<String, String> map = new HashMap<>();
                        map.put("band", band.getName());
                        FlurryAgent.logEvent("LineUpBandClick", map);
                    } else {
                        Snackbar.make(view, "Can't open details.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            recyclerView.setAdapter(adapter);

            Nestlean.event("LineUp");
            FlurryAgent.logEvent("LineUp");

        }
    }

    @Override
    public String getName() {
        return LineUpGalleryFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.line_up_fragment_container;
    }

    private boolean hasProblems(Band band){
        boolean problem = false;

        if (band == null)
            problem = true;
        else if (band.getName() == null || band.getName().equals(""))
            problem = true;
        else if (band.getDate() == null || band.getDate().equals(""))
            problem = true;
        else if (band.getDescription() == null || band.getDescription().equals(""))
            problem = true;

        return problem;
    }
}

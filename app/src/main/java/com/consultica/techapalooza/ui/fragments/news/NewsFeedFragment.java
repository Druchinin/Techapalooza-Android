package com.consultica.techapalooza.ui.fragments.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.NewsRecycleViewAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.model.News;
import com.flurry.android.FlurryAgent;
import com.nestlean.sdk.Nestlean;

import java.util.List;

public class NewsFeedFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.NewsFeedFragment";

    private View view;
    private RecyclerView recyclerView;
    private NewsRecycleViewAdapter adapter;
    private DBMaster dbMaster;

    private static NewsFeedFragment instance;

    public static NewsFeedFragment getInstance() {
        if (instance == null)
            instance = new NewsFeedFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        setup();

        return view;
    }

    private void setup() {
        dbMaster = DBMaster.getInstance(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.art_recycle_view);

        List<News> data = dbMaster.getAllNews();

        if (data.size() > 0) {

            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
            adapter = new NewsRecycleViewAdapter(getActivity(), data);
            recyclerView.setAdapter(adapter);


            Nestlean.event("News Feed");
            FlurryAgent.logEvent("News Feed");

        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

}

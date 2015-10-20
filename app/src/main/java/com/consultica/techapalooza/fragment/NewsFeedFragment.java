package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapter.ArticleRecycleViewAdapter;
import com.consultica.techapalooza.model.Article;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.NewsFeedFragment";

    private View view;
    private RecyclerView recyclerView;
    private ArticleRecycleViewAdapter adapter;

    public static NewsFeedFragment getInstance(){
        Bundle args = new Bundle();
        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.art_recycle_view);
        adapter = new ArticleRecycleViewAdapter(getActivity(), getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static List<Article> getData(){
        List<Article> data = new ArrayList<>();
        String title = "Bill and Davey-O’s Rockabilly Boys Win Judges Choice";
        String date = "March 5, 2015";
        String image = "";
        String text = "Bill and Davey-O’s Rockabilly Boys won the Judge’s Choice award for 2015. It was presented by Alf Dyck of Price Industries (Alf is behind Tom McGouran).\n" +
                "L to R – Alf Dyck (hidden), Price Industries, Tom Gouran, MC, Neil Sinnott, CompuGen, Napoleon Sansregret, HP, Brad Enns, CompuGen, Thomas Wolstencroft, CompuGen (not appearing from the band, Erik and Tom Sinnott)";

        for (int i = 0; i<5; i++){
            Article article = new Article();
            article.title = title;
            article.date = date;
            article.imageId = image;
            article.text = text;

            data.add(article);
        }

        return data;
    }
}

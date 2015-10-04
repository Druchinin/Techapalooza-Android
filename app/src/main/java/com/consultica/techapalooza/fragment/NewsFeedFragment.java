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
        String title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        String date = "March 5, 2015";
        int image = R.drawable.dummy_art_image;
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer malesuada, libero sodales malesuada scelerisque, purus elit luctus orci, sed consequat erat nisi efficitur purus. Sed pretium semper magna. Sed ac est non turpis condimentum scelerisque. Duis tincidunt lectus ut augue laoreet porttitor ut quis nisi. Maecenas vestibulum velit non elit rhoncus, at rutrum quam tincidunt. Suspendisse potenti. Nam egestas lacus in porttitor sagittis. Proin cursus pulvinar libero, vitae elementum urna eleifend consectetur. Donec pharetra nisl sem, sed ornare arcu faucibus id. Nullam ac est auctor, imperdiet tellus in, congue felis. In efficitur molestie ex, ut tempor ipsum viverra et.";

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

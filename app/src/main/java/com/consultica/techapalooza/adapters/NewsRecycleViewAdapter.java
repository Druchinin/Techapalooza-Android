package com.consultica.techapalooza.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.News;
import com.consultica.techapalooza.network.MyHttpImageLoader;
import com.consultica.techapalooza.utils.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<News> data = Collections.emptyList();

    public NewsRecycleViewAdapter(Context context, List<News> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_article_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.date.setText(current.getDate());
        holder.text.setText(current.getContent());


        Picasso.with(App.getInstance())
                .load("https://techapalooza.consulti.ca" + current.getImage())
                .fit()
                .centerInside()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private ImageView image;
        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.art_tv_title);
            date = (TextView) itemView.findViewById(R.id.art_tv_date);
            image = (ImageView) itemView.findViewById(R.id.art_iv_image);
            text = (TextView) itemView.findViewById(R.id.art_tv_text);
        }
    }

}

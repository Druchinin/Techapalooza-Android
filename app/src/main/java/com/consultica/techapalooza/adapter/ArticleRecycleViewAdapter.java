package com.consultica.techapalooza.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Article;

import java.util.Collections;
import java.util.List;

public class ArticleRecycleViewAdapter extends RecyclerView.Adapter<ArticleRecycleViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Article> data = Collections.emptyList();

    public ArticleRecycleViewAdapter(Context context, List<Article> data) {
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
        Article current = data.get(position);
        holder.title.setText(current.title);
        holder.date.setText(current.date);
        holder.image.setImageResource(current.imageId);
        holder.text.setText(current.text);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        private ImageView image;
        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            Typeface titleTypeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/PT_Sans-Narrow-Web-Regular.ttf");
            Typeface textTypeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Roboto-Light.ttf");

            title = (TextView) itemView.findViewById(R.id.art_tv_title);
            title.setTypeface(titleTypeface);
            date = (TextView) itemView.findViewById(R.id.art_tv_date);
            date.setTypeface(titleTypeface);
            image = (ImageView) itemView.findViewById(R.id.art_iv_image);
            text = (TextView) itemView.findViewById(R.id.art_tv_text);
            text.setTypeface(textTypeface);
        }
    }
}

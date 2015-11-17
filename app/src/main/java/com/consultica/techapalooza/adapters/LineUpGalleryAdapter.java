package com.consultica.techapalooza.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.network.MyHttpImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class LineUpGalleryAdapter extends RecyclerView.Adapter<LineUpGalleryAdapter.ViewHolder> {

    public static final String URL = "https://techapalooza";

    private LayoutInflater inflater;
    private List<Band> data = Collections.emptyList();

    private static ClickListener clickListener;

    public LineUpGalleryAdapter(Context context, List<Band> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public LineUpGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_line_up_image_view, parent, false);
        LineUpGalleryAdapter.ViewHolder viewHolder = new LineUpGalleryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LineUpGalleryAdapter.ViewHolder holder, int position) {
        Band current = data.get(position);

        Picasso picasso;
        Picasso.Builder builder= new Picasso.Builder(App.getInstance());
        picasso = builder.downloader(new MyHttpImageLoader(App.getInstance())).build();

        picasso.load(URL + current.getLogo())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Band getItem(int position){
        return data.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.line_up_item_image_view);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        LineUpGalleryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}

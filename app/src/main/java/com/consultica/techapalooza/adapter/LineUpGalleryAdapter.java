package com.consultica.techapalooza.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Band;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class LineUpGalleryAdapter extends RecyclerView.Adapter<LineUpGalleryAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Band> data = Collections.emptyList();

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

        Picasso picasso = Picasso.with(App.getInstance());
        picasso.setDebugging(true);
        picasso.load(current.imagePath)
                .resizeDimen(R.dimen.grid_image_item_size, R.dimen.grid_image_item_size)
                .centerInside()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.line_up_item_image_view);
        }
    }
}

package com.consultica.techapalooza.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Band;
import com.consultica.techapalooza.utils.FontFactory;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class BandListAdapter extends RecyclerView.Adapter<BandListAdapter.ViewHolder> {

    public static final String URL = "https://techapalooza.consulti.ca";

    private LayoutInflater inflater;
    private List<Band> data = Collections.emptyList();
    private static ClickListener clickListener;

    public BandListAdapter(Context context, List<Band> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_band_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Band band = data.get(position);


        Picasso.with(App.getInstance())
                .load(URL + band.getLogo())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.band_list_logo_iv);

        holder.band_list_title_tv.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Band getItem(int postition) {
        return data.get(postition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView band_list_logo_iv;
        private TextView band_list_title_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            band_list_logo_iv = (ImageView) itemView.findViewById(R.id.band_list_logo_iv);

            band_list_title_tv = (TextView) itemView.findViewById(R.id.band_list_title_tv);
            band_list_title_tv.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        BandListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

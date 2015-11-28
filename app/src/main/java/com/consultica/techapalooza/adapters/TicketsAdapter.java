package com.consultica.techapalooza.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Ticket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;


public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Ticket> data = Collections.emptyList();

    public TicketsAdapter(Context context, List<Ticket> data) {
        inflater = LayoutInflater.from(App.getInstance());
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_ticket_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        File cacheDir = App.getInstance().getCacheDir();
        File f;
        FileInputStream fis = null;
        Bitmap bitmap;

        f = new File(cacheDir, getItem(position).getCode());
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap= BitmapFactory.decodeStream(fis);

        if (bitmap != null)
            holder.ticket_qr_code.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Ticket getItem(int postition) {
        return data.get(postition);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ticket_qr_code;
        ImageView ticket_background_image;
        TextView ticket_lable_techapalooza, ticket_lable_2016;


        public ViewHolder(View itemView) {
            super(itemView);

            Typeface myTypeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Capture_it.ttf");


            ticket_background_image = (ImageView) itemView.findViewById(R.id.ticket_background_image);
            ticket_qr_code = (ImageView) itemView.findViewById(R.id.ticket_qr_code);

            ticket_lable_techapalooza = (TextView) itemView.findViewById(R.id.ticket_lable_techapalooza);
            ticket_lable_techapalooza.setTypeface(myTypeface);

            ticket_lable_2016 = (TextView) itemView.findViewById(R.id.ticket_lable_2016);
            ticket_lable_2016.setTypeface(myTypeface);

        }
    }
}
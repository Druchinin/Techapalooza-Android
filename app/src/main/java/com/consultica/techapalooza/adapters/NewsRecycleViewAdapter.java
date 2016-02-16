package com.consultica.techapalooza.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.News;
import com.consultica.techapalooza.utils.FontFactory;
import com.flurry.android.FlurryAgent;
import com.nestlean.sdk.Nestlean;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<News> data = Collections.emptyList();
    private Context context;

    String tempUrl;

    public NewsRecycleViewAdapter(Context context, List<News> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
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
        final News current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.date.setText(current.getDate());
        holder.text.setText(current.getContent());


        Picasso.with(App.getInstance())
                .load("https://techapalooza.consulti.ca" + current.getImage())
                .fit()
                .centerInside()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.image);

        String url = current.getUrl();

        if (url != null && !url.equals("")){
            tempUrl = url;
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tempUrl));
                    context.startActivity(browserIntent);

                    Bundle bundle = new Bundle();
                    bundle.putString("web", tempUrl);
                    Nestlean.event("web", bundle);

                    Map<String, String> map = new HashMap<>();
                    map.put("web", tempUrl);
                    FlurryAgent.logEvent("web", map);
                }
            });
        } else {
            holder.btn.setVisibility(View.GONE);
        }

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
        private Button btn;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.art_tv_title);
            title.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

            date = (TextView) itemView.findViewById(R.id.art_tv_date);
            date.setTypeface(FontFactory.getTypeface(FontFactory.FONT_TRADE_GOTHIC_LT_CN18));

            image = (ImageView) itemView.findViewById(R.id.art_iv_image);

            text = (TextView) itemView.findViewById(R.id.art_tv_text);
            text.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_LIGHT));

            btn = (Button) itemView.findViewById(R.id.art_btn_learn_more);
            btn.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));
        }
    }

}

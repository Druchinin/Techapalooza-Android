package com.consultica.techapalooza.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Band;

import java.util.Collections;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Band> data = Collections.emptyList();

    public ScheduleListAdapter(Context context, List<Band> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_schedule_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Band current = data.get(position);
        holder.hour.setText(current.hour);
        holder.minutes.setText(current.minutes);
        holder.moon.setText(current.moon);
        holder.line.setBackgroundResource(R.color.vertLineIndicatorNormal);
        holder.title.setText(current.name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Band getItem(int postition){
        return data.get(postition);
    }

    public void changeVerticalIndicatorState(int position, boolean state){
        if (state) {
            data.get(position).vertLineIndicator.setBackgroundResource(R.color.vertLineIndicatorActive);
        } else {
            data.get(position).vertLineIndicator.setBackgroundResource(R.color.vertLineIndicatorNormal);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView hour;
        private TextView minutes;
        private TextView moon;
        private FrameLayout line;
        private TextView title;
        private ImageView arrow;

        public ViewHolder(View itemView) {
            super(itemView);

            Typeface myTypeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/PT_Sans-Narrow-Web-Regular.ttf");

            hour = (TextView) itemView.findViewById(R.id.sched_list_item_hour);
            hour.setTypeface(myTypeface);
            minutes = (TextView) itemView.findViewById(R.id.sched_list_item_min);
            minutes.setTypeface(myTypeface);
            moon = (TextView) itemView.findViewById(R.id.sched_list_item_moon);
            moon.setTypeface(myTypeface);
            line = (FrameLayout) itemView.findViewById(R.id.sched_list_vert_line);
            title = (TextView) itemView.findViewById(R.id.sched_list_item_title);
            title.setTypeface(myTypeface);
        }
    }

}

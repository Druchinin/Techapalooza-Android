package com.consultica.techapalooza.adapters;

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
import com.consultica.techapalooza.model.Schedule;

import java.util.Collections;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Schedule> data = Collections.emptyList();
    private static ClickListener clickListener;
    private int currItemIndicator = -1;

    public ScheduleListAdapter(Context context, List<Schedule> data) {
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
        Schedule current = data.get(position);
        String min = String.valueOf(current.getMinutes());
        if (min.length() == 1) min = "0" + min;

        holder.hour.setText(String.valueOf(current.getHours()));
        holder.minutes.setText(min);
        holder.moon.setText(current.getAM_PM());

        if (current.getBand_Id() != null){
            holder.arrow.setVisibility(View.VISIBLE);
        } else {
            holder.arrow.setVisibility(View.INVISIBLE);
        }

        if (current.isNow())
            holder.line.setBackgroundResource(R.color.vertLineIndicatorActive);
        else
            holder.line.setBackgroundResource(R.color.vertLineIndicatorNormal);

        if (!current.getBand_name().equals(""))
            holder.title.setText(current.getBand_name());
        else holder.title.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Schedule getItem(int postition) {
        return data.get(postition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView hour;
        private TextView minutes;
        private TextView moon;
        private FrameLayout line;
        private TextView title;
        private ImageView arrow;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

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

            arrow = (ImageView) itemView.findViewById(R.id.sched_list_item_arrow);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ScheduleListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

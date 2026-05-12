package com.example.proiect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class VideoFeedAdapter extends ArrayAdapter<VideoFeed> {

    public VideoFeedAdapter(Context context, List<VideoFeed> feeds) {
        super(context, 0, feeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_video_feed, parent, false);
        }
        VideoFeed feed = getItem(position);

        TextView tvNume = convertView.findViewById(R.id.tvNumeFeed);
        TextView tvZona = convertView.findViewById(R.id.tvZonaFeed);
        TextView tvStatus = convertView.findViewById(R.id.tvStatusFeed);

        tvNume.setText(feed.nume);
        tvZona.setText(feed.zona);
        tvStatus.setText(feed.status);

        if ("LIVE".equals(feed.status)) {
            tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            tvStatus.setTextColor(Color.GRAY);
        }

        return convertView;
    }
}

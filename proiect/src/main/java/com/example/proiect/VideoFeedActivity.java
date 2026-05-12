package com.example.proiect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_feed);

        GridView gridView = findViewById(R.id.gridViewFeeds);

        List<VideoFeed> feeds = new ArrayList<>(Arrays.asList(
                new VideoFeed("FATA", "Intrare Principală", "LIVE", 44.4268, 26.1025,
                        "https://www.youtube.com/watch?v=rnXIjl_Rzy4"),
                new VideoFeed("LATERAL1", "Flancul Stâng", "LIVE", 44.4272, 26.1018,
                        "https://www.youtube.com/watch?v=83VPsAPWiME"),
                new VideoFeed("SPATE", "Ieșire Spate", "LIVE", 44.4261, 26.1031,
                        "https://www.youtube.com/watch?v=wUQc3RoLAPs"),
                new VideoFeed("LATERAL2", "Flancul Drept", "OFFLINE", 44.4265, 26.1038,
                        "https://www.youtube.com/watch?v=qBdFrNyaomo")
        ));

        VideoFeedAdapter adapter = new VideoFeedAdapter(this, feeds);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            VideoFeed feed = feeds.get(position);
            if ("OFFLINE".equals(feed.status)) {
                Toast.makeText(this, feed.nume + " — cameră offline", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feed.url)));
        });
    }
}

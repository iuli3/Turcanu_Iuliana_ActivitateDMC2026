package com.example.laborator9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhoneAdapter extends BaseAdapter {
    private Context context;
    private List<PhoneImage> phoneImages;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public PhoneAdapter(Context context, List<PhoneImage> phoneImages) {
        this.context = context;
        this.phoneImages = phoneImages;
    }

    @Override
    public int getCount() { return phoneImages.size(); }

    @Override
    public Object getItem(int position) { return phoneImages.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_phone, parent, false);
        }

        PhoneImage currentPhone = phoneImages.get(position);
        ImageView ivPhone = convertView.findViewById(R.id.ivPhone);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);

        tvDescription.setText(currentPhone.getDescription());

        if (currentPhone.getBitmap() != null) {
            ivPhone.setImageBitmap(currentPhone.getBitmap());
        } else {
            ivPhone.setImageResource(android.R.drawable.ic_menu_report_image);
            executorService.execute(() -> {
                try {
                    InputStream in = new URL(currentPhone.getImageUrl()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    currentPhone.setBitmap(bitmap);
                    mainHandler.post(this::notifyDataSetChanged);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return convertView;
    }
}

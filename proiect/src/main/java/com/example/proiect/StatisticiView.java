package com.example.proiect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

public class StatisticiView extends View {

    private Map<String, Integer> date;
    private String titlu = "Statistici";
    private final Paint paintBar = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintValoare = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintTitlu = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int[] culori = {
            Color.parseColor("#2196F3"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#FF9800"),
            Color.parseColor("#E91E63")
    };

    public StatisticiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintLabel.setColor(Color.DKGRAY);
        paintLabel.setTextSize(28f);
        paintLabel.setTextAlign(Paint.Align.CENTER);
        paintValoare.setColor(Color.BLACK);
        paintValoare.setTextSize(30f);
        paintValoare.setTextAlign(Paint.Align.CENTER);
        paintValoare.setFakeBoldText(true);
        paintTitlu.setColor(Color.parseColor("#1565C0"));
        paintTitlu.setTextSize(36f);
        paintTitlu.setFakeBoldText(true);
    }

    public void setDate(Map<String, Integer> date) {
        this.date = date;
        invalidate();
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (date == null || date.isEmpty() || getWidth() == 0) return;

        int width = getWidth();
        int height = getHeight();
        int paddingH = 60;
        int paddingBottom = 80;
        int paddingTop = 80;
        int n = date.size();
        int barWidth = (width - 2 * paddingH) / n;

        int maxVal = 1;
        for (int v : date.values()) if (v > maxVal) maxVal = v;

        canvas.drawText(titlu, width / 2f, 55, paintTitlu);

        int i = 0;
        for (Map.Entry<String, Integer> entry : date.entrySet()) {
            paintBar.setColor(culori[i % culori.length]);

            float chartH = height - paddingTop - paddingBottom;
            float barH = (float) entry.getValue() / maxVal * chartH;
            float cx = paddingH + i * barWidth + barWidth / 2f;
            float left = cx - barWidth * 0.35f;
            float right = cx + barWidth * 0.35f;
            float top = height - paddingBottom - barH;
            float bottom = height - paddingBottom;

            canvas.drawRect(left, top, right, bottom, paintBar);
            canvas.drawText(String.valueOf(entry.getValue()), cx, top - 8, paintValoare);
            canvas.drawText(entry.getKey(), cx, height - paddingBottom + 45, paintLabel);

            i++;
        }
    }
}

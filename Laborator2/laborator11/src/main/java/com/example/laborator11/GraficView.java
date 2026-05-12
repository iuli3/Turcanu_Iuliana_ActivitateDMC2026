package com.example.laborator11;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class GraficView extends View {

    private float[] valori;
    private String[] etichete;
    private String tip = "ColumnChart";

    private final Paint paintBar   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintText  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintValue = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintAxis  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintLegend = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final int[] COLORS = {
        0xFF2196F3, 0xFF4CAF50, 0xFFFF9800, 0xFFE91E63,
        0xFF9C27B0, 0xFF00BCD4, 0xFFFF5722, 0xFF607D8B,
        0xFF8BC34A, 0xFFFFEB3B
    };

    public GraficView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintAxis.setColor(0xFFBDBDBD);
        paintAxis.setStrokeWidth(2f);
        paintText.setColor(Color.DKGRAY);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintValue.setFakeBoldText(true);
        paintValue.setTextAlign(Paint.Align.CENTER);
        paintLegend.setTextAlign(Paint.Align.LEFT);
    }

    public void setDate(float[] valori, String[] etichete, String tip) {
        this.valori   = valori;
        this.etichete = etichete;
        this.tip      = tip;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (valori == null || valori.length == 0 || getWidth() == 0) return;
        canvas.drawColor(Color.WHITE);
        switch (tip) {
            case "ColumnChart": drawColumnChart(canvas); break;
            case "BarChart":    drawBarChart(canvas);    break;
            case "PieChart":    drawPieChart(canvas);    break;
        }
    }


    private void drawColumnChart(Canvas canvas) {
        float startP = 100;
        float latimeColoana = 80;
        float bottom = 800;
        float maxVal = maxVal();
        float scara = 500;

        for (int i = 0; i < valori.length; i++) {
            int red = ((i + 2) * 23) % 256;
            paintBar.setColor(Color.rgb(red, 100, 200));

            // inaltime
            float inaltime = (valori[i] / maxVal) * scara;
            float top = bottom - inaltime;

            // col
            canvas.drawRect(startP, top, startP + latimeColoana, bottom, paintBar);

            paintValue.setTextSize(26f);
            canvas.drawText(formatVal(valori[i]), startP + latimeColoana / 2, top - 10, paintValue);

            // Afisare Eticheta
            paintText.setTextSize(26f);
            canvas.drawText(etichete[i], startP + latimeColoana / 2, bottom + 40, paintText);

            // pozitie
            startP += latimeColoana * 2;
        }
    }
    private void drawBarChart(Canvas canvas) {
        float startY = 100;
        float grosimeBara = 60;
        float padL = 220;
        float maxVal = maxVal();
        float latimeMaxima = getWidth() - padL - 100;

        for (int i = 0; i < valori.length; i++) {
            paintBar.setColor(COLORS[i % COLORS.length]);
            float lungimeBara = (valori[i] / maxVal) * latimeMaxima;

            // bara
            canvas.drawRect(padL, startY, padL + lungimeBara, startY + grosimeBara, paintBar);

            ;paintText.setTextAlign(Paint.Align.RIGHT)
            paintText.setTextSize(30f);
            canvas.drawText(etichete[i], padL - 20, startY + grosimeBara / 2 + 10, paintText);

            paintValue.setTextAlign(Paint.Align.LEFT);
            paintValue.setTextSize(26f);
            canvas.drawText(formatVal(valori[i]), padL + lungimeBara + 10, startY + grosimeBara / 2 + 10, paintValue);

            startY += grosimeBara * 2; // spatiu bare
        }
    }

    private void drawPieChart(Canvas canvas) {
        float total = 0;
        for (float v : valori) total += v;
        if (total == 0) return;

        float padding = 150;
        // cerc
        RectF rect = new RectF(padding, padding, getWidth() - padding, getWidth() - padding);
        float startAngle = 0;

        for (int i = 0; i < valori.length; i++) {
            paintBar.setColor(COLORS[i % COLORS.length]);
            float sweepAngle = (valori[i] / total) * 360;

            // felie
            canvas.drawArc(rect, startAngle, sweepAngle, true, paintBar);

            // legenda
            float ly = getWidth() + (i * 60); //vertical sub cerc
            canvas.drawRect(padding, ly, padding + 40, ly + 40, paintBar);

            paintLegend.setTextSize(30f);
            canvas.drawText(etichete[i] + ": " + formatVal(valori[i]), padding + 60, ly + 32, paintLegend);

            startAngle += sweepAngle;
        }
    }

    private float maxVal() {
        float m = 1;
        for (float v : valori) if (v > m) m = v;
        return m;
    }

    private String formatVal(float v) {
        return v == (int) v ? String.valueOf((int) v) : String.format("%.1f", v);
    }
}

package com.example.tobiasz.projekt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawingView2 extends View {
    public int width;
    public int height;
    public int x;
    public int y;
    private Paint canvasPaint;
    private Bitmap canvasBitmap;
    public Canvas canvas;
    public String et;
    public Paint paint;
    public Context con;

    public DrawingView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        width = 0;
        height = 0;
        x = 0;
        y = 0;
        et = "";
        paint = new Paint();
        setupDrawing();
    }

    private void setupDrawing() {
        canvasPaint = new Paint(4);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        canvasBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        int place = 60;
        paint.setColor(Color.WHITE);
        paint.setTextSize((float)place);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.canvasBitmap, 0.0F, 0.0F, this.canvasPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch(event.getAction()) {
            case 0:
                if(!this.et.equals("")) {
                    x = (int)touchX;
                    y = (int)touchY;
                    Bitmap drawableBitmap = canvasBitmap.copy(Config.ARGB_8888, true);
                    canvas = new Canvas(drawableBitmap);
                    canvas.drawText(et, (float)x, (float)y, paint);
                    canvasBitmap = drawableBitmap.copy(Config.ARGB_8888, true);
                } else {
                    Toast.makeText(con, "Nie podano tekstu", Toast.LENGTH_LONG).show();
                }
                invalidate();
                return true;
            default:
                return false;
        }
    }

    public void setText(String s) {
        this.et = s;
    }

    public void setColorDV(int k) {
        this.paint.setColor(k);
    }

    public void setCon(Context co) {
        this.con = co;
    }
}

package com.example.tobiasz.projekt;

        import android.graphics.PorterDuff;
        import android.graphics.PorterDuffXfermode;
        import android.graphics.Rect;
        import android.view.View;
        import android.content.Context;
        import android.util.AttributeSet;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.view.MotionEvent;

public class DrawingView extends View  {

    int width=0, height=0, side=0, x=0, y=0;
    private Rect drawRect;
    private Paint canvasPaint;
    private Bitmap canvasBitmap;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawRect = new Rect();
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w; height = h;
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawRect(drawRect, canvasPaint);
}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) touchX;
                y = (int) touchY;
                if(side == 1) {
                    drawRect.set(0, 0, width, (int) touchY);
                }else if(side == 2) {
                    drawRect.set(0, (int) touchY, width, height);
                }else if(side == 3) {
                    drawRect.set(0, 0, (int) touchX, height);
                }else if(side == 4) {
                    drawRect.set((int) touchX, 0, width, height);
                }
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setSide(int side){
        this.side = side;
    }

    public double [] getValues(){
        double [] ret = new double [2];
        ret[0] = side;
        double tmp, tmp2, tmp3;
        if(side == 1 || side == 2){
            tmp = y;
            tmp2 = height;
            tmp3 = tmp/tmp2;
            ret[1] = tmp3;
        }else if(side == 3 || side == 4) {
            tmp = x;
            tmp2 = width;
            tmp3 = tmp/tmp2;
            ret[1] = tmp3;
        }
        return ret;
    }

    public void clear(){
        canvasPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        drawRect.set(0, 0, 0, 0);
    }

}

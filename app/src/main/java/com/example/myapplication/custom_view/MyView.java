package com.example.myapplication.custom_view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyView extends View {
    private int backgroundColor = Color.TRANSPARENT;


    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setColor(String color){
        backgroundColor = Color.parseColor(color);
        invalidate();
    }

    public void setColor(int color){
        backgroundColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if(backgroundColor == 0){
//            paint.setColor(Color.BLUE);
            return;
        } else {
            paint.setColor(backgroundColor);
        }

        setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        int center = getWidth()/7*2;
        int center2 = getHeight()/2;
        int halfW = getWidth()/9*2;


        paint.setMaskFilter(new BlurMaskFilter(800, BlurMaskFilter.Blur.NORMAL));
        canvas.drawCircle(center, center2, halfW, paint);
//
//        paint.setMaskFilter(new BlurMaskFilter(40, BlurMaskFilter.Blur.NORMAL));
//        canvas.drawCircle(100, 350, 250, paint);
//
//        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));
//        canvas.drawRect(100, 600, 250, 750, paint);
//
//        paint.setMaskFilter(new BlurMaskFilter(60, BlurMaskFilter.Blur.NORMAL));
//        canvas.drawRect(100, 850, 250, 1000, paint);
    }
}

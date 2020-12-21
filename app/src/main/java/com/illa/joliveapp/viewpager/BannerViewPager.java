package com.illa.joliveapp.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.illa.joliveapp.controller.BannerController;

public class BannerViewPager extends ViewPager {

    private boolean enabled;
    private BannerController controller;

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    public void setController(BannerController controller){
        this.controller = controller;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.d("SWIPECHECK", "==onTouchEvent==");

        Log.d("SWIPECHECK", "action: " +  action);
        if(controller == null) {return false; }

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            Log.d("SWIPECHECK", "ACTION_DOWN");

            controller.stopSwipe();
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            Log.d("SWIPECHECK", "ACTION_UP");

            controller.startSwipe();
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
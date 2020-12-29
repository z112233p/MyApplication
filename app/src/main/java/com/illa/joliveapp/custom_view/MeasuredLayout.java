package com.illa.joliveapp.custom_view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class MeasuredLayout extends LinearLayout {

    private int largestHeight;

    private OnKeyboardHideListener onKeyboardHideListener;
    private int heightPrevious;
    private int heightNow;
    private View mChildOfContent;
    private int usableHeightPrevious;

    public MeasuredLayout(Context context, View view){
        super(context);
        addView(view);
    }

    public MeasuredLayout(Context context, AttributeSet attrs, int layoutId) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(layoutId, this);
        mChildOfContent=getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // 鍵盤彈出
                if (onKeyboardHideListener != null) {
                    onKeyboardHideListener.onKeyboardHide(false);
                }
            } else {
                // 鍵盤收起
                if (onKeyboardHideListener != null) {
                    onKeyboardHideListener.onKeyboardHide(true);
                }
            }
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }



    public interface OnKeyboardHideListener {
        void onKeyboardHide(boolean hide);
    }

    public void setOnKeyboardHideListener(OnKeyboardHideListener onKeyboardHideListener) {
        this.onKeyboardHideListener = onKeyboardHideListener;
    }
}

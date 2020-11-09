package com.example.myapplication.custom_view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OverScrollLayout extends LinearLayout {

    private static final int ANIM_TIME = 4000;

    private RecyclerView childView;

    private Rect original = new Rect();

    private boolean isMoved = false;

    private float startYpos;
    private float startXpos;

    /**
     * 阻尼系数
     */
    private static final float DAMPING_COEFFICIENT = 0.3f;

    private boolean isSuccess = false;

    private ScrollListener mScrollListener;

    public OverScrollLayout(Context context) {
        this(context, null);
    }

    public OverScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childView = (RecyclerView) getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        original.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
    }

    public void setScrollListener(ScrollListener listener) {
        mScrollListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float touchYpos = ev.getY();
        float touchXpos = ev.getY();

        if (touchXpos >= original.right || touchXpos <= original.left) {
            if (isMoved) {
                recoverLayout();
            }
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                Log.e("Peter","canPullDownMotionEvent.ACTION_CANCEL"+isMoved);

                if (isMoved) {
                    recoverLayout();
                }
            case MotionEvent.ACTION_OUTSIDE:
                Log.e("Peter","canPullDownMotionEvent.ACTION_OUTSIDE"+isMoved);

                if (isMoved) {
                    recoverLayout();
                }
            case MotionEvent.ACTION_DOWN:
                Log.e("Peter","canPullDownMotionEvent.ACTION_DOWN"+isMoved);

                if (isMoved) {
                    recoverLayout();
                }
                startYpos = ev.getY();
                startXpos = ev.getX();
            case MotionEvent.ACTION_MOVE:
                Log.e("Peter","canPullDownMotionEvent.ACTION_MOVE"+isMoved);

                int scrollYpos = (int) (ev.getY() - startYpos);
                int scrollXpos = (int) (ev.getX() - startXpos);

                boolean pullDown = scrollYpos > 5 && canPullDown();
                boolean pullUp = scrollYpos < 5 && canPullUp();

                boolean pullLeft = scrollXpos > 5 && canPullDown();
                boolean pullRight = scrollXpos < -5 && canPullUp();


                if (pullLeft || pullRight) {
                    cancelChild(ev);
                    int offset = (int) (scrollXpos * DAMPING_COEFFICIENT);
                    childView.layout(original.left + offset, original.top, original.right + offset, original.bottom);
                    if (mScrollListener != null) {
                        mScrollListener.onScroll();
                    }
                    Log.e("Peter","canPullDownMotionEvent.ACTION_UP   111"+pullLeft+"   "+pullRight+"  "+scrollXpos);

                    isMoved = true;
                    isSuccess = false;
                    return true;
                } else {
                    startXpos = ev.getX();
                    isMoved = false;
                    isSuccess = true;
                    Log.e("Peter","canPullDownMotionEvent.ACTION_UP   222"+pullLeft+"   "+pullRight+"  "+scrollXpos);

                    return super.dispatchTouchEvent(ev);
                }

            case MotionEvent.ACTION_UP:
                Log.e("Peter","canPullDownMotionEvent.ACTION_UP"+isMoved);

                if (isMoved) {
                    recoverLayout();
                }
                return  super.dispatchTouchEvent(ev);
            default:
                return true;
        }
    }

    /**
     * 取消子view已经处理的事件
     *
     * @param ev event
     */
    private void cancelChild(MotionEvent ev) {
        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(ev);
    }

    /**
     * 位置还原
     */
    private void recoverLayout() {
        Log.e("Peter","recoverLayout "+(childView.getLeft() - original.left));

        TranslateAnimation anim = new TranslateAnimation(childView.getLeft() - original.left, 0, 0, 0);
        anim.setDuration(400);
        anim.setFillAfter(true);
        childView.startAnimation(anim);
        childView.layout(original.left, original.top, original.right, original.bottom);
        isMoved = false;
    }

    /**
     * 判断是否可以下拉
     *
     * @return true：可以，false:不可以
     */
    private boolean canPullDown() {
//        final int firstVisiblePosition = ((LinearLayoutManager) childView.getLayoutManager()).findFirstVisibleItemPosition();
        if (childView.canScrollHorizontally(-1) && childView.getAdapter().getItemCount() != 0 ) {
//            Log.e("Peter","canPullDownFFFF 1"+childView.canScrollVertically(-1));
//            Log.e("Peter","canPullDownFFFF 2"+childView.canScrollVertically(1));
            Log.e("Peter","canPullDownFFFF 3"+childView.canScrollHorizontally(-1)+"    "+childView.getAdapter().getItemCount());
            Log.e("Peter","canPullDownFFFF 4"+childView.canScrollHorizontally(1));

            return false;
        }

//        Log.e("Peter","canPullDown 1"+childView.canScrollVertically(-1));
//        Log.e("Peter","canPullDown 2"+childView.canScrollVertically(1));

        int mostTop = (childView.getChildCount() > 0) ? childView.getChildAt(0).getRight() : 0;
        Log.e("Peter","canPullDownFTTT 3"+childView.canScrollHorizontally(-1)+"   "+mostTop);
        Log.e("Peter","canPullDownFTTT 4"+childView.canScrollHorizontally(1));
        return mostTop >= 0;
    }

    /**
     * 判断是否可以上拉
     *
     * @return true：可以，false:不可以
     */
    private boolean canPullUp() {
//        final int lastItemPosition = childView.getAdapter().getItemCount() - 1;
//        final int lastVisiblePosition = ((LinearLayoutManager) childView.getLayoutManager()).findLastVisibleItemPosition();
//        if (lastVisiblePosition >= lastItemPosition) {
//            final int childIndex = lastVisiblePosition - ((LinearLayoutManager) childView.getLayoutManager()).findFirstVisibleItemPosition();
//            final int childCount = childView.getChildCount();
//            final int index = Math.min(childIndex, childCount - 1);
//            final View lastVisibleChild = childView.getChildAt(index);
//            if (lastVisibleChild != null) {
//                return lastVisibleChild.getRight() <= childView.getRight() - childView.getLeft();
//            }
//        }
        return !childView.canScrollHorizontally(1);
    }


    public interface ScrollListener {
        /**
         * 滚动事件回调
         */
        void onScroll();
    }


}

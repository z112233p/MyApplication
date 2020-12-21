package com.illa.joliveapp.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class AnimHelper {

    private static final String TAG = "AnimHelper";
    private static boolean animOnGoing = false;

    public static boolean checkAnimState(){
        return animOnGoing;
    }
    public static void closeBooleanAnimate(){
        animOnGoing = false;
    }

    //Vertical Setting
    public static Animation getVerticalAnimation(final View view, int duration, final boolean isShown, int height ,int movement) {
        float viewHeight = height;
        if (isShown){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
            Log.e("Peter","params1:  "+params.topMargin+"   viewHeight:  "+viewHeight);

            params.topMargin += viewHeight;
            params.leftMargin += 0;
            view.setLayoutParams(params);
        }
        animOnGoing = true;

        float yMovement = movement * -1;
        float fromY = 0;

        if(isShown){
            fromY = yMovement;
            yMovement = 0;

        }

        TranslateAnimation anim = new TranslateAnimation(0, 0, fromY, yMovement);
        anim.setFillAfter(true);
        anim.setDuration(duration);
//        final float finalYMovement = !isShown? yMovement: -(fromY);
        final float finalYMovement = -viewHeight;
        Log.e("Peter","finalYMovement:  "+finalYMovement);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("Peter","onAnimationStart:  ");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isShown){
                    animOnGoing = false;
                    return;
                }
                view.clearAnimation();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
                Log.e("Peter","params:  "+params.topMargin);

                params.topMargin += finalYMovement;
                params.leftMargin += 0;
                view.setLayoutParams(params);
                animOnGoing = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return anim;
    }


    public static Animation getVerticalAnimation(final View view, int duration, final boolean isShown) {
        if (isShown){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
            Log.e("Peter","params1:  "+params.topMargin);

            params.topMargin += view.getMeasuredHeight();
            params.leftMargin += 0;
            view.setLayoutParams(params);
        }
        animOnGoing = true;

        float yMovement = view.getMeasuredHeight() * -1;

        float fromY = 0;
        if(isShown){
            fromY = yMovement;
            yMovement = 0;

        }

        TranslateAnimation anim = new TranslateAnimation(0, 0, fromY, yMovement);
        anim.setFillAfter(true);
        anim.setDuration(duration);
        final float finalYMovement = !isShown? yMovement: -(fromY);
        Log.e("Peter","finalYMovement:  "+finalYMovement);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("Peter","onAnimationStart:  ");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isShown){
                    animOnGoing = false;
                    return;
                }
                view.clearAnimation();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
                Log.e("Peter","params:  "+params.topMargin);

                params.topMargin += finalYMovement;
                params.leftMargin += 0;
                view.setLayoutParams(params);
                animOnGoing = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return anim;
    }

    //Horizontal Setting
    public static Animation getHorizontalAnimation(final View view, int duration, final boolean isShown){
        animOnGoing = true;

        int xMovement = view.getWidth();

        float fromX = 0;
        if(!isShown){
            xMovement *= (-1);
            fromX = view.getX();
        }

        TranslateAnimation anim = new TranslateAnimation(fromX, xMovement, 0, 0);
        anim.setFillAfter(true);
        anim.setDuration(duration);
        final int finalXMovement = xMovement;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation keepAnim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                keepAnim.setDuration(1);
                view.startAnimation(keepAnim);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
                params.topMargin += 0;
                params.leftMargin += finalXMovement;
                view.setLayoutParams(params);
                animOnGoing = false;
            }
        }, duration);
        return anim;
    }

    //Report && Vertical Setting
    public static Animation getVerticalAnimation(final View view, boolean isLive, int duration, final boolean isShown, final boolean dealVisibility, int height) {
        animOnGoing = true;

        int yMovement;
        if(isLive){
            yMovement = view.getHeight() + height;
        } else {
            yMovement = height;
        }

        float fromY = 0;
        if(!isShown){
            yMovement *= (-1);
            fromY = view.getY();
        }
        if(dealVisibility){
            view.setVisibility(View.VISIBLE);
        }

        TranslateAnimation anim = new TranslateAnimation(0, 0, fromY, yMovement);
        anim.setFillAfter(true);
        anim.setDuration(duration);

        final int finalYMovement = yMovement;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.clearAnimation();

                TranslateAnimation keepAnim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                keepAnim.setDuration(1);
                view.startAnimation(keepAnim);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
                params.topMargin += finalYMovement;
                params.leftMargin += 0;
                view.setLayoutParams(params);

                if(isShown && dealVisibility ){
                    view.setVisibility(View.GONE);
                }
                animOnGoing = false;
            }
        }, duration+100);

        return anim;
    }


    public static Bitmap myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }

    public static void downloadFile3(String path){
        //下载路径，如果路径无效了，可换成你的下载路径
        String url = "http://c.qijingonline.com/test.mkv";
        final long startTime = System.currentTimeMillis();
        Log.i("DOWNLOAD","startTime="+startTime);
        url = path;

        Request request = new Request.Builder().url(url).addHeader("X-Auth-Token", PrefHelper.INSTANCE.getChatToken())
                                .addHeader("X-User-Id", PrefHelper.INSTANCE.getChatId()).build();
        String finalUrl = url;
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DOWNLOAD","download failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {

                    String mSDCardPath= Environment.getExternalStorageDirectory().getPath() +
                            File.separatorChar + Environment.DIRECTORY_DCIM + File.separatorChar;

//                    File dest = new File(mSDCardPath,   finalUrl.substring(finalUrl.lastIndexOf("/") + 1));
                    File dest = new File(Tools.INSTANCE.getLocalSavedAudioPath());

                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());

                    bufferedSink.close();
                    Log.i("DOWNLOAD","download success");
                    Log.i("DOWNLOAD","totalTime="+ (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("DOWNLOAD","download failed");
                } finally {
                    if(bufferedSink != null){
                        bufferedSink.close();
                    }

                }
            }
        });
    }
}


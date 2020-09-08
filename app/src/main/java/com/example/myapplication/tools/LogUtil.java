package com.example.myapplication.tools;

import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.stfalcon.chatkit.messages.MessageHolders;

public class LogUtil extends MessageHolders {
    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    public static void e(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {


            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }


    }


    public <TYPE extends MessageContentType>
    MessageHolders registerContentTypeQQ(
            byte type,
             Class<? extends MessageHolders.BaseMessageViewHolder<TYPE>> incomingHolder, @LayoutRes int incomingLayout,
             Class<? extends MessageHolders.BaseMessageViewHolder<TYPE>> outcomingHolder, @LayoutRes int outcomingLayout,
             MessageHolders.ContentChecker contentChecker) {
        return this;
    }

}

package com.example.myapplication.adapter;

import android.util.SparseArray;
import android.view.View;

import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.ViewHolder;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

public class PeterHolder extends MessageHolders {

    @Override
    protected void bind(ViewHolder holder, Object item, boolean isSelected, ImageLoader imageLoader, View.OnClickListener onMessageClickListener, View.OnLongClickListener onMessageLongClickListener, DateFormatter.Formatter dateHeadersFormatter, SparseArray< MessagesListAdapter.OnMessageViewClickListener > clickListenersArray) {
        super.bind(holder, item, isSelected, imageLoader, onMessageClickListener, onMessageLongClickListener, dateHeadersFormatter, clickListenersArray);
    }
}

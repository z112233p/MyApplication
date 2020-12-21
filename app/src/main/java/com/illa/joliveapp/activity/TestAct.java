package com.illa.joliveapp.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.illa.joliveapp.R;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class TestAct extends AppCompatActivity {

    private static final byte CONTENT_TYPE_VOICE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        boolean t = true;
        Log.e("Peter",""+(t?"QQ":"EE"));

    }
}

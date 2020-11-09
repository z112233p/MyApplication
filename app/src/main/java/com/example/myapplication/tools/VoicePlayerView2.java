package com.example.myapplication.tools;

import android.content.Context;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import me.jagar.chatvoiceplayerlibrary.FileUtils;
import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

public class VoicePlayerView2 extends VoicePlayerView {
    private Context mContext;
    public VoicePlayerView2(Context context) {
        super(context);
        mContext = context;
    }

    public VoicePlayerView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public VoicePlayerView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }

    private void init(){


    }

    public void start(){
        getImgPlay().performClick();
    }

    @Override
    public void setAudio(String audioPath) {
        setPath(audioPath);

        Log.e("peter","VOICEPLAYERVIEW   setAudio");
        setEnableVirtualizer(true);
            getSeekbarV().setVisibility(VISIBLE);
            getSeekBar().setVisibility(GONE);
        getSeekbarV().getProgressDrawable().setColorFilter(getResources().getColor(android.R.color.transparent), PorterDuff.Mode.SRC_IN);
        getSeekbarV().getThumb().setColorFilter(getResources().getColor(android.R.color.transparent), PorterDuff.Mode.SRC_IN);
        getSeekbarV().setColors(getVisualizationPlayedColor(), getVisualizationNotPlayedColor());

        HashMap<String,String> headers = new HashMap<>();
        headers.put("X-Auth-Token",PrefHelper.INSTANCE.getChatToken());
        headers.put("X-User-Id",PrefHelper.INSTANCE.getChatId());
        getSeekbarV().setVisibility(VISIBLE);
        setMediaPlayer(AudioRecordHelper.INSTANCE.getPlayer());
        //                AudioRecordHelper.INSTANCE.getPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                    @Override
//                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                        Log.e("peter","VOICEPLAYERVIEW   onError");
//
//                        return true;
//                    }
//                });
//                Uri uri =  Uri.parse(audioPath);
//                AudioRecordHelper.INSTANCE.getPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {
//                        mediaPlayer.start();
//                    }
//                });
//                AudioRecordHelper.INSTANCE.getPlayer().reset();
//                AudioRecordHelper.INSTANCE.getPlayer().setDataSource(AudioRecordHelper.INSTANCE.getContext(),uri, headers);
//                AudioRecordHelper.INSTANCE.getPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
//                AudioRecordHelper.INSTANCE.getPlayer().prepareAsync();
//        AudioRecordHelper.INSTANCE.getPlayer().setVolume(10, 10);
        //START and PAUSE are in other listeners
        AudioRecordHelper.INSTANCE.getPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                getSeekBar().setMax(mp.getDuration());
                if (getSeekbarV().getVisibility() == VISIBLE){
                    getSeekbarV().setMax(mp.getDuration());
                }
                AudioRecordHelper.INSTANCE.getPlayer().start();
//                        getTxtProcess().setText("00:00:00/"+convertSecondsToHMmSs(mp.getDuration() / 1000));
            }
        });
        AudioRecordHelper.INSTANCE.getPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                getImgPause().setVisibility(View.GONE);
                getImgPlay().setVisibility(View.VISIBLE);
            }
        });
        AudioRecordHelper.INSTANCE.play(audioPath);
        getSeekBar().setOnSeekBarChangeListener(getSeekBarListener());
        getImgPlay().setOnClickListener(getImgPlayClickListener());
        getImgPause().setOnClickListener(getImgPauseClickListener());
        getImgShare().setOnClickListener(getImgShareClickListener());
        if (getSeekbarV().getVisibility() == VISIBLE){
            getSeekbarV().updateVisualizer(FileUtils.fileToBytes(new File(getPath())));
        }
        getSeekbarV().setOnSeekBarChangeListener(getSeekBarListener());
        getSeekbarV().updateVisualizer(FileUtils.fileToBytes(new File(getPath())));
    }
}

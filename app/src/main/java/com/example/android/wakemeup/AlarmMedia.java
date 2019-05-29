package com.example.android.wakemeup;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmMedia {

    private MediaPlayer mMediaPlayer;
    private Context mContext;

    private static AlarmMedia ourInstance;

    public static AlarmMedia getInstance(Context context) {
        if (ourInstance== null) {
            ourInstance = new AlarmMedia(context);
        }
        return ourInstance;
    }

    private AlarmMedia(Context context) {
        mContext = context;
    }

    public void startAlarmMedia() {
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.mantralayamandira);
        mMediaPlayer.start();
    }

    public void stopAlarmMedia() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.seekTo(0);
        }
    }
}

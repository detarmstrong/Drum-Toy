package com.sparkmachine.drum;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Sound {
    private static SoundPool sSoundPool;

    private Point mSoundSymbolCenterPoint;

    private MediaPlayer mMediaPlayer;

    private int mSoundImageButtonId;

    private Drawable mSoundImageDrawable;
    
    public Sound(Context context, int soundResourceId) {
        mMediaPlayer = MediaPlayer.create(context, soundResourceId);
        
        
    }

    public Sound(Context context, int soundResourceId, Drawable soundImageDrawable,
            Point soundSymbolCenterPoint) {
        mMediaPlayer = MediaPlayer.create(context, soundResourceId);
        setSoundSymbolCenterPoint(soundSymbolCenterPoint);
        mSoundImageDrawable = soundImageDrawable;

    }

    public Drawable getSoundImageDrawable() {
        return mSoundImageDrawable;
    }

    public void setSoundImageDrawable(Drawable soundImageDrawable) {
        mSoundImageDrawable = soundImageDrawable;
    }

    private void setSoundSymbolCenterPoint(Point topLeftPoint) {
        mSoundSymbolCenterPoint = topLeftPoint;

    }

    public Point getSoundSymbolCenterPoint() {
        return mSoundSymbolCenterPoint;
    }

    public void setSoundImageButtonId(int soundImageButtonId) {
        mSoundImageButtonId = soundImageButtonId;
    }

    public int getSoundImageButtonId() {
        return mSoundImageButtonId;
    }
    
    public void play(){
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(0);
        } else {
            mMediaPlayer.start();
        }
    }
    
    @Override
    public String toString(){
        return "Sound with id img button id " + getSoundImageButtonId();
    }
}

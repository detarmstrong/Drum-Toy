package com.sparkmachine.DrumMachine;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;

public class Sound {
    private Point mSoundSymbolCenterPoint;

    private MediaPlayer mMediaPlayer;

    public Sound(Context context, int soundResourceId) {
        mMediaPlayer = MediaPlayer.create(context, soundResourceId);
        
        
    }

    public Sound(Context context, int soundResourceId,
            Point soundSymbolCenterPoint) {
        mMediaPlayer = MediaPlayer.create(context, soundResourceId);
        setSoundSymbolCenterPoint(soundSymbolCenterPoint);

    }

    private void setSoundSymbolCenterPoint(Point topLeftPoint) {
        mSoundSymbolCenterPoint = topLeftPoint;

    }

    public Point getSoundSymbolCenterPoint() {
        return mSoundSymbolCenterPoint;
    }
}

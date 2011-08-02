package com.sparkmachine.playground;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioTrackSample implements Runnable {
    static private final int mBufferSize = 8000;
    private AudioTrack mTrack;
    private short mBuffer[];
    private short mSample;
    public boolean mPlay = true;

    public AudioTrackSample() {
        mBuffer = new short[mBufferSize];
        mTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize * 2, AudioTrack.MODE_STREAM);

        mSample = 0;
    }
    
    @Override
    public void run() {
        mTrack.play();
        while(mPlay){
            generateTone(mBuffer, mBufferSize);
            mTrack.write(mBuffer, 0, mBufferSize);
        }

    }

    private void generateTone(short[] data, int size) {
        for(int i = 0; i < size; i++){
            data[i] = mSample;
            mSample += 600;
            
        }
        
    }

}

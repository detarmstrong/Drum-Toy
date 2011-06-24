package com.sparkmachine.DrumMachine;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

/**
 * @purpose Sequencer activity is about making sequences of sounds
 * 
 */
public class Sequencer extends Activity implements OnClickListener {
    private static final String TAG = "Sequencer";
    private HorizontalScrollView mCanvasScrollView;
    private FrameLayout mCanvasLayout;
    private BeatBoardBackgroundView mBeatBoardBgView;
    private View mPlayButton;
    private MediaPlayer mTomPlayer;
    private ScheduledThreadPoolExecutor mTimer;
    private boolean mIsTmerScheduled;
    private int mBeatIndex;
    private int mSumSequenceBeats;
    private SoundSymbolView mSoundSymbolView;
    private BlipView mBlipView;
    private ArrayList<Beat> mBeatsArray;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int beatCount = 12;
        mBeatsArray = new ArrayList<Beat>(beatCount);
        for (int i = 0; i < beatCount; i++) {
            mBeatsArray.add(new Beat(Beat.BeatState.RESTING, false));
        }
        
        BeatBoard beatBoard = new BeatBoard(this, mBeatsArray);

        mCanvasScrollView = (HorizontalScrollView) findViewById(R.id.canvasScroll);
        mCanvasLayout = (FrameLayout) findViewById(R.id.canvas);

        mBeatBoardBgView = new BeatBoardBackgroundView(this, mBeatsArray);
        mBeatBoardBgView.setLayoutParams(new ViewGroup.LayoutParams(
                beatBoard.getWidth(),
                ViewGroup.LayoutParams.MATCH_PARENT));

        mBlipView = new BlipView(this, beatBoard, mBeatsArray);
        mBlipView.setLayoutParams(new ViewGroup.LayoutParams(
                beatBoard.getWidth(),
                ViewGroup.LayoutParams.MATCH_PARENT));
        mBlipView.setBackgroundColor(R.color.transparent);
        
        mSoundSymbolView = new SoundSymbolView(this, beatBoard, mBeatsArray);
        mSoundSymbolView.setLayoutParams(new ViewGroup.LayoutParams(
                beatBoard.getWidth(),
                ViewGroup.LayoutParams.MATCH_PARENT));
        mSoundSymbolView.setBackgroundColor(R.color.transparent);

        mCanvasLayout.addView(mBeatBoardBgView);
        mCanvasLayout.addView(mBlipView);
        mCanvasLayout.addView(mSoundSymbolView);

        mPlayButton = findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(this);

        mTomPlayer = MediaPlayer.create(this, R.raw.tom);

        mIsTmerScheduled = false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mTimer.shutdownNow();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {
        mBeatBoardBgView.requestLayout();
        switch (view.getId()) {
            case R.id.playButton:
                if (!isLooping()) {
                    startLooping();
                } else {
                    stopLooping();
                }
                break;
        }

    }

    private int getBpm() {
        return 120;
    }

    private void stopLooping() {
        mTimer.shutdown();
        mIsTmerScheduled = false;
    }

    private boolean isLooping() {
        return mIsTmerScheduled;
    }

    /**
     * purpose Begin looping the sequence
     */
    private void startLooping() {
        int periodMs = convertBpmToPeriodMs(getBpm());

        mTimer = new ScheduledThreadPoolExecutor(3);
        mTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                int mBeatIndex = revolvingBeatIndex();
                
                mUiUpdateHandler.sendMessage(mUiUpdateHandler
                        .obtainMessage(mBeatIndex));

                Log.i(TAG, "in loop, hitting play()");
                if (mTomPlayer.isPlaying()) {
                    mTomPlayer.seekTo(0);
                } else {
                    mTomPlayer.start();
                }

            }

        }, periodMs, periodMs, TimeUnit.MILLISECONDS);
        mIsTmerScheduled = true;

    }

    protected int revolvingBeatIndex() {
        mBeatIndex++;
        if (mBeatIndex >= mBeatsArray.size()) {
            mBeatIndex = 0;

        }
        return mBeatIndex;
    }

    private int convertBpmToPeriodMs(int bpm) {
        return 1000 / (bpm / 60);
    }

    private Handler mUiUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int activeBeatIndex = msg.what;
            
            // update views that make up soundboard
            mBlipView.update(activeBeatIndex, mBeatsArray);

            if(activeBeatIndex == 0){
                mCanvasScrollView.fullScroll(View.FOCUS_LEFT);
            } else
            if(activeBeatIndex % 4 == 0){
                mCanvasScrollView.pageScroll(View.FOCUS_RIGHT);
            }
            
        }
    };
}


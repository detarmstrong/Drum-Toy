package com.sparkmachine.drum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sparkmachine.drum.R;
import com.sparkmachine.playground.AudioTrackSample;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;

/**
 * @purpose Sequencer activity is about making sequences of sounds
 * 
 */
public class Sequencer extends Activity implements OnClickListener {
    private static final String TAG = "Sequencer";
    private static final int MAX_STREAMS = 8;
    private HorizontalScrollView mCanvasScrollView;
    private FrameLayout mCanvasLayout;
    private BeatBoardBackgroundView mBeatBoardBgView;
    private View mPlayButton;
    private ScheduledThreadPoolExecutor mTimer;
    private boolean mIsTmerScheduled;
    private int mBeatIndex;
    private int mSumSequenceBeats;
    private SoundSymbolView mSoundSymbolView;
    private BlipView mBlipView;
    private ArrayList<Beat> mBeatsArray;
    private View mTomButton;
    private int mSelectedSoundResourceId;
    private HashMap<Integer, Integer> mSoundMap;
    private int mSelectedSoundImageButtonId;
    private Drawable mSelectedSoundDrawable;
    private AudioTrackSample mAudioTrack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int beatCount = 4;
        mBeatsArray = new ArrayList<Beat>(beatCount);
        for (int i = 0; i < beatCount; i++) {
            mBeatsArray.add(new Beat(Beat.BeatState.RESTING, false));
        }

        BeatBoard beatBoard = new BeatBoard(this, mBeatsArray);

        mCanvasScrollView = (HorizontalScrollView) findViewById(R.id.canvasScroll);
        mCanvasLayout = (FrameLayout) findViewById(R.id.canvas);

        mBeatBoardBgView = new BeatBoardBackgroundView(this, mBeatsArray);
        mBeatBoardBgView.setLayoutParams(new ViewGroup.LayoutParams(beatBoard
                .getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));

        mBlipView = new BlipView(this, beatBoard, mBeatsArray);
        mBlipView.setLayoutParams(new ViewGroup.LayoutParams(beatBoard
                .getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
        mBlipView.setBackgroundColor(R.color.transparent);

        mSoundSymbolView = new SoundSymbolView(this, beatBoard, mBeatsArray);
        mSoundSymbolView.setLayoutParams(new ViewGroup.LayoutParams(beatBoard
                .getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
        mSoundSymbolView.setBackgroundColor(R.color.transparent);

        mCanvasLayout.addView(mBeatBoardBgView);
        mCanvasLayout.addView(mBlipView);
        mCanvasLayout.addView(mSoundSymbolView);

        mPlayButton = findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(this);

        mIsTmerScheduled = false;

        mSoundMap = new HashMap<Integer, Integer>();
        mSoundMap.put(R.id.drumButtonTom, R.raw.tom);
        mSoundMap.put(R.id.drumButtonHiHatOpen, R.raw.hat_open);
        mSoundMap.put(R.id.drumButtonRide, R.raw.ride_bell);
        mSoundMap.put(R.id.drumButtonSnare, R.raw.snare);
        mSoundMap.put(R.id.drumButtonDing, R.raw.ding);

        for (Integer viewResId : mSoundMap.keySet()){
            findViewById(viewResId).setOnClickListener(this);
        }
        
        mSelectedSoundResourceId = mSoundMap.get(R.id.drumButtonTom);
        
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
            default:
                mSelectedSoundResourceId = (Integer) mSoundMap
                        .get(view.getId());
                mSelectedSoundImageButtonId = view.getId();
                mSelectedSoundDrawable = ((ImageButton) findViewById(view
                        .getId())).getDrawable();

                Log.i(TAG, "button pushed on view "
                        + mSelectedSoundImageButtonId);
                break;
        }

    }

    private int getBpm() {
        return 120;
    }

    public Sound getSelectedSoundInstance(Point center) {
        return new Sound(this, mSelectedSoundResourceId,
                mSelectedSoundDrawable, center);
    }

    private void stopLooping() {
        mTimer.shutdown();
        mIsTmerScheduled = false;
        mAudioTrack.mPlay = false;
        
    }

    private boolean isLooping() {
        return mIsTmerScheduled;
    }

    /**
     * purpose Begin looping the sequence
     */
    private void startLooping() {
        mAudioTrack = new AudioTrackSample();
        mAudioTrack.run();
        
        // allow for quarter notes
        int periodMs = convertBpmToPeriodMs(getBpm());

        mTimer = new ScheduledThreadPoolExecutor(3);
        mTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                int mBeatIndex = revolvingBeatIndex();
                
                /*
                ArrayList<BeatSubDivide> subDivisionsTemp;
                Beat beat = mBeatsArray.get(mBeatIndex);
                
                subDivisionsTemp = beat.getSubdivisions();

                if (subDivisionsTemp.size() > 0) { // Avoid implicit iterator
                                                   // creation in for : in below if
                                                   // no elements

                    for (BeatSubDivide subDivide : subDivisionsTemp) {
                        ArrayList<Sound> tSounds = subDivide.getSounds();

                        if (tSounds != null && tSounds.size() > 0) {
                            for (Sound sound : tSounds) {
                                sound.play();

                            }
                            continue;
                        }

                    }

                }
                */
                
                

                mUiUpdateHandler.sendMessage(mUiUpdateHandler
                        .obtainMessage(mBeatIndex));

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

            if (activeBeatIndex == 0) {
                mCanvasScrollView.fullScroll(View.FOCUS_LEFT);
            } else if (activeBeatIndex % 4 == 0) {
                mCanvasScrollView.pageScroll(View.FOCUS_RIGHT);
            }

        }
    };
}

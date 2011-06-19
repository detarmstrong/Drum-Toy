package com.sparkmachine.DrumMachine;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * @purpose Sequencer activity is about making sequences of sounds
 *
 */
public class Sequencer extends Activity implements OnClickListener {
    private static final String TAG = "Sequencer";
    private HorizontalScrollView mCanvasScrollView;
    private LinearLayout mCanvasLayout;
    private BeatBoardView mVisualizerView;
    private View mPlayButton;
    private MediaPlayer mTomPlayer;
    private ScheduledThreadPoolExecutor mTimer;
    private boolean mIsTmerScheduled;
    private int mBeatIndex;
    private int mSumSequenceBeats;
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mCanvasScrollView = (HorizontalScrollView) findViewById(R.id.canvasScroll);
        mCanvasLayout = (LinearLayout) findViewById(R.id.canvas);

        mVisualizerView = new BeatBoardView(this);
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(900,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mCanvasLayout.addView(mVisualizerView);

        mVisualizerView.updateVisualizer(new Beat[4]);
        
        mPlayButton = findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(this);
        
        mTomPlayer = MediaPlayer.create(this, R.raw.tom);
        
        mIsTmerScheduled = false;
        
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            mTimer.shutdownNow();
        }
        catch(Exception e){
            
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.playButton:
                if(!isLooping()){
                    startLooping(getBpm());
                }
                else{
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
     * purpose  Begin looping the sequence
     */
    private void startLooping(int bpm) {
        int periodMs = convertBpmToPeriodMs(bpm);
        
        mTimer = new ScheduledThreadPoolExecutor(1);
        mTimer.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                int mBeatIndex = revolvingBeatIndex();
                Log.i(TAG, "in loop, hitting play()");
                if(mTomPlayer.isPlaying()){
                    mTomPlayer.seekTo(0);
                }
                else{
                    mTomPlayer.start();
                }
                
            }
            
        }, periodMs, periodMs, TimeUnit.MILLISECONDS);
        mIsTmerScheduled = true;

    }

    protected int revolvingBeatIndex() {
        mBeatIndex++;
        if(mBeatIndex > mSumSequenceBeats){
            mBeatIndex = 0;
            
        }
        return mBeatIndex;
    }

    private int convertBpmToPeriodMs(int bpm) {
        return 1000 / (bpm  / 60);
    }
}

class BeatBoardView2 extends View {
    public int mBeatCount = 4;
    public int mMinGapDp = 15;
    public int mZoomLevel = 1;
    public int mLineColor = Color.WHITE;

    public BeatBoardView2(Context context) {
        super(context);
        init();
    }

    public void init() {

    }

}


/**
 * @purpose View of blips that indicate the current beat playing
 *
 */
class BlipView extends View {

    public BlipView(Context context) {
        super(context);
    }
    
}

/**
 * @purpose SoundView represents graphically sound elements (instruments)
 *
 */
class SoundView extends View {

    public SoundView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
}


/**
 * @purpose Represents the view of the static board elements
 *
 */
class BeatBoardView extends View {
    private Paint mBlipPaint = new Paint();
    private Rect mBlipRect = new Rect(); // blip is manifestation of a beat
    private Beat[] mBeats;
    private Context mContext;
    private Paint mActiveBeatLinePaint;
    private Paint mInactiveBeatLinePaint;

    public BeatBoardView(Context context) {
        super(context);
        mContext = (Context) context;
        init();
    }

    public void init() {
        mBlipPaint.setColor(Color.GRAY);
        mBlipPaint.setStrokeWidth(1);
        mBlipPaint.setAntiAlias(true);
        
        mActiveBeatLinePaint = new Paint();
        mActiveBeatLinePaint.setColor(Color.WHITE);
        mInactiveBeatLinePaint = new Paint();
        mInactiveBeatLinePaint.setColor(Color.GRAY);
        
    }

    public void updateVisualizer(Beat[] beats) {
        mBeats = beats;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int blipWidth = 30;

        // at default zoom level, show 4 full beats and half of the 5th beat
        int displayWidthPx = displayWidth();

        float beatSpacing = (float) (displayWidthPx / 5.5);
        float absBeatSpace = 0; // absolute value from left for where to place x
                                // of line

        for (Beat beat : mBeats) {
            absBeatSpace += beatSpacing;
            canvas.drawLine(absBeatSpace, 0, absBeatSpace, getHeight(),
                    mActiveBeatLinePaint);
            
            int blipLeft = (int) absBeatSpace - (blipWidth / 2);
            int blipTop = getHeight() - 10;
            int blipRight = blipLeft + blipWidth;
            int blipBottom = getHeight();
            mBlipRect.set(blipLeft, blipTop, blipRight, blipBottom);
            canvas.drawRect(mBlipRect, mBlipPaint);
        }

        // draw the faded line that represents adding an new line
        float addNewBeatLineLineX = absBeatSpace + beatSpacing;
        canvas.drawLine(addNewBeatLineLineX, 0, addNewBeatLineLineX,
                getHeight(), mInactiveBeatLinePaint);

    }

    private int displayWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);

        int displayWidthPx = metrics.widthPixels;
        return displayWidthPx;
    }

}
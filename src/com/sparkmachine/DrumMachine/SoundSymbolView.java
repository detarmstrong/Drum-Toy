package com.sparkmachine.DrumMachine;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * SoundSymbolView represents graphically sound elements (instruments)
 * 
 */
public class SoundSymbolView extends View {

    private static final String TAG = null;
    private Context mContext;
    private BeatBoard mBeatBoard;
    private ArrayList<Beat> mBeats;

    public SoundSymbolView(Context context, BeatBoard beatBoard,
            ArrayList<Beat> beatsArray) {
        super(context);

        mContext = context;
        mBeatBoard = beatBoard;
        mBeats = beatsArray;

    }

    public void update(int activeBeatIndex, ArrayList<Beat> beats) {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        Toast.makeText(mContext,
                "on touch event: " + event.getX() + ", " + event.getY(),
                Toast.LENGTH_SHORT).show();

        Log.d(TAG, "on touch event: " + event.getX() + ", " + event.getY());

        return true;

    }

}

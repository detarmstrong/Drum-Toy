package com.sparkmachine.DrumMachine;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

/**
 * Render the active beat represented as a little yellow rectangle
 * 
 */
public class BlipView extends View {

    private static final String TAG = "BlipView";
    private ArrayList<Beat> mBeats;
    private HashMap<String, Object> mPropsMap;
    private int mActiveBeatIndex;
    private Paint mBlipPaint;
    private Context mContext;
    private Rect mBlipRect;
    private BeatBoard mBeatBoard;

    public BlipView(Context context, BeatBoard beatBoard, ArrayList<Beat> beatsArray) {
        super(context);

        mContext = context;
        mBeatBoard = beatBoard;
        mBeats = beatsArray;

        mBlipRect = new Rect();

        mPropsMap = new HashMap<String, Object>();
        mPropsMap.put("width", 30);
        mPropsMap.put("height", 10);

        mBlipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlipPaint.setColor(getResources().getColor(R.color.blip));
    }

    public void update(int activeBeatIndex, ArrayList<Beat> beats) {
        mBeats = beats;
        mActiveBeatIndex = activeBeatIndex;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Integer blipWidth = (Integer) mPropsMap.get("width");
        Integer blipHeight = (Integer) mPropsMap.get("height");

        // at default zoom level, show 4 full beats and half of the 5th beat
        int beatSpacing = mBeatBoard.beatSpacing();
        
        int absBeatSpace = 0; // absolute value from left for where to place x
                                // of line

        for (Beat beat : mBeats) {
            absBeatSpace += beatSpacing;

            if (beat.equals(mBeats.get(mActiveBeatIndex))) {
                drawBlip(canvas, blipWidth, blipHeight, absBeatSpace);
            }
        }
        
        //getLayoutParams().width = (int) absBeatSpace;

    }



    private void drawBlip(Canvas canvas, Integer blipWidth, Integer blipHeight,
            int absBeatSpace) {
        int blipLeft = absBeatSpace - (blipWidth / 2);
        int blipTop = getHeight() - blipHeight;
        int blipRight = blipLeft + blipWidth;
        int blipBottom = getHeight();
        mBlipRect.set(blipLeft, blipTop, blipRight, blipBottom);
        canvas.drawRect(mBlipRect, mBlipPaint);
    }

}

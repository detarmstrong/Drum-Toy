package com.sparkmachine.drum;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * @purpose Represents the view of the background board elements
 *
 */
class BeatBoardBackgroundView extends View {
    private Paint mBlipPaint = new Paint();
    private Rect mBlipRect = new Rect(); // blip is manifestation of a beat
    private ArrayList<Beat> mBeats;
    private Context mContext;
    private Paint mActiveBeatLinePaint;
    private Paint mInactiveBeatLinePaint;
    private Paint mInvisibleBeatLinePaint;

    public BeatBoardBackgroundView(Context context, ArrayList<Beat> beatsArray) {
        super(context);
        init(context, beatsArray);
    }

    public void init(Context context, ArrayList<Beat> beatsArray) {
        mContext = context;
        mBeats = beatsArray;
        
        mBlipPaint.setColor(Color.GRAY);
        mBlipPaint.setStrokeWidth(1);
        mBlipPaint.setAntiAlias(true);
        
        mActiveBeatLinePaint = new Paint();
        mActiveBeatLinePaint.setColor(Color.WHITE);
        mInactiveBeatLinePaint = new Paint();
        mInactiveBeatLinePaint.setColor(Color.GRAY);
        mInvisibleBeatLinePaint = new Paint();
        mInvisibleBeatLinePaint.setColor(Color.BLACK);
        
    }

    public void update(ArrayList<Beat> beatsArray) {
        mBeats = beatsArray;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int blipWidth = 30;

        // at default zoom level, show 4 full beats and half of the 5th beat
        int displayWidthPx = Util.displayWidth(mContext);

        float beatSpacing = (float) (displayWidthPx / 5.5);
        float absBeatSpace = 0; // absolute value from left for where to place x
                                // of line

        for (Beat beat : mBeats) {
            canvas.drawLine(absBeatSpace, 0, absBeatSpace, getHeight(),
                    mActiveBeatLinePaint);
            
            // draw background blips - BlipView draws foreground active blip
            int blipLeft = (int) absBeatSpace - (blipWidth / 2);
            int blipTop = getHeight() - 10;
            int blipRight = blipLeft + blipWidth;
            int blipBottom = getHeight();
            mBlipRect.set(blipLeft, blipTop, blipRight, blipBottom);
            canvas.drawRect(mBlipRect, mBlipPaint);
            
            absBeatSpace += beatSpacing;
        }

        // draw the faded line that represents adding an new line
        float addNewBeatLineLineX = absBeatSpace + beatSpacing;
        canvas.drawLine(addNewBeatLineLineX, 0, addNewBeatLineLineX,
                getHeight(), mInactiveBeatLinePaint);

        // add an invisible line to make scroll
        float invisibleBeatLineX = absBeatSpace + (beatSpacing * 2);
        canvas.drawLine(invisibleBeatLineX, 0, invisibleBeatLineX,
                getHeight(), mInvisibleBeatLinePaint);
        
        //getLayoutParams().width = (int) invisibleBeatLineX;
        //requestLayout();

    }



}
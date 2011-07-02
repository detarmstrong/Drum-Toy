package com.sparkmachine.DrumMachine;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * SoundSymbolView represents graphically sound elements (instruments)
 * 
 */
public class SoundSymbolView extends View {

    private static final String TAG = "SoundSymbolView";
    private Context mContext;
    private BeatBoard mBeatBoard;
    private ArrayList<Beat> mBeats;
    private Paint mSymbolPaint;

    public SoundSymbolView(Context context, BeatBoard beatBoard,
            ArrayList<Beat> beatsArray) {
        super(context);

        mContext = context;
        mBeatBoard = beatBoard;
        mBeats = beatsArray;
        mSymbolPaint = new Paint();
        mSymbolPaint.setColor(Color.GREEN);
        mSymbolPaint.setTextSize(12);

    }

    public void update(int activeBeatIndex, ArrayList<Beat> beats) {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "drawing soudnsymbolview");

        ArrayList<BeatSubDivide> subDivisionsTemp;

        // draw all sounds
        for (Beat beat : mBeats) {
            subDivisionsTemp = beat.getSubdivisions();

            for (BeatSubDivide subDivide : subDivisionsTemp) {
                ArrayList<Sound> tSounds = subDivide.getSounds();

                for (Sound sound : tSounds) {
                    Point centerPoint = sound.getSoundSymbolCenterPoint();

                    canvas.drawBitmap(((BitmapDrawable) getResources()
                            .getDrawable(R.drawable.kick)).getBitmap(),
                            centerPoint.x - (drawableWidth / 2), centerPoint.y - (drawableHeight / 2), mSymbolPaint);

                }

            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        // snap to snap resolution, as denominator of fraction
        // eg snapResolution = 16 is 1/16th of the beat
        // int snapResolution = 16;

        // int subDivision = 0;
        // float touchedX = event.getX();
        // int beatBoardWidth = getWidth();
        //
        // float x = beatBoardWidth / touchedX;
        // int beatIndex = (int) x;
        //
        // float dividend = (x / (float) snapResolution);
        // float choppedDecimal = dividend - (int) dividend;
        // int roundedRemainder = Math.round(choppedDecimal);
        //
        // subDivision = (int) dividend * snapResolution
        // + (roundedRemainder * snapResolution);
        //
        // Log.i(TAG, "beat " + beatIndex + ", subdivision " + subDivision);
        //
        // mBeats.get(beatIndex).addAt(subDivision);

        // Next Attempt - Purple

        int snapResolution = 4;

        float touchedX = event.getX();
        int beatBoardWidth = getWidth();

        // Determine which beat touched on

        int beatsOnBoard = mBeats.size();

        int beatGapPxs = beatBoardWidth / beatsOnBoard;
        float snapResolutionPxs = beatGapPxs / snapResolution;

        float nearestBeatPxs = roundToNearest(touchedX, snapResolutionPxs);

        int subbeatIndex = (int) (nearestBeatPxs / (beatGapPxs / snapResolution));
        int beatIndex = subbeatIndex / snapResolution;
        int subDivision = subbeatIndex % snapResolution;

        Point soundSymbolCenterPoint = new Point((int) nearestBeatPxs,
                (int) event.getY());

        Sound selectedSound = ((Sequencer) mContext)
                .getSelectedSoundInstance(soundSymbolCenterPoint);

        mBeats.get(beatIndex).addAt(subDivision, selectedSound);

        Log.i(TAG, "beat " + beatIndex + " subbeat " + subbeatIndex
                % snapResolution);

        invalidate((int) event.getX() - 50, (int) event.getY() - 50,
                (int) event.getX() + 50, (int) event.getY() + 50);

        return true;

    }

    public float roundToNearest(float input, float multipleOf) {
        float dividend = (input / (float) multipleOf);
        float choppedDecimal = dividend - (int) dividend;
        int roundedRemainder = Math.round(choppedDecimal);

        return (int) dividend * multipleOf + (roundedRemainder * multipleOf);

    }

}

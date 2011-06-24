package com.sparkmachine.DrumMachine;

import java.util.ArrayList;

import android.content.Context;

public class BeatBoard {

    private ArrayList<Beat> mBeats;
    private Context mContext;
    public int mMinGapDp = 15;
    public float mDefaultBeatsDisplayed = 5.5f;
    public int mZoomLevel = 1;

    public BeatBoard(Context context, ArrayList<Beat> beats){
        mContext = context;
        mBeats = beats;
    }
    
    public void setBeats(ArrayList<Beat> beats){
        mBeats = beats;
    }

    public int getWidth() {
        int beatSpacing = beatSpacing();
        return mBeats.size() * beatSpacing;
    }
    
    public int beatSpacing() {
        int displayWidthPx = Util.displayWidth(mContext);

        float beatSpacing = (float) (displayWidthPx / mDefaultBeatsDisplayed);
        return (int) beatSpacing;
    }
}

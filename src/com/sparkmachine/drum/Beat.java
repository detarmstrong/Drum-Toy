package com.sparkmachine.drum;

import java.util.ArrayList;

public class Beat {
    public boolean playing; // to play is to be heard
    public boolean stressed; //
    private BeatState mBeatState;
    private boolean mIsStressed;
    private ArrayList<BeatSubDivide> mSubdivisions = new ArrayList<BeatSubDivide>(16);
    
    public ArrayList<BeatSubDivide> getSubdivisions() {
        return mSubdivisions;
    }

    public void setSubdivisions(ArrayList<BeatSubDivide> subdivisions) {
        mSubdivisions = subdivisions;
    }

    public Beat(BeatState beatState, boolean isStressed){
        mBeatState = beatState;
        mIsStressed = isStressed;
        
        for(int j = 0; j < 4; j++){
            mSubdivisions.add(new BeatSubDivide(0));
            
        }
        
    }
    
    public enum BeatState {
        PLAYING, RESTING;
    }

    public void addAt(int subDivision, Sound selectedSound) {
        BeatSubDivide sub = new BeatSubDivide(subDivision);
        sub.getSounds().add(selectedSound);
        mSubdivisions.add(subDivision, sub);
        
    }
    
}


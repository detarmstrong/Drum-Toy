package com.sparkmachine.DrumMachine;

public class Beat {
    public boolean playing; // to play is to be heard
    public boolean stressed; //
    private BeatState mBeatState;
    private boolean mIsStressed;
    
    public Beat(BeatState beatState, boolean isStressed){
        mBeatState = beatState;
        mIsStressed = isStressed;
    }
    
    public enum BeatState {
        PLAYING, RESTING;
    }
    
}


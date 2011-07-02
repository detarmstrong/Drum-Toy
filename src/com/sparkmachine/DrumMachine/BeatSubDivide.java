package com.sparkmachine.DrumMachine;

import java.util.ArrayList;

public class BeatSubDivide {

    private ArrayList<Sound> mSounds;
    
    // 1 == on begin of the beat
    // 2 == mid beat
    // 3 == at first 3rd of beat
    // etc
    private int mSubdivision;
    
    public BeatSubDivide(int subDivide){
        mSubdivision = subDivide;
        mSounds = new ArrayList<Sound>();
    }

    public void setSounds(ArrayList<Sound> sounds) {
        mSounds = sounds;
    }

    public ArrayList<Sound> getSounds() {
        return mSounds;
    }
    

}

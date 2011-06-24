package com.sparkmachine.DrumMachine;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Util {
    
    public static int displayWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);

        int displayWidthPx = metrics.widthPixels;
        return displayWidthPx;
    }

}

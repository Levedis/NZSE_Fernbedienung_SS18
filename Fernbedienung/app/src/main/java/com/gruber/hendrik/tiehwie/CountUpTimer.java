package com.gruber.hendrik.tiehwie;

import android.os.CountDownTimer;
import android.util.Log;

public class CountUpTimer {

    final long totalSeconds = 30000;
    long intervalSeconds = 1;
    long timeElapsed = 0;

    CountDownTimer timer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {
        public void onTick(long millisUntilFinished) {
            timeElapsed = (totalSeconds * 1000 - millisUntilFinished) / 1000;
            Log.i("Time: ", "Elapsed: " + timeElapsed);
        }

        public void onFinish() {
            Log.i("Oops! ", "Maximum Pause Length Reached!");
        }
    };

    public void startCounting(){
        timer.start();
    }
    public void stopCounting(){
        timer.cancel();
    }
    public long getTime(){
        return timeElapsed;
    }
}

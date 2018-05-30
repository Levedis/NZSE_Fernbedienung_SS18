package com.gruber.hendrik.tiehwie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    private Button flipButton;
    private Button settingsButton;
    private Button favoritesButton;
    private Button pipButton;
    private Button nightModeButton;
    private Button playButton;
    private Button channelPlusButton;
    private Button channelMinusButton;
    private SeekBar volumeSlider;
    private int volumeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Buttons
        flipButton = (Button) findViewById(R.id.flipButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        favoritesButton = (Button) findViewById(R.id.favoritesButton);
        pipButton = (Button) findViewById(R.id.pipButton);
        nightModeButton = (Button) findViewById(R.id.nightButton);

        playButton = (Button) findViewById(R.id.playButton);
        channelPlusButton = (Button) findViewById(R.id.channelPlusButton);
        channelMinusButton = (Button) findViewById(R.id.channelMinusButton);
        volumeSlider = (SeekBar) findViewById(R.id.volumeSlider);

    }

    public void buttonClick(View v){
        if(v == flipButton){
            Log.i("Button Clicked:", "Flip");
        }
        if(v == settingsButton){
            Log.i("Button Clicked:", "Settings");
            startActivity(new Intent(this, MainSettings.class));
        }
        if(v == favoritesButton){
            Log.i("Button Clicked:", "Favorites");
            startActivity(new Intent(this, ChannelList.class));
        }
        if(v == pipButton){
            Log.i("Button Clicked:", "Picture in Picture Settings");
            startActivity(new Intent(this, PicInPic.class));
        }
        if(v == nightModeButton){
            Log.i("Button Clicked:", "Night Mode Toggle");
        }
        if(v == playButton){
            Log.i("Button Clicked:", "Play/Pause");
        }
        if(v == channelPlusButton){
            Log.i("Button Clicked:", "Channel++");
        }
        if(v == channelMinusButton){
            Log.i("Button Clicked:", "Channel--");
        }
    }

    public void onVolumeChange(SeekBar v){
        volumeValue = v.getProgress();
        String volumeValueString = "Volume: " + Integer.toString(volumeValue);
        Log.i("Slider Changed: ", volumeValueString);
    }

}

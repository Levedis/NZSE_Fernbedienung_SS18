package com.gruber.hendrik.tiehwie;

import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private Button mirrorScreenRight;
    private Button mirrorScreenLeft;
    private Button settingsButton;
    private Button favoritesButton;
    private Button pipButton;
    private Button nightModeButton;
    private Button playButton;
    private Button channelPlusButton;
    private Button channelMinusButton;
    private SeekBar volumeSlider;

    private boolean rightHanded;
    private boolean isPaused;
    CountUpTimer countUp;

    private HttpRequest request;

    @Override
    //Get Buttons new when orientation is changed. Musst be done to work with orientation switching
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            getButtons();

        }else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            getButtons();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Get Buttons
        getButtons();

        //Default Layout is right-handed-mode
        rightHanded = true;

        //TV is playing by default
        isPaused = false;

        //Make HTTP Request
        request = new HttpRequest("192.168.2.107", 1000, true);
        //request = new HttpRequest("10.0.0.2", 1000, false);

        //Initialize Counter for Timeshift
        countUp = new CountUpTimer();

    }



    public void buttonClick(View v) throws IOException, JSONException {
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
            playPause();

        }
        if(v == channelPlusButton){
            Log.i("Button Clicked:", "Channel++");
            channelPlus();
        }
        if(v == channelMinusButton){
            Log.i("Button Clicked:", "Channel--");
            channelMinus();
        }
    }

    /*  Gets all Buttons on the main
        screen and assigns them to variables    */
    public void getButtons(){
        mirrorScreenRight = (Button) findViewById(R.id.flipButtonRight);
        mirrorScreenLeft = (Button) findViewById(R.id.flipButtonLeft);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        favoritesButton = (Button) findViewById(R.id.favoritesButton);
        pipButton = (Button) findViewById(R.id.pipButton);
        nightModeButton = (Button) findViewById(R.id.nightButton);

        playButton = (Button) findViewById(R.id.playButton);
        channelPlusButton = (Button) findViewById(R.id.channelPlusButton);
        channelMinusButton = (Button) findViewById(R.id.channelMinusButton);
        volumeSlider = (SeekBar) findViewById(R.id.volumeSlider);
    }

    /*  Switches Mirror-Mode in portrait and landscape mode
        by changing the layout xml files    */
    public void mirrorScreen(View view){
        if(!rightHanded){
            rightHanded = true;
            setContentView(R.layout.activity_main);
            getButtons();
            Log.i("Flipped: ", "Right");
        }
        else{
            rightHanded = false;
            setContentView(R.layout.activity_main_left);
            getButtons();
            Log.i("Flipped: ", "Left");
        }


    }

    //Timeshift Control
    public void playPause(){
        if(!isPaused){
            try {
                //Pause Request sent to TV
                request.execute("timeShiftPause=");
            }
            catch(IOException e){}
            catch(JSONException je){}

            isPaused = true;
            countUp.startCounting();
        } else {
            isPaused = false;
            countUp.stopCounting();
            long timer = countUp.getTime();
            String strLong = Long.toString(timer);
            try {
                //Play Request sent to TV with Timestamp
                request.execute("timeShiftPlay=" + strLong);
                Log.i("Timer: ", strLong);
            }
            catch(IOException e){}
            catch(JSONException je){}
            
        }

    }

    //+ Button
    public void channelPlus(){
        try {
            //Channel ++ Request sent to TV
            request.execute("channelMain=8b");
        }
        catch(IOException e){}
        catch(JSONException je){}
    }

    //- Button
    public void channelMinus(){
        try {
            //Channel -- Request sent to TV
            request.execute("channelMain=8a");
        }
        catch(IOException e){}
        catch(JSONException je){}
    }


}   //End of File
package com.gruber.hendrik.tiehwie;

import android.content.Context;
import android.content.SharedPreferences;
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

    //Persistence
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    //___________

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

    String rightHandMode = "";
    private boolean rightHanded = true;

    int volumeValue = 0;

    public static String ipConnect = "";
    public static String lastChannel = "";

    private ConnectionHandler connect;
    HttpRequest request;
    PersistenceHandler pers;
    MainSettings settings;

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

        connect = new ConnectionHandler();

        //Get Buttons
        getButtons();

        //ConnectionHander
        loadIp();
        loadChannel();
        request = new HttpRequest(ipConnect, 1000, true);
        if(!lastChannel.equals("") && !ipConnect.equals("")){
            try{
                request.execute("channelMain=" + lastChannel);
            }
            catch(IOException io){}
            catch(JSONException je){}

        }
        //Load Mirror Mode
        loadMirrorModes();
        if(rightHanded){
            rightHanded = false;
            mirrorScreen(mirrorScreenLeft);
        } else {
            rightHanded = true;
            mirrorScreen(mirrorScreenRight);
        }

        //Set Volume
        if(!ipConnect.equals("")){
            try{
                loadVolume();
                request.execute("volume=" + volumeValue);
            }
            catch(IOException io){}
            catch(JSONException je){}
        }

        // perform seek bar change listener event used for getting the progress value
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!ipConnect.equals("")){
                    try {
                        //Volume Range 0 - 100
                        progress = progress / 25;
                        volumeValue = progress;
                        //Change Volume
                        request.execute("volume=" + progress);
                    }
                    catch(IOException e){}
                    catch(JSONException je){}
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveMirrorMode();
        saveChannel();
        saveVolume();
        saveIp();
    }

    public void buttonClick(View v) throws IOException, JSONException {

        if(v == settingsButton){
            startActivity(new Intent(this, MainSettings.class));
        }
        if(v == favoritesButton){
            startActivity(new Intent(this, ChannelList.class));
        }
        if(v == pipButton){
            startActivity(new Intent(this, PicInPic.class));
        }
        if(v == nightModeButton){
        }
        if(v == playButton){
            connect.playPause();
        }
        if(v == channelPlusButton){
            connect.channelPlus();
        }
        if(v == channelMinusButton){
            connect.channelMinus();
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
        }
        else{
            rightHanded = false;
            setContentView(R.layout.activity_main_left);
            getButtons();
        }
        saveMirrorMode();
    }

    public void saveMirrorMode(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        if(rightHanded){
            rightHandMode = "true";
        } else {
            rightHandMode = "false";
        }
        preferenceEditor.putString("Mirror", rightHandMode);
        preferenceEditor.commit();
    }

    public void loadMirrorModes(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        rightHandMode = preferenceSettings.getString("Mirror", "true");
        if(rightHandMode.equals("true")){
            rightHanded = true;
        } else {
            rightHanded = false;
        }
    }

    public void saveChannel(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        lastChannel = ConnectionHandler.currentChannel;
        preferenceEditor.putString("Last Channel", lastChannel);
        preferenceEditor.commit();
    }

    public void loadChannel(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        lastChannel = preferenceSettings.getString("Last Channel", "8a");
    }

    public void saveVolume(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.putString("Volume", Integer.toString(volumeValue));
        preferenceEditor.commit();
    }

    public void loadVolume(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        String temp = "";
        temp = preferenceSettings.getString("Volume", "50");
        volumeValue = Integer.parseInt(temp);
    }

    public void saveIp(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        ipConnect = MainSettings.input;
        preferenceEditor.putString("IP Main", ipConnect);
        preferenceEditor.commit();
    }

    public void loadIp(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        ipConnect = preferenceSettings.getString("IP Main", "");
    }

}   //End of File
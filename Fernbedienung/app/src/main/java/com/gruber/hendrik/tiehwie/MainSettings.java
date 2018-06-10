package com.gruber.hendrik.tiehwie;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import java.io.IOException;
import java.nio.channels.Channel;
import java.sql.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

public class MainSettings extends AppCompatActivity {

    //Persistence
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    //___________

    Button powerButton;
    Button connectIp;
    Button scanChannels;
    Button resetFavs;
    EditText ipInput;

    CountUpTimer countUp;

    private ConnectionHandler connect;
    private HttpRequest request;

    public static String input = "";
    public Boolean isStandBy = false;
    private Boolean isCounting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        powerButton = (Button) findViewById(R.id.powerButton);
        connectIp = (Button) findViewById(R.id.connectNewIp);
        scanChannels = (Button) findViewById(R.id.channelSearch);
        resetFavs = (Button) findViewById(R.id.favoritesReset);
        ipInput = (EditText) findViewById(R.id.IPInput);


        connect = new ConnectionHandler();
        request = new HttpRequest(input, 1000, true);

        loadIp();
        loadChannels();
        if(!input.equals("")){
            ipInput.setText(input);
            if(PersistenceHandler.channelList.size() == 0 && PersistenceHandler.channlName.size() == 0){
                scanChannels();
            }
        }

        //Create Timer for Power Off
        countUp = new CountUpTimer();

        powerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isCounting){
                    counter();
                }
                return false;
            }
        });
    }


    public void counter(){
        isCounting = true;
        countUp.startCounting();
    }

    public void changeIP(View v){
        input = ipInput.getText().toString();   //Read from EditText
        saveIp();   //Save new IP Address
        MainActivity.ipConnect = input; //Direct access to ipConnect because it's a public static
        ConnectionHandler.currentIp = input;
        if(PersistenceHandler.channelList.size() != 0)
            startActivity(new Intent(this, MainActivity.class));    //Jump To Main Activity after done inputting IP
    }
    public String getIp(){
        loadIp();
        return input;
    }

    public void callChannelScan(View v){
        scanChannels();
    }

    public void scanChannels(){
        ConnectionHandler.channelScan();
        saveChannels();
        if(PersistenceHandler.channelList.size() != 0)
            startActivity(new Intent(this, MainActivity.class));
    }

    public void saveIp(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.putString("IP Address", input);
        preferenceEditor.commit();
    }

    public void loadIp(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        input = preferenceSettings.getString("IP Address", "");
    }

    public void loadChannels(){
        String list, name;
        //Clear Both Lists
        PersistenceHandler.channelList.clear();
        PersistenceHandler.channlName.clear();
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        for(int i = 0; i < 33; i++){
            //Get Values
            list = preferenceSettings.getString("Channel ID #" + i, "");
            name = preferenceSettings.getString("Channel Name #" + i, "");
            //Add Values to Array
            PersistenceHandler.channelList.add(list);
            PersistenceHandler.channlName.add(name);
        }
    }

    public void saveChannels(){
        Log.i("Saving", "...");
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        for(int i = 0; i < PersistenceHandler.channelList.size(); i++){
            preferenceEditor.putString("Channel ID #" + i, PersistenceHandler.channelList.get(i).toString());
            preferenceEditor.putString("Channel Name #" + i, PersistenceHandler.channlName.get(i).toString());
            preferenceEditor.commit();
        }

    }

    public void powerOff(View v){
        countUp.stopCounting();
        isCounting = false;
        long timer = countUp.getTime();
        if(!input.equals("")) {
            if (timer < 1) {
                if (!input.equals("")) {
                    if (!isStandBy) {
                        try {
                            //Start Pip
                            request.execute("standby=1");
                        } catch (IOException e) {
                        } catch (JSONException je) {
                        }
                        isStandBy = true;
                    } else {
                        try {
                            //Zoom in on Main Picture
                            request.execute("standby=0");
                        } catch (IOException e) {
                        } catch (JSONException je) {
                        }
                        isStandBy = false;
                    }
                }
            } else {
                try {
                    //Start Pip
                    request.execute("powerOff=1");
                } catch (IOException e) {
                } catch (JSONException je) {
                }
            }
        }
    }
}
package com.gruber.hendrik.tiehwie;

import android.util.Log;
import org.json.JSONException;
import java.io.IOException;
import java.sql.Connection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.support.v4.content.ContextCompat.startActivity;

public class ConnectionHandler {

    private boolean isPaused;

    CountUpTimer countUp;
    private HttpRequest request;
    private MainSettings settings = new MainSettings();

    ConnectionHandler(){
        //TV is playing by default
        isPaused = false;

        //Get IP Address From Settings Menu
        String newIp = settings.getIp();

        //Establish TV Connection
        establishConnection("");

        //Initialize Counter for Timeshift
        countUp = new CountUpTimer();
    }

    public void establishConnection(String newIp){
        request = new HttpRequest(newIp, 1000, true);
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




}

package com.gruber.hendrik.tiehwie;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.startActivity;

public class ConnectionHandler {

    private boolean isPaused;

    CountUpTimer countUp;
    private static HttpRequest request;
    private MainSettings settings = new MainSettings();
    private PersistenceHandler persistenceHandler = new PersistenceHandler();

    public static String currentChannel = "8a";
    public static String currentIp = MainSettings.input;

    private int currentIndex = 0;
    private static ArrayList<String> channels = PersistenceHandler.channelList;

    ConnectionHandler(){
        //TV is playing by default
        isPaused = false;

        //Initialize Counter for Timeshift
        countUp = new CountUpTimer();
    }

    public static void establishConnection(String newIp){
        request = new HttpRequest(newIp, 1000, true);
    }


    //Timeshift Control
    public void playPause(){
        if(!currentIp.equals("")) {
            if (!isPaused) {
                try {
                    //Pause Request sent to TV
                    request.execute("timeShiftPause=");
                } catch (IOException e) {
                } catch (JSONException je) {
                }

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
                } catch (IOException e) {
                } catch (JSONException je) {
                }
            }
        }
    }

    //+ Button
    public void channelPlus(){
        if(!currentIp.equals("")){
            String nextChannel = "";
            if(currentIndex < 32){
                currentIndex = getChannelIndex() + 1;
                nextChannel = channels.get(currentIndex);
                try {
                    Log.i("Next", nextChannel);
                    //Channel ++ Request sent to TV
                    request.execute("channelMain=" + nextChannel);
                    currentChannel = nextChannel;
                    MainActivity.lastChannel = currentChannel;
                }
                catch(IOException e){}
                catch(JSONException je){}
            }
        }
    }

    //- Button
    public void channelMinus(){
        if(!currentIp.equals("")) {
            String nextChannel = "";
            if (currentIndex > 0) {
                currentIndex = getChannelIndex() - 1;
                nextChannel = channels.get(currentIndex);
                try {
                    Log.i("Next", nextChannel);
                    //Channel ++ Request sent to TV
                    request.execute("channelMain=" + nextChannel);
                    currentChannel = nextChannel;
                    MainActivity.lastChannel = currentChannel;
                } catch (IOException e) {
                } catch (JSONException je) {
                }
            } else {
                Log.i("Error", "Channel++ Overflow");
            }
        }
    }

    public int getChannelIndex(){
        channels = persistenceHandler.channelList;
        if(channels.contains(currentChannel)){
            Log.i("Contains?", "yes");
            return channels.indexOf(currentChannel);
        } else {
            Log.i("Contains?", "no");
        }
        return 0;
    }

    //Channel Scanning
    public static void channelScan(){
        currentIp = MainSettings.input;
        if (!currentIp.equals("")){
                try {
                //Channel -- Request sent to TV
                establishConnection(currentIp);
                JSONObject getConsole = request.execute("scanChannels=");
                //Log.i("Console", getConsole.toString());

                //Calls Persistence Handler to save JSON Objects from the Channel List
                PersistenceHandler.saveChannels(getConsole);
            }
            catch(IOException e){
            }
            catch(JSONException je){
            }
        }
        currentIp = MainSettings.input;

    }

    public static String getCurrentChannel(){
        return currentChannel;
    }
}

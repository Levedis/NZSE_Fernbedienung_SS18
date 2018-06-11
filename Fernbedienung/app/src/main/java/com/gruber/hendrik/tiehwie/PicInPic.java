package com.gruber.hendrik.tiehwie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;

public class PicInPic extends AppCompatActivity {

    Button aspectRatio;
    Button startPip;
    Button zoomPip;
    Button switchPip;

    HttpRequest request;

    String currentIp = MainSettings.input;

    public static Boolean pipChannel = false;

    Boolean isZoomed = false;
    String zoomed = "";
    Boolean pipIsZoomed = false;
    String pipZoom = "";
    Boolean pipIsOn = false;
    String pipOn = "";

    Boolean fromCreate = false;

    public static String currentPip = "";
    public static String currentMain = "";

    //Persistence
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    //___________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_in_pic);

        //Get Buttons
        aspectRatio = (Button) findViewById(R.id.aspectRatioButton);
        startPip = (Button) findViewById(R.id.pipButton);
        zoomPip = (Button) findViewById(R.id.CancelButton);
        switchPip = (Button) findViewById(R.id.ReverseButton);

        getIp();
        request = new HttpRequest(currentIp, 1000, true);

        //Load PiP
        loadPip();
        fromCreate = true;
        if(pipIsOn){
            pipIsOn = false;
            togglePip(startPip);
        } else {
            pipIsOn = true;
            togglePip(startPip);
        }
        fromCreate = false;

        //Load Zoom Main
        loadMainZoom();
        if(isZoomed){
            isZoomed = false;
            toggleZoom(aspectRatio);
        } else {
            isZoomed = true;
            toggleZoom(aspectRatio);
        }

        //Load Pip Zoom
        loadPipZoom();
        if(pipIsZoomed){
            pipIsZoomed = false;
            togglePipZoom(zoomPip);
        } else {
            pipIsZoomed = true;
            togglePipZoom(zoomPip);
        }
    }

    private void getIp(){
        currentIp = MainSettings.input;
    }

    public void toggleZoom(View v){
        if(!currentIp.equals("")){
            if(!isZoomed){
                try {
                    //Start Pip
                    request.execute("zoomMain=1");
                }
                catch(IOException e){}
                catch(JSONException je){}
                isZoomed = true;
            } else {
                try {
                    //Zoom in on Main Picture
                    request.execute("zoomMain=0");
                }
                catch(IOException e){}
                catch(JSONException je){}
                isZoomed = false;
            }
            saveMainZoom();
        }
    }

    public void togglePip(View v){
        if(!currentIp.equals("")){
            if(!pipIsOn){
                try {
                    //Start Pip
                    request.execute("showPip=1");
                }
                catch(IOException e){}
                catch(JSONException je){}
                pipIsOn = true;
                //If instantiating new PiP -> Call Channel List to choose a channel
                if(!fromCreate){
                    pipChannelSelection();
                }
            } else {
                try {
                    //Zoom in on Main Picture
                    request.execute("showPip=0");
                }
                catch(IOException e){}
                catch(JSONException je){}
                pipIsOn = false;
            }
            savePip();
        }
    }

    public void pipChannelSelection(){
            pipChannel = true;
            startActivity(new Intent(this, ChannelList.class));
    }

    public void switchPip(View v){
        if(!currentIp.equals("") && pipIsOn){
            currentMain = ConnectionHandler.getCurrentChannel();
            String tempSwap = "";
            try {
                //Switch PiP and Main
                request.execute("channelPip=" + currentMain);
                request.execute("channelMain=" + currentPip);
                tempSwap = currentMain;
                currentMain = currentPip;
                ConnectionHandler.currentChannel = currentMain;
                currentPip = tempSwap;
            }
            catch(IOException e){}
            catch(JSONException je){}
        }
    }

    public void togglePipZoom(View v){
        if(!currentIp.equals("")){
            if(!pipIsZoomed){
                try {
                    //Start Pip
                    request.execute("zoomPip=1");
                }
                catch(IOException e){}
                catch(JSONException je){}
                pipIsZoomed = true;
            } else {
                try {
                    //Zoom in on Main Picture
                    request.execute("zoomPip=0");
                }
                catch(IOException e){}
                catch(JSONException je){}
                pipIsZoomed = false;
            }
            savePipZoom();
        }
    }

    public void savePip(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        if(pipIsOn){
            pipOn = "true";
        } else {
            pipOn = "false";
        }
        preferenceEditor.putString("Pip", pipOn);
        preferenceEditor.commit();
    }

    public void loadPip(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        pipOn = preferenceSettings.getString("Pip", "");
        if(pipOn.equals("true")){
            pipIsOn = true;
        } else {
            pipIsOn = false;
        }
    }

    public void saveMainZoom(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        if(isZoomed){
            zoomed = "true";
        } else {
            zoomed = "false";
        }
        preferenceEditor.putString("Zoomed Main", zoomed);
        preferenceEditor.commit();
    }

    public void loadMainZoom(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        zoomed = preferenceSettings.getString("Zoomed Main", "");
        if(zoomed.equals("true")){
            isZoomed = true;
        } else {
            isZoomed = false;
        }
    }

    public void savePipZoom(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        if(pipIsZoomed){
            pipZoom = "true";
        } else {
            pipZoom = "false";
        }
        preferenceEditor.putString("Zoomed Pip", zoomed);
        preferenceEditor.commit();
    }

    public void loadPipZoom(){
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        pipOn = preferenceSettings.getString("Zoomed Pip", "");
        if(pipZoom.equals("true")){
            pipIsZoomed = true;
        } else {
            pipIsZoomed = false;
        }
    }


}   //End of PicInPic

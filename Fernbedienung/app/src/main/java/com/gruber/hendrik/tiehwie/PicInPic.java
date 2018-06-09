package com.gruber.hendrik.tiehwie;

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
    Button stopPip;
    Button switchPip;

    HttpRequest request;

    String currentIp = MainSettings.input;

    Boolean isZoomed = false;
    Boolean pipIsZoomed = false;
    Boolean pipIsOn = false;

    private String currentPip = "";
    public static String currentMain = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_in_pic);

        //Get Buttons
        aspectRatio = (Button) findViewById(R.id.aspectRatioButton);
        startPip = (Button) findViewById(R.id.pipButton);
        stopPip = (Button) findViewById(R.id.CancelButton);
        switchPip = (Button) findViewById(R.id.ReverseButton);

        getIp();

        request = new HttpRequest(currentIp, 1000, true);
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

                //Select which channel will be displayed in the pip
                pipChannelSelection();

            } else {
                try {
                    //Zoom in on Main Picture
                    request.execute("showPip=0");
                }
                catch(IOException e){}
                catch(JSONException je){}
                pipIsOn = false;
            }
        }
    }

    public void pipChannelSelection(){
        try{
            currentPip = "64c";
            request.execute("channelPip=" + currentPip);
        }
        catch(IOException e){}
        catch(JSONException je){}
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
        }
    }


}   //End of PicInPic

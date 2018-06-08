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
    Boolean pipIsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_in_pic);

        //Get Buttons
        aspectRatio = (Button) findViewById(R.id.aspectRatioButton);
        startPip = (Button) findViewById(R.id.pipButton);
        stopPip = (Button) findViewById(R.id.CancelButton);
        switchPip = (Button) findViewById(R.id.ReverseButton);

        //Establish Connection for Requests
        establishConnection();
    }

    private void establishConnection(){
        request = new HttpRequest(currentIp, 10000, true);
    }

    private void toggleZoom(View v){
        if(!currentIp.equals("")){
            if(!isZoomed){
                    try {
                        //Zoom in on Main Picture
                        request.execute("zoomMain=1");
                    }
                    catch(IOException e){}
                    catch(JSONException je){}
                    isZoomed = true;
            } else {
                try {
                    //Zoom out of Main Picture
                    request.execute("zoomMain=0");
                }
                catch(IOException e){}
                catch(JSONException je){}
                isZoomed = false;
            }
        }
    }

    private void togglePip(View v){
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

    private void pipChannelSelection(){
        Log.i("TODO", "PiP Channel Select");
    }

    private void switchPip(View v){
        Log.i("TODO", "Switch PiP and Main Picture");
    }


}//End of PicInPic

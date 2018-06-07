package com.gruber.hendrik.tiehwie;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.Object;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.widget.Toast;
import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class PersistenceHandler{

    public static ArrayList<String> channelList = new ArrayList<>();

    public static void saveChannels(JSONObject channels){

        try {
            JSONArray parentArray = channels.getJSONArray("channels");
            if (parentArray.length() == 0) {
                Log.i("Error: ", "No Students Found.");
            } else {
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    String frequency = finalObject.getString("frequency");
                    String channelName = finalObject.getString("channel");
                    String quality = finalObject.getString("quality");
                    String program = finalObject.getString("program");
                    String provider = finalObject.getString("provider");
                    channelList.add(channelName);
                }
            }
        }
        catch(JSONException ja){}
        //for(int i = 0; i < channelList.length; i++)
        //    Log.i(Integer.toString(i), channelList[i]);
    }


}//End of Persistence Handler

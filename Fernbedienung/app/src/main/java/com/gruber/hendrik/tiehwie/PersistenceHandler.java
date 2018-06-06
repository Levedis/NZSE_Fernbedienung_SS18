package com.gruber.hendrik.tiehwie;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import android.content.Context;

import org.json.JSONObject;

public class PersistenceHandler {

    public String path = "Users/Hendrik/Desktop/Fernbedienung/app/src/main/java/com/gruber/hendrik/tiehwie";


    public void writeToChannels(String channels){
        File file = new File (path + "channels.txt");


    }
}

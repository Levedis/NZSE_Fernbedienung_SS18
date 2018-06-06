package com.gruber.hendrik.tiehwie;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import java.nio.channels.Channel;
import java.sql.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MainSettings extends AppCompatActivity {

    Button powerButton;
    EditText ipInput;

    private ConnectionHandler connect;

    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        powerButton = (Button) findViewById(R.id.powerButton);
        ipInput = (EditText) findViewById(R.id.IPInput);

        connect = new ConnectionHandler();
    }

    public void changeIP(View v){
        Log.i("Button Pressed: ", "Change IP");
        input = ipInput.getText().toString();   //Read from EditText
        MainActivity.ipConnect = input; //Direct access to ipConnect because it's a public static
        startActivity(new Intent(this, MainActivity.class));    //Jump To Main Activity after done inputting IP
    }
    public String getIp(){
        return input;
    }

    public void scanChannels(View v){
        Log.i("Button Pressed: ", "Scanning Channels...");
        connect.channelScan();
        //startActivity(new Intent(this, ChannelList.class));    //Jump To Channel List Activity after done Scaning Channels
    }

    public void shutDown(View v){
        finish();
        System.exit(0);
    }
}
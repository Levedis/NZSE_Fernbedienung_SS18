package com.gruber.hendrik.tiehwie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ChannelList extends AppCompatActivity {

    SearchView search;
    ListView channelList;
    ListView favoritesList;
    CharSequence query;

    public ArrayList<String> channelId;
    public ArrayList<String> channelName;

    HttpRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);

        request = new HttpRequest(MainSettings.input, 1000, true);

        search = (SearchView) findViewById(R.id.searchBar);
        channelList = (ListView) findViewById(R.id.channelList);
        query = search.getQuery();      //Get content within searchview

        channelId = PersistenceHandler.channelList;
        channelName = PersistenceHandler.channlName;

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, channelName);

        // DataBind ListView with items from ArrayAdapter
        channelList.setAdapter(arrayAdapter);

        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = channelId.get(position);
                if (!MainSettings.input.equals("")) {
                    try {
                        if(PicInPic.pipChannel){
                            request.execute("channelPip=" + item);
                            PicInPic.currentPip = item;
                            PicInPic.pipChannel = false;
                            finish();
                        } else {
                            request.execute("channelMain=" + item);
                            ConnectionHandler.currentChannel = item;
                            MainActivity.lastChannel = item;
                        }
                    } catch (IOException e) {
                    } catch (JSONException je) {
                    }
                }
            }
        });
    }



}

package com.example.chenningzhang.yourfault;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    protected static Long lastUpdatedTime;
    protected static JSONArray recentEarthquakes;
    protected static ArrayList<String> earthquakeList;
    protected static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            firstUpdate();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        refreshEarthquakes();
    }

    private void firstUpdate() throws ExecutionException, InterruptedException, JSONException {
        String urlPath = getString(R.string.base_url);
        EarthquakeLookupTask earthquakeLookupTask = new EarthquakeLookupTask();
        recentEarthquakes = earthquakeLookupTask.execute(urlPath).get();
        lastUpdatedTime = Long.valueOf(recentEarthquakes.getJSONObject(0).getJSONObject("properties").getString("time"));
        earthquakeList = EarthquakeLookupTask.getEarthquakesInList(recentEarthquakes);
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, earthquakeList);
        listView = (ListView) findViewById(R.id.earthquakeList);
        listView.setAdapter(arrayAdapter);

        Log.d("FIRST UPDATE", earthquakeList.toString());
    }

    private void refreshEarthquakes() {
        Log.d("START SERVICE", "SERVICE STARTED");
        startService(new Intent(this, EarthquakeService.class));
    }

}

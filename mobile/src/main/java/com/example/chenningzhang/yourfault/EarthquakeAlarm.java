package com.example.chenningzhang.yourfault;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by ChenningZhang on 11/3/15.
 */
public class EarthquakeAlarm extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARM RECEIVER", "onReceiver called");
        try {
            refreshEarthquakes(context);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshEarthquakes(Context context) throws ExecutionException, InterruptedException, JSONException {
        Date date = new Date(MainActivity.lastUpdatedTime);
        String newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        String[] dateArr = newDate.split(" ");
        String queryParam = "&starttime=" + dateArr[0] + "T" + dateArr[1];
        String requestURL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson" + queryParam;
        EarthquakeLookupTask earthquakeLookupTask = new EarthquakeLookupTask();
        JSONArray newEarthquakes = earthquakeLookupTask.execute(requestURL).get();
        Log.d("CONTINUOUS UPDATE", newEarthquakes.toString());

        if (newEarthquakes.length() > 0) {
            for (int i=newEarthquakes.length()-1; i >=0; i--) {
                Long earthquakesTime = Long.valueOf(newEarthquakes.getJSONObject(i).getJSONObject("properties").getString("time"));
                if (earthquakesTime > MainActivity.lastUpdatedTime) {
                    //TODO: When a new earthquake is detected
                    //recentEarthquakes.put(0, newEarthquakes.getJSONObject(i));
                    MainActivity.recentEarthquakes.put(0, newEarthquakes.getJSONObject(i));
                    MainActivity.lastUpdatedTime = earthquakesTime;
                    MainActivity.earthquakeList.add(0, parseEarthquakeObj(newEarthquakes.getJSONObject(i)));
                    ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, MainActivity.earthquakeList);
                    MainActivity.listView.setAdapter(arrayAdapter);
                }
            }
        }
    }

    private String parseEarthquakeObj(JSONObject earthquakeObj) throws JSONException {
        JSONObject propertiesObj = earthquakeObj.getJSONObject("properties");
        String mag = propertiesObj.getString("mag");
        String place = propertiesObj.getString("place");
        int numWhiteSpace = 0;
        for (int j=0; j < place.length(); j++) {
            if (place.charAt(j) == ' ') {
                numWhiteSpace++;
            }
            if (numWhiteSpace == 3) {
                place = place.substring(j+1);
                break;
            }
        }
        String earthquakeData = mag + "   " + place;
        return earthquakeData;
    }

}

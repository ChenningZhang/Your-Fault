package com.example.chenningzhang.yourfault;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ChenningZhang on 11/4/15.
 */
public class EarthquakeLookupTask extends AsyncTask<String, Void, JSONArray> {
    @Override
    protected JSONArray doInBackground(String... params) {
        return pullData(params[0]);
    }

    private JSONArray pullData(String urlPath) {
        HttpURLConnection connection = null;
        BufferedReader reader;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            JSONArray features = jsonObject.getJSONArray("features");
            return features;
        } catch (MalformedURLException e) {
            Log.e("ERROR", "Invalid URL", e);
        } catch (IOException e) {
            Log.e("ERROR", "IO/Connection Error", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return new JSONArray();
    }

    protected static ArrayList<String> getEarthquakesInList(JSONArray jsonArray) {
        ArrayList<String> resultList = new ArrayList<String>();
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                JSONObject earthquakeObj = jsonArray.getJSONObject(i);
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
                resultList.add(earthquakeData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return resultList;
    }
}

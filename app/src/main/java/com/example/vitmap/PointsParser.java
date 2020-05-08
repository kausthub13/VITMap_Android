package com.example.vitmap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    String directionMode = "driving";
    TaskLoadedCallback taskCallback;

    public PointsParser(Context mContext, String directionMode2) {
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode2;
    }

    /* access modifiers changed from: protected */
    public List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        String str = "mylog";
        List<List<HashMap<String, String>>> routes = null;
        try {
            JSONObject jObject = new JSONObject(jsonData[0]);
            Log.d(str, jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d(str, parser.toString());
            routes = parser.parse(jObject);
            Log.d(str, "Executing routes");
            Log.d(str, routes.toString());
            return routes;
        } catch (Exception e) {
            Log.d(str, e.toString());
            e.printStackTrace();
            return routes;
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(List<List<HashMap<String, String>>> result) {
        String str;
        PolylineOptions lineOptions = null;
        int i = 0;
        while (true) {
            str = "mylog";
            if (i >= result.size()) {
                break;
            }
            ArrayList<LatLng> points = new ArrayList<>();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = (List) result.get(i);
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = (HashMap) path.get(j);
                points.add(new LatLng(Double.parseDouble((String) point.get("lat")), Double.parseDouble((String) point.get("lng"))));
            }
            lineOptions.addAll(points);
            if (this.directionMode.equalsIgnoreCase("walking")) {
                lineOptions.width(10.0f);
                lineOptions.color(-65281);
            } else {
                lineOptions.width(20.0f);
                lineOptions.color(-16776961);
            }
            Log.d(str, "onPostExecute lineoptions decoded");
            i++;
        }
        if (lineOptions != null) {
            this.taskCallback.onTaskDone(lineOptions);
            return;
        }
        Log.d(str, "without Polylines drawn");
    }
}

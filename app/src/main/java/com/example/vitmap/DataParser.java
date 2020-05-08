package com.example.vitmap;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        try {
            JSONArray jRoutes = jObject.getJSONArray("routes");
            int i = 0;
            int i2 = 0;
            while (i2 < jRoutes.length()) {
                JSONArray jLegs = ((JSONObject) jRoutes.get(i2)).getJSONArray("legs");
                List path = new ArrayList();
                int j = i;
                while (j < jLegs.length()) {
                    JSONArray jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    int k = i;
                    while (k < jSteps.length()) {
                        String str = "";
                        String polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);
                        int l = i;
                        while (l < list.size()) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            String polyline2 = polyline;
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                            l++;
                            JSONObject jSONObject = jObject;
                            polyline = polyline2;
                        }
                        k++;
                        JSONObject jSONObject2 = jObject;
                        i = 0;
                    }
                    routes.add(path);
                    j++;
                    JSONObject jSONObject3 = jObject;
                    i = 0;
                }
                i2++;
                JSONObject jSONObject4 = jObject;
                i = 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
        }
        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {
        int index;
        int index2;
        int b;
        String str = encoded;
        List<LatLng> poly = new ArrayList<>();
        int b2 = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;
        while (b2 < len) {
            int shift = 0;
            int result = 0;
            while (true) {
                index = b2 + 1;
                int b3 = str.charAt(b2) - 63;
                result |= (b3 & 31) << shift;
                shift += 5;
                if (b3 < 32) {
                    break;
                }
                b2 = index;
            }
            int lat2 = lat + ((result & 1) != 0 ? ~(result >> 1) : result >> 1);
            int shift2 = 0;
            int result2 = 0;
            while (true) {
                index2 = index + 1;
                b = str.charAt(index) - 63;
                result2 |= (b & 31) << shift2;
                shift2 += 5;
                if (b < 32) {
                    break;
                }
                int i = b;
                int i2 = len;
                index = index2;
            }
            lng += (result2 & 1) != 0 ? ~(result2 >> 1) : result2 >> 1;
            int i3 = b;
            int len2 = len;
            poly.add(new LatLng(((double) lat2) / 100000.0d, ((double) lng) / 100000.0d));
            b2 = index2;
            lat = lat2;
            len = len2;
        }
        return poly;
    }
}

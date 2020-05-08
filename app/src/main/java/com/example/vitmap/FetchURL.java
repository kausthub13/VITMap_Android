package com.example.vitmap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchURL extends AsyncTask<String, Void, String> {
    String directionMode = "driving";
    Context mContext;

    public FetchURL(Context mContext2) {
        this.mContext = mContext2;
    }

    /* access modifiers changed from: protected */
    public String doInBackground(String... strings) {
        String data = "";
        this.directionMode = strings[1];
        try {
            data = downloadUrl(strings[0]);
            StringBuilder sb = new StringBuilder();
            sb.append("Background task data ");
            sb.append(data.toString());
            Log.d("mylog", sb.toString());
            return data;
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
            return data;
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String s) {
        super.onPostExecute(s);
        new PointsParser(this.mContext, this.directionMode).execute(new String[]{s});
    }

    private String downloadUrl(String strUrl) throws IOException {
        String str = "mylog";
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String str2 = "";
            while (true) {
                String readLine = br.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                sb.append(line);
            }
            data = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Downloaded URL: ");
            sb2.append(data.toString());
            Log.d(str, sb2.toString());
            br.close();
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Exception downloading URL: ");
            sb3.append(e.toString());
            Log.d(str, sb3.toString());
        } catch (Throwable th) {
            iStream.close();
            urlConnection.disconnect();
            throw th;
        }
        iStream.close();
        urlConnection.disconnect();
        return data;
    }
}

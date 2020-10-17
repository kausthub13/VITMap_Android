package com.example.vitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowBlockLayout extends AppCompatActivity {
    PhotoView img;
    String start_room,dest_room,floor,building,start_coord,end_coord,full_path,dest_floor,stair_name;
    Bitmap imgmap,testing;
    Button draw_path_ready;
    int[] start_coord_arr,end_coord_arr;
    TextView txtString,fullPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_block_layou);
        img = findViewById(R.id.block_layout);
        txtString = findViewById(R.id.txtString);
        fullPath = findViewById(R.id.full_path);
        Intent i = getIntent();
        building = i.getStringExtra("block");
        start_room = i.getStringExtra("start");
        dest_room = i.getStringExtra("dest");
        floor = i.getStringExtra("floor");
        dest_floor = i.getStringExtra("dest_floor");
        draw_path_ready = findViewById(R.id.draw_path);
        draw_path_ready.setEnabled(false);

//        img.setImageResource(getResources().getIdentifier(building+"_"+floor,"drawable",getPackageName()));
        start_coord_arr = getCoords(setCoords(start_room));
        if(floor.equals(dest_floor)) {

            end_coord_arr = getCoords(setCoords(dest_room));
            Button dest_flr_btn = findViewById(R.id.dest_flr_btn);
            dest_flr_btn.setEnabled(false);
        }
        else
        {
            if(building.equals("sjt"))
            {
                int lf1[] = getCoords(setCoords("lf1"));
                int lf2[] = getCoords(setCoords("lf2"));
                int lf3[] = getCoords(setCoords("lf3"));
                int lf4[] = getCoords(setCoords("lf4"));
                int st1[] = getCoords(setCoords("st1"));
                int st2[] = getCoords(setCoords("st2"));
                int st3[] = getCoords(setCoords("st3"));
                int st4[] = getCoords(setCoords("st4"));
                int st5[] = getCoords(setCoords("st5"));
                int min_dist = getDistance(start_coord_arr,lf1);
                stair_name = "LF1";
                end_coord_arr = lf1;
                if(min_dist>getDistance(start_coord_arr,lf2))
                {
                    min_dist = getDistance(start_coord_arr,lf2);
                    stair_name = "LF2";
                    end_coord_arr = lf2;
                }
                if(min_dist>getDistance(start_coord_arr,lf3))
                {
                    min_dist = getDistance(start_coord_arr,lf3);
                    stair_name = "LF3";
                    end_coord_arr = lf3;
                }
                if(min_dist>getDistance(start_coord_arr,lf4))
                {
                    min_dist = getDistance(start_coord_arr,lf4);
                    stair_name = "LF4";
                    end_coord_arr = lf4;
                }
                if(min_dist>getDistance(start_coord_arr,st1))
                {
                    min_dist = getDistance(start_coord_arr,st1);
                    stair_name = "ST1";
                    end_coord_arr = st1;
                }
                if(min_dist>getDistance(start_coord_arr,st2))
                {
                    min_dist = getDistance(start_coord_arr,st2);
                    stair_name = "ST2";
                    end_coord_arr = st2;
                }
                if(min_dist>getDistance(start_coord_arr,st3))
                {
                    min_dist = getDistance(start_coord_arr,st3);
                    stair_name = "ST3";
                    end_coord_arr = st3;
                }
                if(min_dist>getDistance(start_coord_arr,st4))
                {
                    min_dist = getDistance(start_coord_arr,st4);
                    stair_name = "ST4";
                    end_coord_arr = st4;
                }
                if(min_dist>getDistance(start_coord_arr,st5))
                {
                    min_dist = getDistance(start_coord_arr,st5);
                    stair_name = "ST5";
                    end_coord_arr = st5;
                }

            }
            else if(building.equals("smv"))
            {
                int st1[] = getCoords(setCoords("st1"));
                int st2[] = getCoords(setCoords("st2"));
                int min_dist = getDistance(start_coord_arr,st1);
                stair_name = "ST1";
                end_coord_arr = st1;
                if(min_dist>getDistance(start_coord_arr,st2))
                {
                    stair_name = "ST2";
                    end_coord_arr = st2;
                }
            }
        }
        imgmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(building+"_"+floor,"drawable",getPackageName()) );
        img.setImageBitmap(imgmap);
        testing= drawRectangles(imgmap,start_coord_arr,end_coord_arr);
        img.setImageBitmap(testing);
    }

    public void destFloorIntent(View v)
    {
        Intent i = new Intent(getApplicationContext(),ShowBlockLayout.class);
        i.putExtra("block",building);
        i.putExtra("start",stair_name);
        i.putExtra("dest",dest_room);
        i.putExtra("floor",dest_floor);
        i.putExtra("dest_floor",dest_floor);
        startActivity(i);

    }

    public int getDistance(int arr1[],int arr2[])
    {
        return Math.abs(arr1[0]-arr2[0]) + Math.abs(arr1[1]-arr2[1]);
    }

    public String setCoords(String room)
    {
        try {
            InputStreamReader fr = new InputStreamReader(getAssets().open(building+"_"+floor+".txt"));
            BufferedReader br = new BufferedReader(fr);
            String json = br.readLine();
            JSONObject floor_json = new JSONObject(json);
            return floor_json.getString(room.toUpperCase());
//            start_coord = floor_json.getString(start_room.toUpperCase());
//            end_coord = floor_json.getString(dest_room.toUpperCase());
//            Log.d("Start coord",start_coord);
//            Log.d("End Coord",end_coord);
//            TextView start = findViewById(R.id.start_coord);
//            TextView end = findViewById(R.id.goto_coord);
//            start.setText(start_coord);
//            end.setText(end_coord);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Not Found";
    }

    public int[] getCoords(String coord)
    {
        String arr[] = coord.split(" ");
        int ret_coord[] = new int[arr.length];
        for(int i = 0;i<arr.length;i++)
        {
            ret_coord[i] = Integer.valueOf(arr[i]);
        }
        return ret_coord;

    }

    public Bitmap drawRectangles(Bitmap bm,int []start_coord_arr,int []end_coord_arr)
    {
        Bitmap new_bm = bm.copy(bm.getConfig(),true);
        Paint green = new Paint();
        green.setStyle(Paint.Style.STROKE);
        green.setColor(Color.GREEN);
        Paint yellow = new Paint();
        yellow.setStyle(Paint.Style.STROKE);
        yellow.setColor(Color.YELLOW);
        Canvas c = new Canvas(new_bm);
        c.drawRect(new Rect(start_coord_arr[0]-5,start_coord_arr[1]-5,start_coord_arr[4]+5,start_coord_arr[5]+5),green);
        c.drawText("Start Location",start_coord_arr[0]-5,start_coord_arr[1]-10,green);
        c.drawRect(new Rect(end_coord_arr[0]-5,end_coord_arr[1]-5,end_coord_arr[4]+5,end_coord_arr[5]+5),yellow);
        c.drawText("Destination Location",end_coord_arr[0]-5,end_coord_arr[1]-10,yellow);
        return new_bm;
    }

    public void drawRequest(View v)
    {
        String base_url = "https://vitmap-python-api.herokuapp.com/";
        String request = start_coord_arr[0]+"_"+start_coord_arr[1]+"_"+end_coord_arr[0]+"_"+end_coord_arr[1]+"_"+building+floor+"b";
        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(base_url+request);
        draw_path_ready.setEnabled(true);
    }

    public void getPathRequest(View v)
    {
        String base_url = "https://vitmap-python-api.herokuapp.com/status/";
        String id = txtString.getText().toString();
        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(base_url+id);
    }

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(txtString.getText().toString().equals("Not Ready")) {
                txtString.setText(s);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
            else if(s.length() > 40 )
            {
//                txtString.setText(s);
                fullPath.setText(s);
                full_path = s;

                if(s.length() > 40) {
                    String short_show = full_path.substring(0,20);
                    full_path = s;
                    Toast.makeText(getApplicationContext(),short_show,Toast.LENGTH_SHORT).show();
                    Bitmap temp = drawPath(testing, full_path);
                    img.setImageBitmap(temp);
                }
                else
                    Toast.makeText(getApplicationContext(),"Path Not Ready",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Path Not Ready",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap drawPath(Bitmap bm, String path)
    {
        Bitmap new_bm = bm.copy(bm.getConfig(),true);
        Canvas c = new Canvas(new_bm);
        Paint red = new Paint();
        red.setStyle(Paint.Style.STROKE);
        red.setStrokeWidth(5);
        red.setColor(Color.RED);
        String int_coords[] = path.split(" ");
        for(int i = 100;i<int_coords.length-100;i+=2)
        {
            c.drawPoint(Integer.valueOf(int_coords[i]),Integer.valueOf(Integer.valueOf(int_coords[i+1])),red);
        }
        return new_bm;
    }
}

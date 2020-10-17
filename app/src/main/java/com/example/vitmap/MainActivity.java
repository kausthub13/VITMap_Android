package com.example.vitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button block,maps;
    RadioGroup building,gmaps;
    RadioButton block_select,maps_select;
    EditText block_start,block_dest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void showBlock(View v)
    {
        building = findViewById(R.id.block_grp);
        String block_name="";
        try{
            int sel_block = building.getCheckedRadioButtonId();
            block_select = findViewById(sel_block);
            block_name = block_select.getText().toString().toLowerCase();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Select A Building", Toast.LENGTH_SHORT).show();
        }

        block_start = findViewById(R.id.block_start);
        block_dest = findViewById(R.id.block_dest);

        String start_room = block_start.getText().toString().toLowerCase();
        String dest_room = block_dest.getText().toString().toLowerCase();
        String floor = Character.toString(start_room.charAt(0));
        String dest_floor = Character.toString(dest_room.charAt(0));
        if(!block_name.isEmpty()) {
            if (start_room.length() == 3 && dest_room.length() == 3) {
                Intent i = new Intent(getApplicationContext(), ShowBlockLayout.class);
                i.putExtra("block", block_name);
                i.putExtra("start", start_room);
                i.putExtra("dest", dest_room);
                i.putExtra("floor", floor);
                i.putExtra("dest_floor", dest_floor);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Room Number", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Select A Building", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMaps(View v)
    {
        Intent i = new Intent(getApplicationContext(),MapActivity.class);
        gmaps = (RadioGroup) findViewById(R.id.maps_grp);
        int sel_pos = gmaps.getCheckedRadioButtonId();
        maps_select = findViewById(sel_pos);
        if(maps_select.getText().toString().equals("Away")) {
            building = findViewById(R.id.block_grp);
            int sel_block = building.getCheckedRadioButtonId();
            block_select = findViewById(sel_block);
            String block_name = block_select.getText().toString();
            i.putExtra("block", block_name);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You are near your destination proceed with show block layout",Toast.LENGTH_SHORT).show();
        }
    }

}

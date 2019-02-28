package com.example.reenamaryputhota.treasuryservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.reenamaryputhota.common.DailyCash;

import java.util.ArrayList;

public class TreasuryDBActivity extends AppCompatActivity {
    private OpenHelper mDbHelper;
    DailyCashAdapter dailyCashAdapter;
    Button insert, read;
    ArrayList<DailyCash> dailyCashes;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasury_db);

        // Create a new DatabaseHelper
        mDbHelper = new OpenHelper(this);
        insert = findViewById(R.id.insert);
        read = findViewById(R.id.read);
        listView= findViewById(R.id.list);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDbHelper.insertRecords()){
                    insert.setClickable(false);
                    read.setVisibility(View.VISIBLE);
                }

            }
        });


        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dailyCashes = mDbHelper.readRecords(2017,1,17,20);
                Log.e("TreasuryDBActivity",dailyCashes.size() +"");
                dailyCashAdapter= new DailyCashAdapter(dailyCashes,getApplicationContext());
                listView.setAdapter(dailyCashAdapter);
            }
        });






        // start with an empty database
        //clearAll();

        // Insert records

        //insertRecords();
    }
    @Override
    protected void onDestroy() {

        mDbHelper.deleteTable();
        mDbHelper.deleteDatabase();

        super.onDestroy();

    }

}

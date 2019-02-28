package com.example.reenamaryputhota.treasury;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.reenamaryputhota.common.DailyCash;

import java.util.ArrayList;

public class TreasuryDetailsActivity extends AppCompatActivity {

    DailyCashAdapter dailyCashAdapter;
    ListView listView;
    ArrayList<DailyCash> dailyCashes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasury_details);


        listView = findViewById(R.id.list);

        Intent intent = getIntent();
        dailyCashes = intent.getParcelableArrayListExtra("dailyCashes");

        dailyCashAdapter = new DailyCashAdapter(dailyCashes,this);
        dailyCashAdapter= new DailyCashAdapter(dailyCashes,getApplicationContext());
        listView.setAdapter(dailyCashAdapter);



    }
}

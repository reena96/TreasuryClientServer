package com.example.reenamaryputhota.treasury;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reenamaryputhota.common.BalanceService;
import com.example.reenamaryputhota.common.DailyCash;
import com.example.reenamaryputhota.treasury.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";
    private BalanceService mBalanceService;
    private boolean mIsBound = false;


    Button createDatabase, getData;
    Spinner year, month, day;
    TextView noWorkingDays, dbCreatedMsg, handleMessage;
    LinearLayout userInputLayout;

    ArrayList<DailyCash> dailyCashes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
        noWorkingDays = findViewById(R.id.noWorkingDays);
        dbCreatedMsg = findViewById(R.id.dbCreatedMsg);

        handleMessage = findViewById(R.id.message);

        userInputLayout = findViewById(R.id.userInputLayout);

        createDatabase = findViewById(R.id.createDatabase);
        getData = findViewById(R.id.getData);

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {


                int index = arg0.getSelectedItemPosition() + 1;
                int array_resourceID;
                ArrayList<Integer> thirtyOne = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));
                ArrayList<Integer> thirty = new ArrayList<>(Arrays.asList(4, 6, 9, 11));
                ArrayList<String> numbers = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.days)));

                if (thirty.contains(index)) {
                    numbers.remove("31");
                    handleMessage.setText("If month selected has 30 days, only 30 numbers will be displayed in days spinner");
                } else if (thirtyOne.contains(index)) {
                    handleMessage.setText("If month selected has 31 days, 31 numbers will be displayed in days spinner");
                } else if (index == 2) {
                    numbers.remove("31");
                    numbers.remove("30");
                    numbers.remove("29");
                    handleMessage.setText("If month selected is February, only 28 numbers will be displayed in days spinner");
                }



                ArrayAdapter adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, numbers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                day.setAdapter(adapter);
                adapter.notifyDataSetChanged();

//                Toast.makeText(getApplicationContext(),
//                        "You have selected item : " + index,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (mIsBound) { // NOT WORKING!

                        // Call KeyGenerator and get a new ID
                        if (mBalanceService.createDatabase()) {
                            dbCreatedMsg.setText("Database has been created successfully!");
                            createDatabase.setClickable(false);
                            userInputLayout.setVisibility(View.VISIBLE);
                        } else {
                            dbCreatedMsg.setText("Failure in creating database");
                        }
                    } else {

                        Log.i(TAG, "Ugo says that the service was not bound!");

                    }

                } catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }


            }
        });

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year_no = Integer.parseInt(year.getSelectedItem().toString());
                int month_no = Integer.parseInt(month.getSelectedItem().toString());
                int day_no = Integer.parseInt(day.getSelectedItem().toString());
                int noWorkingDays_no = 0;
                if (!noWorkingDays.getText().toString().equals("")) {
                    noWorkingDays_no = Integer.parseInt(noWorkingDays.getText().toString());

                    try {
                        dailyCashes = (ArrayList<DailyCash>) mBalanceService.dailyCash(year_no, month_no, day_no, noWorkingDays_no);
                        //Log.e("check_client",""+dailyCashes.get(8).getDayCloseAmt());

                        if (dailyCashes.size() == 0) {
                            Toast.makeText(getApplicationContext(), "There is no  data that matches your query", Toast.LENGTH_SHORT).show();
                        } else {
                            //Log.e("size", dailyCashes.size() + "");
                            Intent intent = new Intent(MainActivity.this, TreasuryDetailsActivity.class);
                            intent.putParcelableArrayListExtra("dailyCashes", dailyCashes);
                            startActivity(intent);
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    Log.e("selected", "" + year_no + " " + month_no + " " + day_no + " " + noWorkingDays_no + " ");
                } else {
                    noWorkingDays.setError("Specify the no of working days");
                }

            }
        });

    }


    // Bind to KeyGenerator Service
    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsBound) {

            boolean b = false;
            Intent i = new Intent(BalanceService.class.getName());

            // UB:  Stoooopid Android API-20 no longer supports implicit intents
            // to bind to a service #@%^!@..&**!@
            // Must make intent explicit or lower target API level to 19.

            //get info object which contains the package name information of the SERVICE class associated with the intent
            //@SuppressLint("WrongConstant") ResolveInfo info = getPackageManager().resolveService(i, 0);
            //ComponentName = package name + class name (set this detail using info object)
            i.setComponent(new ComponentName("com.example.reenamaryputhota.treasuryservice", "com.example.reenamaryputhota.treasuryservice.BalanceServiceImpl"));

            b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                Log.i(TAG, "Ugo says bindService() succeeded!");   // WORKING!
            } else {
                Log.i(TAG, "Ugo says bindService() failed!");
            }

        }
    }


    // Unbind from KeyGenerator Service
    @Override
    protected void onPause() {


        super.onPause();
    }


    //Anonymous class implementing ServiceConnection Interface
    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mBalanceService = BalanceService.Stub.asInterface(iservice);

            mIsBound = true;

        }

        public void onServiceDisconnected(ComponentName className) {

            mBalanceService = null;

            mIsBound = false;

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsBound) {

            unbindService(this.mConnection);

        }

    }
}

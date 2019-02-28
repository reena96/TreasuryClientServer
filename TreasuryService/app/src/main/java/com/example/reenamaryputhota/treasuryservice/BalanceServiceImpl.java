package com.example.reenamaryputhota.treasuryservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.reenamaryputhota.common.BalanceService;
import com.example.reenamaryputhota.common.DailyCash;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by reenamaryputhota on 5/1/18.
 */

public class BalanceServiceImpl extends Service {


    ArrayList<DailyCash> dailyCashes;
    OpenHelper mDbHelper = new OpenHelper(BalanceServiceImpl.this);

    // Implement the Stub for this Object
    private final BalanceService.Stub mBinder = new BalanceService.Stub() {


        // Implement the remote method
        public boolean createDatabase() {
           return mDbHelper.insertRecords();
        }

        public ArrayList<DailyCash> dailyCash(int year, int month, int day, int noWorkDays){
            dailyCashes = mDbHelper.readRecords(year,month,day,noWorkDays);
            // Log.e("check",""+dailyCashes.get(8).getDayCloseAmt());
            return dailyCashes;
        }


    };

    // Return the Stub defined above
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

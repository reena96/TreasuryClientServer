package com.example.reenamaryputhota.treasuryservice;

/**
 * Created by reenamaryputhota on 5/1/18.
 */


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.reenamaryputhota.common.DailyCash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by reenamaryputhota on 4/30/18.
 * <p>
 * 1. Year (either 2017 or 2018)
 * 2. Month (1—12)
 * 3. Day (1—31)
 * 4. Day of week (e.g., “Monday”)
 * 5. Amount held by US at day open in millions of US dollars
 * 6. Amount held by US at day close in millions of US dollars
 */

public class OpenHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;
    boolean dbCreated = false;
    /**
     * TABLE NAME
     */
    final static String TABLE_NAME = "treasury";

    /**
     * 1
     */
    final static String _ID = "_id";
    /**
     * 2
     */
    final static String YEAR = "year";
    /**
     * 3
     */
    final static String MONTH = "month";
    /**
     * 4
     */
    final static String DAY = "day";
    /**
     * 5
     */
    final static String DAY_OF_WEEK = "day_of_week";
    /**
     * 6
     */
    final static String DAY_OPEN_AMOUNT = "day_open_amount";
    /**
     * 7
     */
    final static String DAY_CLOSE_AMOUNT = "day_close_amount";

    //    final static String[] columns = { YEAR, MONTH, DAY, DAY_OF_WEEK, DAY_OPEN_AMOUNT, DAY_CLOSE_AMOUNT };
//
    final private static String CREATE_CMD =

            "CREATE TABLE " + TABLE_NAME
                    + "("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + YEAR + " INTEGER, "
                    + MONTH + " INTEGER, "
                    + DAY + " INTEGER, "
                    + DAY_OF_WEEK + " TEXT NOT NULL, "
                    + DAY_OPEN_AMOUNT + " INTEGER, "
                    + DAY_CLOSE_AMOUNT + " INTEGER "
                    + ");";

    final private static String DELETE_ROWS =

            "DELETE FROM " + TABLE_NAME;

//    private static final String DELETE_CMD =
//            "DROP TABLE IF EXISTS " + TABLE_NAME;


    final private static String DATABASE_NAME = "treasury_db";
    final private static Integer DATABASE_VERSION = 1;
    final private Context mContext;

    public OpenHelper(Context context) {
        // Always call superclass's constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // Save the context that created DB in order to make calls on that context,
        // e.g., deleteDatabase() below.
        this.mContext = context;
    }

    //The database is not actually created or opened until one of getWritableDatabase() or getReadableDatabase() is called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
        dbCreated = true;


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertRecords() {

        db = getWritableDatabase();
        if (getNumOfRows() >= 290)
            db.execSQL(DELETE_ROWS);

        AssetManager assetManager = mContext.getAssets();
        InputStream is = null;

        try {
            is = assetManager.open("treasury-io-final.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        StringTokenizer st = null;
        try {
            int i = 0;

            while ((line = reader.readLine()) != null) {
                i = i + 1;
                st = new StringTokenizer(line, ",");
                //your attributes   YEAR, MONTH, DAY, DAY_OF_WEEK, DAY_OPEN_AMOUNT, DAY_CLOSE_AMOUNT
                int year = Integer.parseInt(st.nextToken());
                int month = Integer.parseInt(st.nextToken());
                int day = Integer.parseInt(st.nextToken());
                String dayOfWeek = st.nextToken();
                int dayOpenAmt = Integer.parseInt(st.nextToken());
                int dayCloseAmt = Integer.parseInt(st.nextToken());


                ContentValues values = new ContentValues();
                values.put(OpenHelper._ID, i);
                values.put(OpenHelper.YEAR, year);
                values.put(OpenHelper.MONTH, month);
                values.put(OpenHelper.DAY, day);
                values.put(OpenHelper.DAY_OF_WEEK, dayOfWeek);
                values.put(OpenHelper.DAY_OPEN_AMOUNT, dayOpenAmt);
                values.put(OpenHelper.DAY_CLOSE_AMOUNT, dayCloseAmt);

                db.insert(OpenHelper.TABLE_NAME, null, values);

            }
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;

    }

    public long getNumOfRows() {
        db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return count;
    }

    public ArrayList<DailyCash> readRecords(int year, int month, int day, int noWorkDays) {
        db = getReadableDatabase();
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        ArrayList<DailyCash> dailyCashes = new ArrayList<>();

        int startID = getRowID(db, year, month, day);


        while (startID == 0) {
            startID = getRowID(db, year, month, ++day);
            if (startID == 0) {
                if (day > 31) {
                    day = 1;
                    ++month;
                }
                if (month > 12 && year == 2017) {
                    month = 1;
                    year = 2018;
                } else if (month > 12 && year == 2018) {
                    break;
                }
            }
        }
        if (startID != 0) {
            Log.e("startID", startID + "");
            int endID = startID + noWorkDays - 1;
            if (endID > 290) {
                endID = 290;
            }
            // Log.e("startID",startID+"");

            Cursor cursor = db.rawQuery("SELECT * from " + TABLE_NAME + " where " + _ID + " BETWEEN " + startID + " AND " + endID, null);


            DailyCash dailyCash;
            Log.e("CURSOR size", cursor.getCount() + "");
            while (cursor.moveToNext()) {
                int ID = (int) cursor.getLong(cursor.getColumnIndex(_ID));
                int _YEAR = (int) cursor.getLong(cursor.getColumnIndex(YEAR));
                int _MONTH = (int) cursor.getLong(cursor.getColumnIndex(MONTH));
                int _DAY = (int) cursor.getLong(cursor.getColumnIndex(DAY));
                String _DAY_OF_WEEK = cursor.getString(cursor.getColumnIndex(DAY_OF_WEEK));
                int _DAY_OPEN_AMOUNT = (int) cursor.getLong(cursor.getColumnIndex(DAY_OPEN_AMOUNT));
                //Log.e("open amt",_DAY_OPEN_AMOUNT+"");

                int _DAY_CLOSE_AMOUNT = (int) cursor.getLong(cursor.getColumnIndex(DAY_CLOSE_AMOUNT));
                //Log.e("close amt",_DAY_CLOSE_AMOUNT+"");

                dailyCash = new DailyCash(ID, _YEAR, _MONTH, _DAY, _DAY_OF_WEEK, _DAY_OPEN_AMOUNT, _DAY_CLOSE_AMOUNT);
                dailyCashes.add(dailyCash);
            }
//        Log.e("LIST size",itemIds.size()+"");

            cursor.close();
        }
//                for (int i = 0 ; i<dailyCashes.size() ; i++){
//                     Log.e("dailyCash",dailyCashes.get(i).mDay + "");
//                }
        return dailyCashes;
    }

    private int getRowID(SQLiteDatabase db, int year, int month, int day) {
        String[] projection = {_ID};

// Filter results WHERE "title" = 'My Title'
        String selection = YEAR + "=" + year
                + " AND " + MONTH + "=" + month
                + " AND " + DAY + "=" + day;


        String[] selectionArgs = {};


//        Cursor cursor = db.rawQuery("SELECT "+ _ID + " from " + TABLE_NAME + " where "+ YEAR + "=" + year + " AND " + MONTH + " AND " + DAY + "=" + day, null);

// How you want the results sorted in the resulting Cursor
        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        long itemId = 0;
        Log.e("CURSOR size", cursor.getCount() + "");
        while (cursor.moveToNext()) {
            itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(_ID));
            Log.e("id", itemId + "");
        }

        cursor.close();
        return (int) itemId;
    }


    // Delete entire table
    public void deleteTable() {
        // Call SQLiteDatabase.delete() -- null arg deletes all rows in arg table.
        db = getWritableDatabase();
        db.rawQuery(" DROP TABLE IF EXISTS " + TABLE_NAME, null);

    }

    // Calls ContextWrapper.deleteDatabase() by way of inheritance
    public void deleteDatabase() {
        mContext.deleteDatabase(DATABASE_NAME);
        db.close();
    }
}

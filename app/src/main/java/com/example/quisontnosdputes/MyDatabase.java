package com.example.quisontnosdputes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE_NAME = "mydatabase";
    private static final String PKEY = "id";
    private static final String COL1 = "jsonDepute";

    MyDatabase(Context context) {
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME + " (" + PKEY + " INTEGER PRIMARY KEY," + COL1 + " TEXT);";
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(int id, String jsonDepute){
        Log.i("Guitou"," Insert in database");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(PKEY,id);
        values.put(COL1, jsonDepute);


        db.insertOrThrow(DATABASE_TABLE_NAME,null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public void readData(){
        Log.i("Guitou", "Reading database...");
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i("Guitou", "Number of entries: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Log.i("Guitou", "Reading: " + cursor.getString(cursor.getColumnIndex(COL1)));
            }
            while (cursor.moveToNext());
        }
    }

    public Boolean isInDB(int id){
        Log.i("Guitou", "Testing db");
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME + " WHERE " + PKEY + " = " + id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i("Guitou", "Number of entries: " + cursor.getCount());
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void deletedata (int id){
        Log.i("Guitou"," Delete in database");
        SQLiteDatabase db = getWritableDatabase();
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME + " WHERE " + PKEY + " = " + id);
        Cursor cursor = db.rawQuery(select, null);

        db.beginTransaction();
        db.delete(DATABASE_TABLE_NAME,PKEY +" = " + id, null);
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i("Guitou", "Number of entries: " + cursor.getCount());

    }
}


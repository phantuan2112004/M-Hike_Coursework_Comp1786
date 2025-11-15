package com.example.mhikeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mhike.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_HIKES = "hikes";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_LOCATION = "location";
    private static final String COL_DATE = "date_hike";
    private static final String COL_PARKING = "parking";
    private static final String COL_LENGTH = "length";
    private static final String COL_DIFFICULTY = "difficulty";
    private static final String COL_DESC = "description";
    private static final String COL_WEATHER = "weather";
    private static final String COL_COMPANIONS = "companions";

    private static final String TABLE_OBS = "observations";
    private static final String COL_OBS_ID = "obs_id";
    private static final String COL_HIKE_ID_FK = "hike_id";
    private static final String COL_OBSERVATION = "observation";
    private static final String COL_TIME = "time";
    private static final String COL_COMMENTS = "comments";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng HIKES
        String createHikes = "CREATE TABLE " + TABLE_HIKES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_PARKING + " TEXT, " +
                COL_LENGTH + " TEXT, " +
                COL_DIFFICULTY + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_WEATHER + " TEXT, " +
                COL_COMPANIONS + " TEXT)";
        db.execSQL(createHikes);

        String createObs = "CREATE TABLE " + TABLE_OBS + " (" +
                COL_OBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_HIKE_ID_FK + " INTEGER, " +
                COL_OBSERVATION + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_COMMENTS + " TEXT, " +
                "FOREIGN KEY(" + COL_HIKE_ID_FK + ") REFERENCES " + TABLE_HIKES + "(" + COL_ID + "))";
        db.execSQL(createObs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBS);
        onCreate(db);
    }


    public long insertHike(String name, String location, String date, String parking,
                           String length, String difficulty, String desc, String weather, String companions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_LOCATION, location);
        values.put(COL_DATE, date);
        values.put(COL_PARKING, parking);
        values.put(COL_LENGTH, length);
        values.put(COL_DIFFICULTY, difficulty);
        values.put(COL_DESC, desc);
        values.put(COL_WEATHER, weather);
        values.put(COL_COMPANIONS, companions);

        long id = db.insert(TABLE_HIKES, null, values);
        db.close();
        return id;
    }

    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HIKES + " ORDER BY " + COL_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                );
                hikeList.add(hike);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hikeList;
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_OBS);
        db.execSQL("DELETE FROM " + TABLE_HIKES);
        db.close();
    }

    public void deleteHike(int hikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBS, COL_HIKE_ID_FK + " = ?", new String[]{String.valueOf(hikeId)});
        db.delete(TABLE_HIKES, COL_ID + " = ?", new String[]{String.valueOf(hikeId)});
        db.close();
    }
}
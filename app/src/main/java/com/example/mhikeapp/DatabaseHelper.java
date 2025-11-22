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

    public Hike getHike(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIKES, null, COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) cursor.moveToFirst();

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
        cursor.close();
        return hike;
    }

    public void updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, hike.getName());
        values.put(COL_LOCATION, hike.getLocation());
        values.put(COL_DATE, hike.getDate());
        values.put(COL_PARKING, hike.getParking());
        values.put(COL_LENGTH, hike.getLength());
        values.put(COL_DIFFICULTY, hike.getDifficulty());
        values.put(COL_DESC, hike.getDescription());
        values.put(COL_WEATHER, hike.getWeather());
        values.put(COL_COMPANIONS, hike.getCompanions());

        db.update(TABLE_HIKES, values, COL_ID + " = ?", new String[]{String.valueOf(hike.getId())});
        db.close();
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

    public long insertObservation(int hikeId, String observation, String time, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HIKE_ID_FK, hikeId);
        values.put(COL_OBSERVATION, observation);
        values.put(COL_TIME, time);
        values.put(COL_COMMENTS, comments);

        long id = db.insert(TABLE_OBS, null, values);
        db.close();
        return id;
    }
    public List<Observation> getObservationsByHikeId(int hikeId) {
        List<Observation> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Lọc theo hike_id
        Cursor cursor = db.query(TABLE_OBS, null, COL_HIKE_ID_FK + "=?",
                new String[]{String.valueOf(hikeId)}, null, null, COL_OBS_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Observation obs = new Observation(
                        cursor.getInt(0), // id
                        cursor.getInt(1), // hikeId
                        cursor.getString(2), // observation name
                        cursor.getString(3), // time
                        cursor.getString(4)  // comments
                );
                list.add(obs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public void updateObservation(Observation obs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_OBSERVATION, obs.getObservation());
        values.put(COL_TIME, obs.getTime());
        values.put(COL_COMMENTS, obs.getComments());

        db.update(TABLE_OBS, values, COL_OBS_ID + "=?", new String[]{String.valueOf(obs.getId())});
        db.close();
    }
    public void deleteObservation(int obsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBS, COL_OBS_ID + "=?", new String[]{String.valueOf(obsId)});
        db.close();
    }

    public List<Hike> searchHikes(String keyword) {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HIKES + " WHERE " + COL_NAME + " LIKE ?", new String[]{"%" + keyword + "%"});

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

    public List<Hike> searchAdvanced(String name, String location, String length, String date) {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_HIKES + " WHERE " +
                COL_NAME + " LIKE ? AND " +
                COL_LOCATION + " LIKE ? AND " +
                COL_LENGTH + " LIKE ? AND " +
                COL_DATE + " LIKE ?";

        String[] args = new String[]{
                "%" + name + "%",
                "%" + location + "%",
                "%" + length + "%",
                "%" + date + "%"
        };

        Cursor cursor = db.rawQuery(query, args);

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
}
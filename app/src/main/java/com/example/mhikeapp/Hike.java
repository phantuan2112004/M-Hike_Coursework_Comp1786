package com.example.mhikeapp;

import java.io.Serializable;

public class Hike implements Serializable {
    private int id;
    private String name;
    private String location;
    private String date;
    private String parking;
    private String length;
    private String difficulty;
    private String description;

    private String weather;
    private String companions;

    public Hike(int id, String name, String location, String date, String parking,
                String length, String difficulty, String description, String weather, String companions) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.weather = weather;
        this.companions = companions;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getParking() { return parking; }
    public String getLength() { return length; }
    public String getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public String getWeather() { return weather; }
    public String getCompanions() { return companions; }
}

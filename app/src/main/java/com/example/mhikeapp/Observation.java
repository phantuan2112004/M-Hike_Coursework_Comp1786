package com.example.mhikeapp;

public class Observation {
    private int id;
    private int hikeId;
    private String observation;
    private String time;
    private String comments;

    public Observation(int id, int hikeId, String observation, String time, String comments) {
        this.id = id;
        this.hikeId = hikeId;
        this.observation = observation;
        this.time = time;
        this.comments = comments;
    }

    public int getId() { return id; }
    public int getHikeId() { return hikeId; }
    public String getObservation() { return observation; }
    public String getTime() { return time; }
    public String getComments() { return comments; }
}
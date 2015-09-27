package com.project.pebblevote;

/**
 * Created by Not-A-Mac on 9/26/2015.
 */
public class LocationModel {

    public static final String LM_NAME = "name";
    public static final String LM_BOROUGH = "borough";
    public static final String LM_LATITUDE = "latitude";
    public static final String LM_LONGITUDE = "longitude";
    public static final String LM_UPVOTE = "upvote";
    public static final String LM_DOWNVOTE = "downvote";

    private String name;
    private String neightborhood;
    private double latitude;
    private double longitude;
    private int upVotes;
    private int downVotes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeightborhood() {
        return neightborhood;
    }

    public void setNeightborhood(String neightborhood) {
        this.neightborhood = neightborhood;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }
}

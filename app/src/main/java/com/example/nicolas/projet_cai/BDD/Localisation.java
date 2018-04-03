package com.example.nicolas.projet_cai.BDD;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Valentin on 10/02/2017.
 */

public class Localisation {

    String NameOfRun;
    LatLng location;
    Time time;
    double altitude;

    public Localisation(String nameOfRun, LatLng location, Time time, double altitude) {
        NameOfRun = nameOfRun;
        this.location = location;
        this.time = time;
        this.altitude = altitude;
    }

    public String getNameOfRun() {
        return NameOfRun;
    }

    public void setNameOfRun(String nameOfRun) {
        NameOfRun = nameOfRun;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}

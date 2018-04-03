package com.example.nicolas.projet_cai.BDD;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class Time {

    int year;
    int month;
    int day;
    int hours;
    int mins;
    int seconds;
    int milliseconds;

    public Time(int year, int month, int day, int hours, int mins, int seconds, int milliseconds) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.mins = mins;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }
}

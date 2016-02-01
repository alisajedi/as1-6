package com.example.erin.elmacdon_fueltrack;

import java.util.Date;

/**
 * Created by erin on 24/01/16.
 */
public class Entry {
    // An Entry carries all the fields needed for a fuel log entry
    protected String date;
    protected String station;
    protected Float odometer;
    protected String grade;
    protected Float amount;
    protected Float costPerL;
    protected String cost;

    public Entry(String date, String station, Float odometer, String grade, Float amount, Float costPerL, String cost) {
        this.date = date;
        this.station = station;
        this.odometer = odometer;
        this.grade = grade;
        this.amount = amount;
        this.costPerL = costPerL;
        this.cost = cost;
    }

    // This converts the entry into a string with all the fields separated by "|"
    public String toString() {
        return date + " | " + station + " | " + odometer.toString() + " km | " + grade + " | " +
                amount.toString() + " L | " + costPerL.toString() + " ¢/L | " + cost;
    }
}

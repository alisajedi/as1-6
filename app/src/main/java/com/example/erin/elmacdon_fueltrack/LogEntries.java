package com.example.erin.elmacdon_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

public class LogEntries extends MainActivity {
    // Gives a list of all logs entered so far. When an entry is clicked it opens EntryPage with
    // the EditText's populated with the information from the clicked entry

    private ListView oldEntriesList;
    private TextView displayCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entries);
        displayCost = (TextView) findViewById(R.id.fuelCost);

        // Accesses the fuel cost from the function in the parent class MainActivity and converts
        // the float number to currency, so it is rounded to 2 decimal places, and makes it a string
        // which is then printed at the top of the screen
        totalCost = getFuelCost();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String output = formatter.format(totalCost);
        displayCost.setText("Total Fuel Cost: " + output);

        // Prints the list of entries
        oldEntriesList = (ListView) findViewById(R.id.oldEntriesList);

        // Taken from http://stackoverflow.com/questions/17851687/how-to-handle-the-click-event-in-listview-in-android
        // 01-31-16
        oldEntriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int place, long id) {
                position = place;
                Intent intent = new Intent(view.getContext(), EntryPage.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        // Prints the list of entries
        loadFromFile();
        adapter = new ArrayAdapter<Entry>(this, R.layout.list_item, entries);
        oldEntriesList.setAdapter(adapter);

        // Same as above (converts float from file to string)
        totalCost = getFuelCost();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String output = formatter.format(totalCost);
        displayCost.setText("Total Fuel Cost: " + output);
    }
}

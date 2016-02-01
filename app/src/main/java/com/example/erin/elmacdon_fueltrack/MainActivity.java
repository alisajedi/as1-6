package com.example.erin.elmacdon_fueltrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCombatActivity {
    // Opens a main page with two buttons (NEW LOG ENTRY and VIEW LOG ENTRIES) which, when clicked,
    // open EntryPage and LogEntries respectively

    protected ArrayList<Entry> entries = new ArrayList<Entry>();
    protected ArrayAdapter<Entry> adapter;
    // totalCost is the amount of money spent on fuel, udpated throughout the program. Position is
    // used when a log entry is clicked to know which entry in the ArrayList to access
    protected static float totalCost;
    protected static int position = -1;

    protected static final String FILENAME = "file.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalCost = getFuelCost();

        // totalCost is either initialized to 0 (see getFuelCost()) or the float in the file
        Button newEntryButton = (Button) findViewById(R.id.newEntry);
        Button viewEntriesButton = (Button) findViewById(R.id.logEntries);
        totalCost = getFuelCost();

        // Calls the activity EntryPage when the newEntry button is clicked
        newEntryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                Intent logIntent = new Intent(view.getContext(), EntryPage.class);
                startActivity(logIntent);
            }
        });

        // Calls the activity LogEntries when the viewEntries button is clicked
        viewEntriesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                Intent logIntent = new Intent(view.getContext(), LogEntries.class);
                startActivity(logIntent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
    }

    // This and saveInFile were taken from lonelyTwitter https://github.com/joshua2ua/lonelyTwitter.git
    // 01-31-16
    protected void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            // Taken from *URL* 01-19-16
            Type listType = new TypeToken<ArrayList<Entry>>() {}.getType();
            entries = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            entries = new ArrayList<Entry>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(entries, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // This and getFuelCost were taken from http://stackoverflow.com/questions/32423647/how-do-i-save-a-float-to-a-file-using-android-java
    // 01-31-16
    public void setFuelCost(float cost){
        FileOutputStream outputStream = null;
        try {
            outputStream = (this).openFileOutput("cost", Context.MODE_PRIVATE);
            outputStream.write(Float.toString(cost).getBytes());
            outputStream.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    public float getFuelCost(){
        ArrayList<String> text = new ArrayList<String>();

        FileInputStream inputStream;
        try {
            inputStream = (this).openFileInput("cost");

            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) { e.printStackTrace();}

        if (text.size() > 0) {
            return Float.parseFloat(text.get(0));
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

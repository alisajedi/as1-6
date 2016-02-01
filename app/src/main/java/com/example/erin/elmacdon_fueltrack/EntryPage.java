package com.example.erin.elmacdon_fueltrack;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erin on 30/01/16.
 */
public class EntryPage extends MainActivity {

    private EditText dateEnt;
    private EditText stationEnt;
    private EditText odometerEnt;
    private EditText gradeEnt;
    private EditText amountEnt;
    private EditText costPerLEnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadFromFile();
        // Assign names to all the edit texts and Buttons
        dateEnt = (EditText) findViewById(R.id.date);
        stationEnt = (EditText) findViewById(R.id.station);
        odometerEnt = (EditText) findViewById(R.id.odometer);
        gradeEnt = (EditText) findViewById(R.id.grade);
        amountEnt = (EditText) findViewById(R.id.amount);
        costPerLEnt = (EditText) findViewById(R.id.costPerL);
        Button saveButton = (Button) findViewById(R.id.save);
        Button cancelButton = (Button) findViewById(R.id.cancel);

        // This handles the case where the user has selected to edit an existing entry. This
        // populates the EditText windows with the existing data
        if (position > -1) {
            Entry oldEntry = entries.get(position);
            String monthNum = getMonth(oldEntry.date.substring(0, 3));
            String entryDate = oldEntry.date.substring(8, 12) + "-" + monthNum + "-" +
                    oldEntry.date.substring(4, 6);
            dateEnt.setText(entryDate);
            stationEnt.setText(oldEntry.station);
            odometerEnt.setText(oldEntry.odometer.toString());
            gradeEnt.setText(oldEntry.grade);
            amountEnt.setText(oldEntry.amount.toString());
            costPerLEnt.setText(oldEntry.costPerL.toString());

            // This takes the fuel cost calculated from the user input for the old entry that has
            // loaded and subtracts it from the totalCost variable and updates the file "cost"
            String oldAmountStr = amountEnt.getText().toString();
            float oldAmount = Float.valueOf(oldAmountStr);
            String oldCostPerLStr = costPerLEnt.getText().toString();
            float oldCostPerL = Float.valueOf(oldCostPerLStr);
            float oldCost = (oldAmount * oldCostPerL) / 100;
            totalCost = getFuelCost();
            totalCost = totalCost - oldCost;
            setFuelCost(totalCost);
        }

        // Taken from http://stackoverflow.com/questions/17423483/how-to-limit-edittext-length-to-7-integers-and-2-decimal-places
        // 01-31-16
        odometerEnt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(1)});
        amountEnt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3)});
        costPerLEnt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(1)});

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    // Taken from http://stackoverflow.com/questions/10028211/how-can-i-get-the-date-from-the-edittext-and-then-store-it-in-database-in-androi
                    // and http://stackoverflow.com/questions/5050170/java-getting-date-without-time
                    // 01-31-16
                    // Specifies the input to accept for the date and converts that to a string
                    String dateStr = (dateEnt.getText().toString());
                    Date dateD = format.parse(dateStr);
                    String dateText = dateD.toString().substring(4, 7) + " " +
                            dateD.toString().substring(8, 10) + ", " +
                            dateD.toString().substring(24, 28);
                    String stationText = stationEnt.getText().toString();
                    // This and code to convert amountStr and CostPerLStr to floats taken from
                    // http://stackoverflow.com/questions/15037465/converting-edittext-to-int-android
                    // 01-31-16
                    // Converts the rest of the EditText fields to strings and floats
                    String odometerStr = odometerEnt.getText().toString();
                    float odometerText = Float.valueOf(odometerStr);
                    String gradeText = gradeEnt.getText().toString();
                    String amountStr = amountEnt.getText().toString();
                    float amountText = Float.valueOf(amountStr);
                    String costPerLStr = costPerLEnt.getText().toString();
                    float costPerLText = Float.valueOf(costPerLStr);

                    // Calculates the cost spent on the entry and adds that to the totalCost
                    // variable which is updated in the file
                    float costFloat = (amountText * costPerLText) / 100;
                    totalCost = totalCost + costFloat;
                    setFuelCost(totalCost);
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    String costText = formatter.format(costFloat);

                    // Creates an Entry class
                    Entry latestEntry = new Entry(dateText, stationText, odometerText, gradeText, amountText, costPerLText, costText);

                    // If an existing entry is being edited, update the entry in the ArrayList
                    // entries at the given position
                    if (position > -1) {
                        entries.set(position, latestEntry);
                    }
                    // If a new entry is being created, add it to entries
                    else {
                        entries.add(latestEntry);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                saveInFile();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                // If the user cancels editing an entry, add the fuelCost previously deducted back
                // to the totalCost in the file "cost" and update the position to -1
                if (position > -1) {
                    String oldAmountStr = amountEnt.getText().toString();
                    float oldAmount = Float.valueOf(oldAmountStr);
                    String oldCostPerLStr = costPerLEnt.getText().toString();
                    float oldCostPerL = Float.valueOf(oldCostPerLStr);
                    float oldCost = (oldAmount * oldCostPerL) / 100;
                    totalCost = getFuelCost();
                    totalCost = totalCost + oldCost;
                    setFuelCost(totalCost);
                    position = -1;
                }
                finish();
            }
        });
    }

    // http://stackoverflow.com/questions/17423483/how-to-limit-edittext-length-to-7-integers-and-2-decimal-places
    // 01-31-16
    public class DecimalDigitsInputFilter implements InputFilter {
        Pattern numPattern;

        public DecimalDigitsInputFilter(int digits) {
            int digitsAfterZero = digits - 1;
            numPattern = Pattern.compile("[0-9]{0,10}(\\.[0-9]{0," + digitsAfterZero + "})?||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = numPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }
    }

    // Function to determine which month to print
    private String getMonth(String month) {
        if (month.equals("Jan")) {
            return "01";
        }
        else if (month.equals("Feb")) {
            return "02";
        }
        else if (month.equals("Mar")) {
            return "03";
        }
        else if (month.equals("Apr")) {
            return "04";
        }
        else if (month.equals("May")) {
            return "05";
        }
        else if (month.equals("Jun")) {
            return "06";
        }
        else if (month.equals("Jul")) {
            return "07";
        }
        else if (month.equals("Aug")) {
            return "08";
        }
        else if (month.equals("Sep")) {
            return "09";
        }
        else if (month.equals("Oct")) {
            return "10";
        }
        else if (month.equals("Nov")) {
            return "11";
        }
        return "12";
    }
}
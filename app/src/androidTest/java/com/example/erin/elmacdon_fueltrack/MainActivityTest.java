package com.example.erin.elmacdon_fueltrack;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by erin on 01/02/16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2 {
    public MainActivityTest() {
            super(com.example.erin.elmacdon_fueltrack.MainActivity.class);
        }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
}


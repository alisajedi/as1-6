package com.example.erin.elmacdon_fueltrack;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by erin on 01/02/16.
 */
public class EntryTest extends ActivityInstrumentationTestCase2 {
    public EntryTest() { super(Entry.class); }

    public void testToString() {
        Entry newEntry = new Entry("2001-03-15", "Safeway", Float.valueOf("20043.5"), "regular",
                Float.valueOf("52.841,"), Float.valueOf("84.7"), "44.44");
        assertTrue(newEntry.toString() != null);
    }
}

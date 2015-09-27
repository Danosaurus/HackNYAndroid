package com.project.pebblevote;

import android.content.Context;
import android.os.AsyncTask;

import com.getpebble.android.kit.*;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

/**
 * Created by danosaurus on 27/09/15.
 */
public class PebbleController {
    private final static UUID PEBBLE_APP_UUID = UUID.fromString("2105c9d1-862c-40df-8369-a411e2b8d8e6");
    private static final int STATUS_KEY = 1;
    private static final int LOCATION_KEY = 2;
    private static final int SCORE_KEY = 3;


    public void sendLocations(Context context, LatLng curLatLng){
        AsyncTask fetchLocationList = new MapsActivity.FetchLocationList().execute(curLatLng);

        PebbleDictionary data = new PebbleDictionary();

        // Add a key of 0
        data.addUint8(0, (byte) 42);

        // Add a key of 1, and a string value.
        data.addString(1, "A string");

        PebbleKit.sendDataToPebble(context.getApplicationContext(), PEBBLE_APP_UUID, data);

    }
}

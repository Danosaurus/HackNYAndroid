package com.project.pebblevote;

import android.content.Context;

import com.getpebble.android.kit.*;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

/**
 * Created by danosaurus on 27/09/15.
 */
public class PebbleController {
    private final static UUID PEBBLE_APP_UUID = UUID.fromString("2105c9d1-862c-40df-8369-a411e2b8d8e6");

    public void sendLocations(Context context){
        PebbleDictionary data = new PebbleDictionary();

        // Add a key of 0, and a uint8_t (byte) of value 42.
        data.addUint8(0, (byte) 42);

        // Add a key of 1, and a string value.
        data.addString(1, "A string");

        PebbleKit.sendDataToPebble(context.getApplicationContext(), PEBBLE_APP_UUID, data);

    }
}

package com.project.pebblevote;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        new FetchServerHealth().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private class FetchServerHealth extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            Uri.Builder uri = null;
            URL url = null;
            try {
                uri = new Uri.Builder();
                uri.scheme("http");
                uri.authority("pebblevote.herokuapp.com/");
                uri.appendPath("_ah");
                uri.appendPath("health");
                uri.build();
                url = new URL(uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String result = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                result = buffer.toString();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    //always disconnect and close
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            return result;
        }
    }

    /**
     * We will just call the api end point and get a list of data, 10 of them to be precise
     *
     */
    private List<LocationModel> getPlaceLocation(LatLng location) {
        Uri.Builder uri = null;
        URL url = null;
        try {
            uri = new Uri.Builder();
            uri.scheme("http");
            uri.authority("api.openweathermap.org");
            uri.appendPath("");
            uri.appendQueryParameter("latitude", String.valueOf(location.latitude));
            uri.appendQueryParameter("longitude", String.valueOf(location.longitude));
            uri.build();
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            result = buffer.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                //always disconnect and close
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }

        return getLocationModelFromJson(result);

    }

    private List<LocationModel> getLocationModelFromJson(String JSON) {

        try {
            JSONArray jsonLocationModelList = new JSONArray(JSON);
            List<LocationModel> toReturn = new ArrayList<>();
            for (int i = 0; i < jsonLocationModelList.length(); i++){
                LocationModel model = new LocationModel();
                JSONObject e = jsonLocationModelList.getJSONObject(i);
                model.setName(e.getString(LocationModel.LM_NAME));
                model.setNeightborhood(e.getString(LocationModel.LM_BOROUGH));
                model.setLatitude(e.getDouble(LocationModel.LM_LATITUDE));
                model.setLongitude(e.getDouble(LocationModel.LM_LONGITUDE));
                model.setUpVotes(e.getInt(LocationModel.LM_UPVOTE));
                model.setDownVotes(e.getInt(LocationModel.LM_DOWNVOTE));

                toReturn.add(model);
            }

            return toReturn;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }
}

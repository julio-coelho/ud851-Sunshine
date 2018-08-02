package com.example.android.sunshine.sync;

//  COMPLETED (1) Create a class called SunshineSyncTask
//  COMPLETED (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      COMPLETED (3) Within syncWeather, fetch new weather data
//      COMPLETED (4) If we have valid results, delete the old data and insert the new

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class SunshineSyncTask {

    private static final String TAG = SunshineSyncTask.class.getSimpleName();

    synchronized public static void syncWeather(Context context) {

        try {
            URL weatherRequestUrl = NetworkUtils.getUrl(context);

            String response = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            Log.d(TAG, "response: " + response);
            ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, response);

            if (weatherValues != null && weatherValues.length > 0) {

                ContentResolver resolver = context.getContentResolver();
                int rowsDeleted = resolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                Log.d(TAG, "Rows Affected by Delete: " + rowsDeleted);
                int rowsInserted = resolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);
                Log.d(TAG, "Rows Affected by Insert: " + rowsInserted);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

    }

}
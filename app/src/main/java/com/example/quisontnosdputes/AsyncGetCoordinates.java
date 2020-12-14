package com.example.quisontnosdputes;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class AsyncGetCoordinates extends AsyncTask<String,Void,double[]> {

    Context context;

    protected AsyncGetCoordinates(Context context) {
        this.context = context;
    }

    @Override
    protected double[] doInBackground(String... strings) {
      /*  requestPermission();
        client = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> locationTask = LocationServices.getFusedLocationProviderClient(context).getLastLocation();

        }
*/
        return new double[0];
    }
}

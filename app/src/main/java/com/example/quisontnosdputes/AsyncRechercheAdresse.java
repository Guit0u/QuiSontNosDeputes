package com.example.quisontnosdputes;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class AsyncRechercheAdresse extends AsyncTask<String,Void, double[]> {
    @Override
    protected double[] doInBackground(String... strings) {
        final double[] coordonnees = new double[2];
        String adresse = new String();
        String urlString = "https://api-adresse.data.gouv.fr/search/?q=";
        String[] splited = strings[0].split("\\s+");
        for(int i=0;i<splited.length;i++){
            if(i==0){urlString = urlString.concat(splited[i]);}
            else{urlString = urlString.concat("+"+splited[i]);}
        }
        urlString = urlString.concat("&limit=1");
        URL url = null;


        publishProgress();
        try {
            url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            adresse = readStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonAdresse = new JSONObject(adresse);
            JSONArray arrayAdresse = jsonAdresse.getJSONArray("features");
            JSONObject jsonBonneAdresse = arrayAdresse.getJSONObject(0);
            JSONObject jsonCoordonnees = jsonBonneAdresse.getJSONObject("geometry");
            JSONArray intCoordonnees = jsonCoordonnees.getJSONArray("coordinates");
            for (int i = 0; i < coordonnees.length; ++i) {
                coordonnees[i] = intCoordonnees.optDouble(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coordonnees;
    }

    protected void onPostExecute(String string){

    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}

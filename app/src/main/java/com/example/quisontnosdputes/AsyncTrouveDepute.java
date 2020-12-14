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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTrouveDepute extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... strings) {

        String depute="zut";
        String[] splited = strings[0].split("-");
        String numDepartement = splited[0];
        String numCirco = splited[1];
        char ch = numDepartement.charAt(0);
        int mieux = Character.getNumericValue(ch);
        if(mieux==0){
            numDepartement = numDepartement.substring(1);
        }
        char ch2 = numCirco.charAt(0);
        int ch3 = Character.getNumericValue(ch2);
        if(ch3==0){
            numCirco = numCirco.substring((1));
        }
        String listeDepute = new String();
        String urlString = "https://www.nosdeputes.fr/deputes/enmandat/json";
        URL url = null;

        try {
            url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            listeDepute = readStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject listeJSON = new JSONObject(listeDepute);
            JSONArray deputesArray = listeJSON.getJSONArray("deputes");
            for(int i =0;i<deputesArray.length();i++){
                JSONObject JsonDeputeInd = deputesArray.getJSONObject(i);
                JSONObject JsonDepute = JsonDeputeInd.getJSONObject("depute");

                if(numDepartement.equals(JsonDepute.getString("num_deptmt")) && numCirco.equals(JsonDepute.getString("num_circo"))){
                    depute = JsonDepute.getString("nom");
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return depute;
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

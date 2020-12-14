package com.example.quisontnosdputes;

import android.content.Context;
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

public class AsyncDepute extends AsyncTask<String,Void,JSONObject> {

    Context context;
    JSONObject monDepute = null;

    protected AsyncDepute(Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        String Deputes = new String();
        URL url = null;
        String urlDeputes = "https://www.nosdeputes.fr/synthese/data/json";
        JSONObject JSONDeputes = null;
        JSONArray ArrayDeputes = null;
        try {
            url = new URL(urlDeputes);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            Deputes = total.toString();
            JSONDeputes = new JSONObject(Deputes);
            ArrayDeputes = JSONDeputes.getJSONArray("deputes");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<ArrayDeputes.length();i++){
            JSONObject currentDepute = null;
            JSONObject currentJSONDepute;
            try {
                currentDepute = ArrayDeputes.getJSONObject(i);
                currentJSONDepute = currentDepute.getJSONObject("depute");
                if(currentJSONDepute.getString("nom").equals(strings[0])){
                    monDepute = currentJSONDepute;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(monDepute==null){
            URL url2 = null;
            String MissingDepute=new String();
            String[] splited = strings[0].split(" ");
            String urlMonDepute = "https://www.nosdeputes.fr/"+splited[0]+"-"+splited[1]+"/json";
            System.out.println(urlMonDepute);
            JSONObject JSONMonDepute = null;
            try {
                url2 = new URL(urlMonDepute);
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                InputStream in2 = new BufferedInputStream(urlConnection2.getInputStream());
                BufferedReader r2 = new BufferedReader(new InputStreamReader(in2));
                StringBuilder total2 = new StringBuilder();
                for (String line; (line = r2.readLine()) != null; ) {
                    total2.append(line).append('\n');
                }
                MissingDepute = total2.toString();
                JSONMonDepute = new JSONObject(MissingDepute);
                JSONObject JSONMonDepute2 = JSONMonDepute.getJSONObject("depute");
                monDepute = JSONMonDepute2;
            } catch (MalformedURLException e) {
            e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return monDepute;
    }

    @Override
    protected void onPostExecute(JSONObject infos) {
        super.onPostExecute(infos);

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

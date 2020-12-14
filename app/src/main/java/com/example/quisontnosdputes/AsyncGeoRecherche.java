package com.example.quisontnosdputes;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;


public class AsyncGeoRecherche extends AsyncTask<Double,Void,String> {

    private Context context;
    String coordonates = "raté";

    protected AsyncGeoRecherche(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Double... CoorPoint) {

        boolean recherche = true;
        int i =0;
        while (recherche && i<2726) {

            String carte = new String();
            String res = "circo";
            res = res.concat(String.valueOf(i));
            int id = context.getResources().getIdentifier(res, "raw", context.getPackageName());
            InputStream is = context.getResources().openRawResource(id);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String string = "";
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    if ((string = reader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(string);
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            carte = stringBuilder.toString();
            JSONObject carteJson = null;
            try {
                carteJson = new JSONObject(carte);
                JSONObject limits = carteJson.getJSONObject("limits");

                if (CoorPoint[0] < limits.getDouble("haut") && CoorPoint[0] > (limits.getDouble("bas")) && CoorPoint[1] > limits.getDouble("gauche") && CoorPoint[1] < limits.getDouble("droite")) {

                    String geo = carteJson.getString("polygon");
                    GeometryJSON g = new GeometryJSON();
                    InputStream stream = new ByteArrayInputStream(geo.getBytes(StandardCharsets.UTF_8));
                    Polygon polygon = g.readPolygon(stream);
                    GeometryFactory gf = new GeometryFactory();
                    Point gPoint = gf.createPoint(new Coordinate(CoorPoint[0], CoorPoint[1]));
                    boolean pointIsInPolygon = polygon.contains(gPoint);
                    if (pointIsInPolygon) {
                        coordonates = carteJson.getString("circo");
                        recherche=false;

                    }
                }
                i++;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return coordonates;
    }
}
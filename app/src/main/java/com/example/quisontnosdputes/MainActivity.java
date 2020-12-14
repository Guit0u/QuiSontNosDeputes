package com.example.quisontnosdputes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    public static MyDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = new MyDatabase(getApplicationContext());
        final double[][] coordonnees = {new double[2]};
        EditText EdAdresse = (EditText) findViewById(R.id.TextRecherche);
        Button recherche = (Button) findViewById(R.id.BoutonRecherche);
        TextView stat = (TextView) findViewById(R.id.StatText);
        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adresse = EdAdresse.getText().toString();

                AsyncRechercheAdresse asyncRechercheAdresse = new AsyncRechercheAdresse();
                asyncRechercheAdresse.execute(adresse);

                try {
                    //depute[0] = asyncRechercheAdresse.get();
                    coordonnees[0] = asyncRechercheAdresse.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }
                AsyncGeoRecherche asyncGeoRecherche = new AsyncGeoRecherche(getApplicationContext());
                asyncGeoRecherche.execute(coordonnees[0][0],coordonnees[0][1]);
                String coordonnees =new String();
                try {
                    coordonnees = asyncGeoRecherche.get();
                } catch (ExecutionException e){
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AsyncTrouveDepute asyncTrouveDepute = new AsyncTrouveDepute();
                asyncTrouveDepute.execute(coordonnees);
                String nomDepute = new String();
                try {
                    nomDepute = asyncTrouveDepute.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intentDepute = new Intent(getApplicationContext(),Depute.class);
                intentDepute.putExtra("JSON_DEPUTE",nomDepute);
                startActivity(intentDepute);

            }
        });
    }
}
package com.example.quisontnosdputes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

public class Depute extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depute);

        Bundle extras = getIntent().getExtras();
        String nomDepute = extras.getString("JSON_DEPUTE");

        AsyncDepute asyncDepute = new AsyncDepute(getApplicationContext());
        asyncDepute.execute(nomDepute);

        JSONObject monDepute = new JSONObject();
        try {
            monDepute = asyncDepute.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView) findViewById(R.id.info1);
        String displayDepute = new String();
        try {
            displayDepute = monDepute.getString("nom") + "\n" + monDepute.getString("groupe_sigle");
            textView.setText(displayDepute);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView photo = (ImageView) findViewById(R.id.photo);
        AsyncPhotoDepute asyncPhotoDepute = new AsyncPhotoDepute(photo);
        asyncPhotoDepute.execute(nomDepute);

        int IDdeputeTXT = 0;
        try {
            IDdeputeTXT = monDepute.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonTXT = monDepute.toString();

        ToggleButton favbtn = (ToggleButton) findViewById(R.id.favbtn);
        Boolean fav = MainActivity.DB.isInDB(IDdeputeTXT);
        favbtn.setChecked(fav);
        if (fav) {
            favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.staron));
        }
        else{
        favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.staroff));
        }
        int finalIDdeputeTXT = IDdeputeTXT;
        favbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.staron));
                    MainActivity.DB.insertData(finalIDdeputeTXT,jsonTXT);
                    Toast.makeText(Depute.this, "Ajouté en favori", Toast.LENGTH_SHORT).show();
                    }
                else {
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.staroff));
                    MainActivity.DB.deletedata(finalIDdeputeTXT);
                    Toast.makeText(Depute.this, "Supprimé des favoris", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
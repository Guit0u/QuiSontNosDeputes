package com.example.quisontnosdputes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

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
        try {
            textView.setText(monDepute.getString("nom") + "\n" + monDepute.getString("groupe_sigle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView photo = (ImageView) findViewById(R.id.photo);
        AsyncPhotoDepute asyncPhotoDepute = new AsyncPhotoDepute(photo);
        asyncPhotoDepute.execute(nomDepute);

        ToggleButton favbtn = (ToggleButton) findViewById(R.id.favbtn);
        favbtn.setChecked(false);
        favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.staroff));
        favbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.staron));
                else
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.staroff));
            }
        });
    }

}
package com.example.quisontnosdputes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

public class DeputeActivity extends AppCompatActivity {


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
        TextView Semaines = (TextView) findViewById(R.id.semainesActivité);
        TextView commissionPresenceView = (TextView) findViewById(R.id.commissionPresence);
        TextView commissionInterventions = (TextView) findViewById(R.id.commissionInter);
        TextView hemicycleInterventions = (TextView) findViewById(R.id.InterventionLongue);
        TextView amendementsProposes = (TextView) findViewById(R.id.AmendementsProposes);
        TextView rapportsEcrits = (TextView) findViewById(R.id.Rapports);
        TextView propEcrites = (TextView) findViewById(R.id.PropEcrites);
        TextView propSignees = (TextView) findViewById(R.id.PropsSignees);
        TextView questionsEcrites = (TextView) findViewById(R.id.QuestionsEcrites);
        TextView questionsOrales = (TextView) findViewById(R.id.QuestionsOrales);



        String nom = new String();
        String semainePresence = new String();
        String commissionPresence = new String();
        String commissionIntervention = new String();
        String hemicycleInterventionsstr = new String();
        String hemicycleInterventionsCourtes = new String();
        String groupe = new String();
        String amendementsProposesstr = new String();
        String rapportsEcritsstr = new String();
        String propecritesstr = new String();
        String propsigneesstr = new String();
        String questionsecritesstr = new String();
        String questionsoralesstr = new String();
        try {
        nom = monDepute.getString("nom");
        groupe = monDepute.getString("groupe_sigle");
        semainePresence ="Semaines de présence : " + monDepute.getString("semaines_presence");
        commissionPresence = "Présence en commission : " + monDepute.getString("commission_presences");
        commissionIntervention = "Intervention en commission : "+monDepute.getString("commission_interventions");
        hemicycleInterventionsstr = "Intervention en hémicycle : " + monDepute.getString("hemicycle_interventions");
        amendementsProposesstr = "Amendements proposés : " + monDepute.getString("amendements_proposes");
        rapportsEcritsstr = "Rapports écrits : " + monDepute.getString("rapports");
        propecritesstr = "Propositions de loi écrites : " + monDepute.getString("propositions_ecrites");
        propsigneesstr = "Propositions de loi signées : " + monDepute.getString("propositions_signees");
        questionsecritesstr = "Questions écrites : " + monDepute.getString("questions_ecrites");
        questionsoralesstr = "Questions orales : " + monDepute.getString("questions_orales");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textView.setText(nom + "\n" + groupe);
        Semaines.setText(semainePresence);
        commissionPresenceView.setText(commissionPresence);
        commissionInterventions.setText(commissionIntervention);
        hemicycleInterventions.setText(hemicycleInterventionsstr);
        amendementsProposes.setText(amendementsProposesstr);
        rapportsEcrits.setText(rapportsEcritsstr);
        propEcrites.setText(propecritesstr);
        propSignees.setText(propsigneesstr);
        questionsEcrites.setText(questionsecritesstr);
        questionsOrales.setText(questionsoralesstr);

        ImageView photo = (ImageView) findViewById(R.id.photo);
        AsyncPhotoDepute asyncPhotoDepute = new AsyncPhotoDepute(photo);
        asyncPhotoDepute.execute(nomDepute);
        Bitmap photoDepute=null;
        try {
           photoDepute =  asyncPhotoDepute.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String StrPhotoDepute = BitMapToString(photoDepute);

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
                    MainActivity.DB.insertData(finalIDdeputeTXT,jsonTXT,StrPhotoDepute);
                    Toast.makeText(DeputeActivity.this, "Ajouté en favori", Toast.LENGTH_SHORT).show();
                    }
                else {
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.staroff));
                    MainActivity.DB.deletedata(finalIDdeputeTXT);
                    Toast.makeText(DeputeActivity.this, "Supprimé des favoris", Toast.LENGTH_SHORT).show();
                }
            }
        });

      /*  ImageButton Mail = (ImageButton) findViewById(R.id.mail);
        JSONObject finalMonDepute = monDepute;
        Mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = null;
                try {
                    email = finalMonDepute.getJSONArray("emails").getJSONObject(0).getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] TO={email};
                Intent emailintent=new Intent(Intent.ACTION_SEND);
                emailintent.setData(Uri.parse("mail to:"));
                emailintent.setType("text/plain");
                emailintent.putExtra(Intent.EXTRA_EMAIL,TO);
            }
        });*/


    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
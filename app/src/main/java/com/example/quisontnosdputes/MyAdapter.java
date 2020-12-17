package com.example.quisontnosdputes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Depute> {

    ArrayList<Depute> ListDepute = new ArrayList<Depute>();

    public MyAdapter(Context context, ArrayList<Depute> ListDepute) {
        super(context,0,ListDepute);
        this.ListDepute = ListDepute;

    }

    public int getCount() {
        return ListDepute.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Depute depute = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_template, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView photo = (ImageView) convertView.findViewById((R.id.photoDepute));
        JSONObject JsonDepute = null;
        String nom = null;
        try {
            JsonDepute = new JSONObject(depute.JsonDepute);
            nom = JsonDepute.getString("nom") + "   " + JsonDepute.getString("groupe_sigle");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        name.setText(nom);
        String photoStr = depute.photo;
        Bitmap bitmap = StringToBitMap(photoStr);
        photo.setImageBitmap(bitmap);
        return convertView;

    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}

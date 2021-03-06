package com.example.quisontnosdputes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncPhotoDepute extends AsyncTask<String,Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;


    public AsyncPhotoDepute(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    protected void onPreExecute() {
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bm = null;
        String milieu = new String();
        String[] splited = strings[0].split("\\s+");
        for(int i=0;i<splited.length;i++) {
            if(i==0){milieu = milieu.concat(splited[i]);}
            else{milieu = milieu.concat("-"+splited[i]);}
        }

        String urlString = "https://www.nosdeputes.fr/depute/photo/" + milieu + "/100";
        URL url = null;
        try {
            url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            bm = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    protected void onPostExecute(Bitmap bitmap){
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,312 , 400, false));
            }
        }
    }
}

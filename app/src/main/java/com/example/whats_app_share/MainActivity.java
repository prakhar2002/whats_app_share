package com.example.whats_app_share;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1001;

    ImageView image;
    Button text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=findViewById(R.id.text);
        image=findViewById(R.id.image);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Context context = getApplication();
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                String[] premission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(premission, PERMISSION_CODE);
                String[] premission1 = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(premission1, PERMISSION_CODE);
                           }

        }



        final Uri uri = Uri.parse("https://cdn.pesopie.com/images/thumbnails/150/267/detailed/4/121f655fe6494184a527308728346d4c.jpg");

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {



                Picasso.get().load("https://cdn.pesopie.com/images/thumbnails/150/267/detailed/4/121f655fe6494184a527308728346d4c.jpg").into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                        Uri screenshotUri = Uri.parse(path);

                        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        intent.setType("image/*");
                        startActivity(Intent.createChooser(intent, "Share image via..."));



                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });




            }
        });




    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, file);
          //  bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}

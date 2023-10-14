package com.classified.upuse;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.classified.upuse.databinding.ActivityImageBinding;
import com.classified.upuse.network.downloadImage;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class imageActivity extends AppCompatActivity {
    ActivityImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        View decorView = getWindow().getDecorView();
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getWindow().getDecorView().getWindowInsetsController().
                            setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                }
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getWindow().getDecorView().getWindowInsetsController().
                            setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                }
                break;
        }

        Intent intent=getIntent();
        String image=intent.getStringExtra("image");
        Picasso.get().load(image).into(binding.bigImg2);

        binding.backbtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadImage(image);
                Toast.makeText(imageActivity.this,"Downloading Image...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getImageFromPath(String path){
        InputStream in =null;
        Bitmap bmp=null;
        int responseCode = -1;
        try{

            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
            }

        }
        catch(Exception ex){

        }
    }

    private void DownloadImage(String ImageUrl) {

        if (ContextCompat.checkSelfPermission(imageActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(imageActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(imageActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(imageActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            new downloadImage(this).execute(ImageUrl);
        } else {
            //Asynctask to create a thread to downlaod image in the background
            new downloadImage(this).execute(ImageUrl);
        }
    }

    @Override
    public void finish() {
        super.finish();
       // overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
    }
}
package com.classified.upuse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.classified.upuse.R;

public class adposted_successful extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adposted_successful);
        this.getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(adposted_successful.this,HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                finish();
            }
        },2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(adposted_successful.this,HomeActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        finish();
    }
}
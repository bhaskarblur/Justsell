package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.classified.justsell.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getSupportActionBar().hide();
    }
}
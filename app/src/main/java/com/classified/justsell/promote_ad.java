package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.classified.justsell.databinding.ActivityPromoteAdBinding;

public class promote_ad extends AppCompatActivity {
    private ActivityPromoteAdBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPromoteAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
    }
}
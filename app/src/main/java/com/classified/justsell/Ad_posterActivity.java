package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.classified.justsell.databinding.ActivityPostPropertyBinding;

public class Ad_posterActivity extends AppCompatActivity {
    private ActivityPostPropertyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
    }
}
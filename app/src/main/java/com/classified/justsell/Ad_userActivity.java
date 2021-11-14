package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.classified.justsell.databinding.ActivityAdUserBinding;

public class Ad_userActivity extends AppCompatActivity {
    private ActivityAdUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
    }
}
package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.classified.justsell.databinding.ActivityChatBinding;

public class chatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
    }
}
package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.classified.justsell.databinding.ActivityImageBinding;
import com.squareup.picasso.Picasso;

public class imageActivity extends AppCompatActivity {
    ActivityImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        Intent intent=getIntent();
        String image=intent.getStringExtra("image");
        Picasso.get().load(image).resize(1280,720).into(binding.bigImg2);

        binding.backbtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
       // overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
    }
}
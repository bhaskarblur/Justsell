package com.classified.justsell;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Adapters.imagesAdapter;
import com.classified.justsell.Adapters.textonlyAdapter;
import com.classified.justsell.CustomDialogs.askBoost_Dialog;
import com.classified.justsell.databinding.ActivityPostAllBinding;
import com.classified.justsell.databinding.ActivityPostPropertyBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class PostActivity_all extends AppCompatActivity {
    private ActivityPostAllBinding binding;
    private textonlyAdapter condAdapter;
    private Boolean warrantystat = false;
    private String condition;
    private imagesAdapter imagesAdapter;
    private List<String> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ManageData();
        viewfunc();
    }

    private void ManageData() {
        Intent intent=getIntent();
        String catname=intent.getStringExtra("catname");
        binding.catname.setText(catname);
        List<String> condList = new ArrayList<>();
        condList.add("New");
        condList.add("Old");

        condAdapter = new textonlyAdapter(PostActivity_all.this, condList);
        GridLayoutManager glm = new GridLayoutManager(PostActivity_all.this, 3);
        binding.condRec.setLayoutManager(glm);
        binding.condRec.setAdapter(condAdapter);
        condAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                condition = catname;
            }
        });
        binding.warrantySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    warrantystat = true;
                } else {
                    warrantystat = false;
                }
            }
        });

        imagesAdapter = new imagesAdapter(PostActivity_all.this, imagesList);
        LinearLayoutManager llm1 = new LinearLayoutManager(PostActivity_all.this);
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        binding.automobImg.setLayoutManager(llm1);
        binding.automobImg.setAdapter(imagesAdapter);
        imagesAdapter.setonTileClick(new imagesAdapter.onTileClick() {
            @Override
            public void onRemoveClick(int position) {
                imagesList.remove(position);
                imagesAdapter.notifyDataSetChanged();
            }
        });

    }

    private void viewfunc() {

        binding.registeruserimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity_all.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.cameraimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity_all.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.postAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (imagesList.size() < 2) {
//                    binding.automobImg.setFocusable(true);
//                    binding.automobImg.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please add atleast 2 images.", Toast.LENGTH_SHORT).show();
//                } else if (binding.titleTxt.getText().toString().isEmpty()) {
//                    binding.titleTxt.setFocusable(true);
//                    binding.titleTxt.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please enter title.", Toast.LENGTH_SHORT).show();
//
//                } else if (binding.prodnameTxt.getText().toString().isEmpty()) {
//                    binding.prodnameTxt.setFocusable(true);
//                    binding.prodnameTxt.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please enter product name.", Toast.LENGTH_SHORT).show();
//
//                } else if (binding.proddescTxt.getText().toString().isEmpty()) {
//                    binding.proddescTxt.setFocusable(true);
//                    binding.proddescTxt.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please enter description.", Toast.LENGTH_SHORT).show();
//
//                } else if (condition == null) {
//                    binding.condRec.setFocusable(true);
//                    binding.condRec.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please select a condition.", Toast.LENGTH_SHORT).show();
//                } else if (warrantystat == null) {
//                    binding.warrantySwitch.setFocusable(true);
//                    binding.warrantySwitch.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please tell about the warranty.", Toast.LENGTH_SHORT).show();
//                } else if (binding.brandTxt.getText().toString().isEmpty()) {
//                    binding.brandTxt.setFocusable(true);
//                    binding.brandTxt.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please enter the brand name.", Toast.LENGTH_SHORT).show();
//                } else if (binding.spTxt.getText().toString().isEmpty()) {
//                    binding.spTxt.setFocusable(true);
//                    binding.spTxt.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please enter the selling price.", Toast.LENGTH_SHORT).show();
//                } else if (binding.cpTxt.getText().toString().isEmpty()) {
//                    binding.cpTxt.setFocusable(true);
//                    binding.cpTxt.requestFocus();
//                    Toast.makeText(PostActivity_all.this, "Please enter the cost price.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Posting API Here
                    askBoost_Dialog dialog = new askBoost_Dialog();
                    dialog.show(getSupportFragmentManager(), "dialog");
//                }
            }
        });

        binding.backbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }

    private void startCropActivity() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imagesList.add(String.valueOf(result.getUri()));
                imagesAdapter.notifyDataSetChanged();
            }

        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}
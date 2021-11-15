package com.classified.justsell;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Adapters.imagesAdapter;
import com.classified.justsell.Adapters.textonlyAdapter;
import com.classified.justsell.CustomDialogs.askBoost_Dialog;
import com.classified.justsell.databinding.ActivityPostBinding;
import com.classified.justsell.databinding.ActivityPostPropertyBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostActivity_property extends AppCompatActivity {
    private ActivityPostPropertyBinding binding;
    private textonlyAdapter adsAdapter;
    private textonlyAdapter propAdapter;
    private textonlyAdapter seloneAdapter;
    private imagesAdapter imagesAdapter;
    private Boolean show_number=false;
    private String adtype;
    private String proptype;
    private String selone;
    private List<String> imagesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        ManageData();
        viewfunc();
    }

    private void ManageData() {
        Intent intent=getIntent();
        String catname=intent.getStringExtra("catname");
        binding.catname.setText(catname);
        List<String> adtypeList = new ArrayList<>();
        adtypeList.add("Rent");
        adtypeList.add("Sell");
        adtypeList.add("Lease");

        List<String> proptypeList=new ArrayList<>();
        proptypeList.add("Residential");
        proptypeList.add("Commerc.");
        proptypeList.add("Agriculture");

        List<String> seloneList=new ArrayList<>();
        seloneList.add("Plot/Land");
        seloneList.add("Apartment");
        seloneList.add("House");
        adsAdapter = new textonlyAdapter(PostActivity_property.this, adtypeList);
        GridLayoutManager glm = new GridLayoutManager(PostActivity_property.this, 3);
        binding.adtypeRec.setLayoutManager(glm);
        binding.adtypeRec.setAdapter(adsAdapter);
        adsAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                adtype = catname;
            }
        });

        propAdapter = new textonlyAdapter(PostActivity_property.this, proptypeList);
        GridLayoutManager glm1 = new GridLayoutManager(PostActivity_property.this, 3);
        binding.proptypeRec.setLayoutManager(glm1);
        binding.proptypeRec.setAdapter(propAdapter);
        propAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                proptype = catname;
            }
        });

        seloneAdapter = new textonlyAdapter(PostActivity_property.this, seloneList);
        GridLayoutManager glm2 = new GridLayoutManager(PostActivity_property.this, 3);
        binding.numofRec.setLayoutManager(glm2);
        binding.numofRec.setAdapter(seloneAdapter);
        seloneAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                selone = catname;
            }
        });

        binding.numberSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    show_number = true;
                } else {
                    show_number = false;
                }
            }
        });
        imagesAdapter = new imagesAdapter(PostActivity_property.this, imagesList);
        LinearLayoutManager llm1 = new LinearLayoutManager(PostActivity_property.this);
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
                    Toast.makeText(PostActivity_property.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.cameraimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity_property.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.postAutomobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (imagesList.size() < 2) {
//                    binding.automobImg.setFocusable(true);
//                    binding.automobImg.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please add atleast 2 images.", Toast.LENGTH_SHORT).show();
//                } else if (binding.titleTxt.getText().toString().isEmpty()) {
//                    binding.titleTxt.setFocusable(true);
//                    binding.titleTxt.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please enter title.", Toast.LENGTH_SHORT).show();
//
//                } else if (binding.prodnameTxt.getText().toString().isEmpty()) {
//                    binding.prodnameTxt.setFocusable(true);
//                    binding.prodnameTxt.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please enter product name.", Toast.LENGTH_SHORT).show();
//
//                } else if (binding.proddescTxt.getText().toString().isEmpty()) {
//                    binding.proddescTxt.setFocusable(true);
//                    binding.proddescTxt.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please enter description.", Toast.LENGTH_SHORT).show();
//
//                } else if (adtype == null) {
//                    binding.adtypeRec.setFocusable(true);
//                    binding.adtypeRec.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please select an ad type.", Toast.LENGTH_SHORT).show();
//                } else if (proptype == null) {
//                    binding.proptypeRec.setFocusable(true);
//                    binding.proptypeRec.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please select a property type.", Toast.LENGTH_SHORT).show();
//                }
//                 } else if (selone == null) {
//                    binding.numofRec.setFocusable(true);
//                    binding.numofRec.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please select any one.", Toast.LENGTH_SHORT).show();
//                } else if (binding.prodpriceTxt.getText().toString().isEmpty()) {
//                    binding.prodpriceTxt.setFocusable(true);
//                    binding.prodpriceTxt.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please enter the selling price.", Toast.LENGTH_SHORT).show();
//                } else if (binding.areaTxt.getText().toString().equals("Select Date")) {
//                    binding.areaTxt.setFocusable(true);
//                    binding.areaTxt.requestFocus();
//                    Toast.makeText(PostActivity_property.this, "Please enter the area.", Toast.LENGTH_SHORT).show();
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
package com.classified.justsell.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.HomeActivity;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.R;
import com.classified.justsell.databinding.FragmentRegisterProfileBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class registerProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private FragmentRegisterProfileBinding sdbinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userid;
    private Uri imguri;

    public registerProfile() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static registerProfile newInstance(String param1, String param2) {
        registerProfile fragment = new registerProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle = getArguments();
        userid = bundle.getString("userid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sdbinding = FragmentRegisterProfileBinding.inflate(inflater, container, false);

        viewfunc();
        loadfunc();
        return sdbinding.getRoot();
    }

    private void viewfunc() {
        sdbinding.cameraimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
            }
        });

        sdbinding.registeruserimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
            }
        });

        sdbinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imguri == null) {
                    Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT).show();
                } else if (sdbinding.nameTxt.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                } else if (sdbinding.statet.getText().toString().contains("Select")) {
                    Toast.makeText(getContext(), "Please select a state", Toast.LENGTH_SHORT).show();
                } else if (sdbinding.cityet.getText().toString().contains("Select")) {
                    Toast.makeText(getContext(), "Please select a city", Toast.LENGTH_SHORT).show();
                } else {
                    api_baseurl baseurl = new api_baseurl();

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    ApiWork apiWork = retrofit.create(ApiWork.class);
                    String base64img = "";
                    try {
                        if (imguri != null) {
                            InputStream is = getActivity().getContentResolver().openInputStream(imguri);
                            Bitmap image = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream by = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 50, by);
                            base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Call<AuthResponse.profile_update> call1 = apiWork.updateprofile(userid, sdbinding.nameTxt.getText().toString()
                            , sdbinding.statet.getText().toString(), sdbinding.cityet.toString(), base64img);

                    call1.enqueue(new Callback<AuthResponse.profile_update>() {
                        @Override
                        public void onResponse(Call<AuthResponse.profile_update> call, Response<AuthResponse.profile_update> response) {
                            if (!response.isSuccessful()) {
                                Log.d("error code",String.valueOf(response.code()));
                                return;
                            }
                          AuthResponse.profile_update resp=response.body();

                            Log.d("message", resp.getStatus());
                            if(resp.getResult()!=null) {
                                Toast.makeText(getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("userlogged","yes");
                                editor.putString("userimage",resp.getResult().getImage());
                                editor.putString("userid",resp.getResult().getId());
                                editor.putString("usermobile",resp.getResult().getMobile());
                                editor.putString("username",resp.getResult().getName());
                                editor.putString("userstate",resp.getResult().getState());
                                editor.putString("usercity",resp.getResult().getCity());
                                editor.commit();
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                            }
                            else {
                                Toast.makeText(getContext(), "There was an error!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        @Override
                        public void onFailure(Call<AuthResponse.profile_update> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void loadfunc() {
        api_baseurl baseurl = new api_baseurl();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl_market)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<AuthResponse.getstate> call = apiWork.getstate();

        call.enqueue(new Callback<AuthResponse.getstate>() {
            @Override
            public void onResponse(Call<AuthResponse.getstate> call, Response<AuthResponse.getstate> response) {
                if (!response.isSuccessful()) {
                    Log.d("error code:", String.valueOf(response.code()));
                    return;
                }

                AuthResponse.getstate statedata = response.body();
                ArrayList<String> statelist = new ArrayList<>();
                statelist.add("Select State");

                if (statedata.getResult() != null) {
                    for (int i = 0; i < statedata.getResult().size(); i++) {
                        statelist.add(statedata.getResult().get(i).getStatename());
                    }
                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, statelist);
                    sdbinding.statespin.setAdapter(listAdapter);
                    sdbinding.statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (!parent.getItemAtPosition(position).equals("Select State")) {
                                sdbinding.statet.setText(parent.getItemAtPosition(position).toString());
                                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl_market)
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                ApiWork apiWork = retrofit.create(ApiWork.class);
                                Call<AuthResponse.getcity> call = apiWork.getcity(statedata.getResult().get(position - 1).getId());

                                call.enqueue(new Callback<AuthResponse.getcity>() {
                                    @Override
                                    public void onResponse(Call<AuthResponse.getcity> call, Response<AuthResponse.getcity> response) {
                                        if (!response.isSuccessful()) {
                                            Log.d("error code:", String.valueOf(response.code()));
                                            return;
                                        }

                                        AuthResponse.getcity citydata = response.body();
                                        ArrayList<String> citylist = new ArrayList<>();
                                        citylist.add("Select City");
                                        if (citydata.getResult() != null) {
                                            for (int i = 0; i < citydata.getResult().size(); i++) {
                                                citylist.add(citydata.getResult().get(i).getCity());
                                            }
                                            ArrayAdapter<String> citylistAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,
                                                    citylist);
                                            sdbinding.cityspin.setAdapter(citylistAdapter);
                                            sdbinding.cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    if (!parent.getItemAtPosition(position).equals("Select City")) {
                                                        sdbinding.cityet.setText(parent.getItemAtPosition(position).toString());
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<AuthResponse.getcity> call, Throwable t) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AuthResponse.getstate> call, Throwable t) {

            }
        });

    }


    private void startCropActivity() {
        CropImage.activity()
                .start(getContext(), this);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imguri = result.getUri();
                sdbinding.cameraimg.setVisibility(View.GONE);
                sdbinding.registeruserimg.setColorFilter(null);
                sdbinding.registeruserimg.clearColorFilter();
                ImageViewCompat.setImageTintList(sdbinding.registeruserimg,null);
                final int radius = 150;
                final int margin = 50;
                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get().load(imguri).transform(new CropCircleTransformation()).into(sdbinding.registeruserimg);

            }

        }
    }
}
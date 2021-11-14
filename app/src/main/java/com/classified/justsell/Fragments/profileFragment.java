package com.classified.justsell.Fragments;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Adapters.FavadsAdapter;
import com.classified.justsell.Adapters.MyadsAdapter;
import com.classified.justsell.Adapters.promadsAdapter;
import com.classified.justsell.AuthActivity;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.HomeActivity;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;
import com.classified.justsell.ViewModels.profilefragViewModel;
import com.classified.justsell.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private FragmentProfileBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String editedimage;
    private SharedPreferences sharedPreferences;
    private String userid;
    private MyadsAdapter myadsAdapter;
    private FavadsAdapter favadsAdapter;
    private com.classified.justsell.Adapters.promadsAdapter promadsAdapter;
    private profilefragViewModel profilefragViewModel;
    api_baseurl baseurl = new api_baseurl();

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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
        sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        userid = sharedPreferences.getString("userid", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {
            noInternetFragment nocon = new noInternetFragment();
            FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction1.setCustomAnimations(R.anim.fade_2, R.anim.fade);
            transaction1.replace(R.id.mainFragment, nocon);
            transaction1.addToBackStack("A");
            transaction1.commit();
        } else {
            ManageData();
            viewfunc();
        }
        return binding.getRoot();
    }

    private void viewfunc() {
        binding.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup(v);
            }
        });

        binding.profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profileEditSave.setVisibility(View.VISIBLE);
                binding.profileEdit.setVisibility(View.INVISIBLE);
                binding.nameTxt.setVisibility(View.VISIBLE);
                binding.numberTxt.setVisibility(View.VISIBLE);
                binding.imgPick.setVisibility(View.VISIBLE);

            }
        });

        binding.imgPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
            }
        });

        binding.profileEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make API Call of update!
                if (editedimage == null) {
                    Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT).show();
                } else if (binding.nameTxt.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                } else {
                    String city = sharedPreferences.getString("usercity", "");
                    String state = sharedPreferences.getString("userstate", "");
                    api_baseurl baseurl = new api_baseurl();

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    ApiWork apiWork = retrofit.create(ApiWork.class);
                    String base64img = "";
                    try {
                        if (editedimage != null) {
                            InputStream is = getActivity().getContentResolver().openInputStream(Uri.parse(editedimage));
                            Bitmap image = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream by = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 50, by);
                            base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                            Log.d("base64img2", base64img);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Call<AuthResponse.profile_update> call1 = apiWork.updateprofile2(userid, binding.nameTxt.getText().toString()
                            , state, city, base64img, binding.numberTxt.getText().toString());

                    call1.enqueue(new Callback<AuthResponse.profile_update>() {
                        @Override
                        public void onResponse(Call<AuthResponse.profile_update> call, Response<AuthResponse.profile_update> response) {
                            if (!response.isSuccessful()) {
                                Log.d("error code", String.valueOf(response.code()));
                                return;
                            }
                            AuthResponse.profile_update resp = response.body();

                            Log.d("message", resp.getStatus());
                            if (resp.getResult() != null) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userlogged", "yes");
                                editor.putString("userimage", resp.getResult().getImage());
                                editor.putString("userid", resp.getResult().getId());
                                editor.putString("usermobile", resp.getResult().getMobile());
                                editor.putString("username", resp.getResult().getName());
                                editor.putString("userstate", resp.getResult().getState());
                                editor.putString("usercity", resp.getResult().getCity());
                                editor.commit();
                                binding.userName.setText(resp.getResult().getName());
                                binding.userNumber.setText(resp.getResult().getMobile());
                                binding.profileEditSave.setVisibility(View.INVISIBLE);
                                binding.profileEdit.setVisibility(View.VISIBLE);
                                binding.nameTxt.setVisibility(View.INVISIBLE);
                                binding.numberTxt.setVisibility(View.INVISIBLE);
                                binding.imgPick.setVisibility(View.INVISIBLE);

                            } else {
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

        binding.myadsTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myadsTxt.setVisibility(View.VISIBLE);
                binding.myadsTxt2.setVisibility(View.INVISIBLE);
                binding.adsLay.setVisibility(View.VISIBLE);

                binding.favsTxt.setVisibility(View.VISIBLE);
                binding.favsTxt3.setVisibility(View.INVISIBLE);
                binding.favsLay.setVisibility(View.INVISIBLE);

                binding.promTxt.setVisibility(View.VISIBLE);
                binding.promTxt2.setVisibility(View.INVISIBLE);
                binding.promoteLay.setVisibility(View.INVISIBLE);

            }
        });

        binding.favsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myadsTxt.setVisibility(View.INVISIBLE);
                binding.myadsTxt2.setVisibility(View.VISIBLE);
                binding.adsLay.setVisibility(View.INVISIBLE);

                binding.favsTxt.setVisibility(View.INVISIBLE);
                binding.favsTxt3.setVisibility(View.VISIBLE);
                binding.favsLay.setVisibility(View.VISIBLE);

                binding.promTxt.setVisibility(View.VISIBLE);
                binding.promTxt2.setVisibility(View.INVISIBLE);
                binding.promoteLay.setVisibility(View.INVISIBLE);
            }
        });

        binding.promTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myadsTxt.setVisibility(View.INVISIBLE);
                binding.myadsTxt2.setVisibility(View.VISIBLE);
                binding.adsLay.setVisibility(View.INVISIBLE);

                binding.favsTxt.setVisibility(View.VISIBLE);
                binding.favsTxt3.setVisibility(View.INVISIBLE);
                binding.favsLay.setVisibility(View.INVISIBLE);

                binding.promTxt.setVisibility(View.INVISIBLE);
                binding.promTxt2.setVisibility(View.VISIBLE);
                binding.promoteLay.setVisibility(View.VISIBLE);
            }
        });

    }

    private void startCropActivity() {
        CropImage.activity()
                .start(getContext(), this);

    }

    private void ManageData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<AuthResponse.VerifyOtp> call = apiWork.getprofile(userid);

        call.enqueue(new Callback<AuthResponse.VerifyOtp>() {
            @Override
            public void onResponse(Call<AuthResponse.VerifyOtp> call, Response<AuthResponse.VerifyOtp> response) {
                if (!response.isSuccessful()) {
                    Log.d("error code", String.valueOf(response.code()));
                    return;
                }

                AuthResponse.VerifyOtp resp = response.body();

                if (resp.getResult() != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userlogged", "yes");
                    editor.putString("userimage", resp.getResult().getImage());
                    editor.putString("userid", resp.getResult().getId());
                    editor.putString("usermobile", resp.getResult().getMobile());
                    editor.putString("username", resp.getResult().getName());
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse.VerifyOtp> call, Throwable t) {
                Log.d("Failure", t.getMessage());
            }
        });
        String name = sharedPreferences.getString("username", "");
        String number = sharedPreferences.getString("usermobile", "");
        String image = sharedPreferences.getString("userimage", "empty");
        editedimage = sharedPreferences.getString("userimage", "empty");

        binding.userName.setText(name);
        binding.userNumber.setText(number);
        binding.nameTxt.setText(name);
        binding.numberTxt.setText(number);
        if (image != null && !image.equals("empty")) {
            final int radius = 150;
            final int margin = 50;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.get().load(image).transform(new CropCircleTransformation()).into(binding.userImg);
        }

        profilefragViewModel = new ViewModelProvider(getActivity()).get(com.classified.justsell.ViewModels.profilefragViewModel.class);
        profilefragViewModel.initwork(userid);
        profilefragViewModel.getMyads().observe(getActivity(), new Observer<List<homeResponse.adsResult>>() {
            @Override
            public void onChanged(List<homeResponse.adsResult> adsResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adsResults.size() > 0) {
                            myadsAdapter.notifyDataSetChanged();
                        }
                    }
                }, 100);
            }
        });
        profilefragViewModel.getFavads().observe(getActivity(), new Observer<List<homeResponse.adsResult>>() {
            @Override
            public void onChanged(List<homeResponse.adsResult> adsResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adsResults.size() > 0) {
                            favadsAdapter.notifyDataSetChanged();
                        }
                    }
                }, 100);
            }
        });
        profilefragViewModel.getPromoteads().observe(getActivity(), new Observer<List<homeResponse.adsResult>>() {
            @Override
            public void onChanged(List<homeResponse.adsResult> adsResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adsResults.size() > 0) {
                            promadsAdapter.notifyDataSetChanged();
                        }
                    }
                }, 100);
            }
        });

        myadsAdapter = new MyadsAdapter(getContext(), profilefragViewModel.getMyads().getValue());
        promadsAdapter = new promadsAdapter(getContext(), profilefragViewModel.getPromoteads().getValue());
        favadsAdapter = new FavadsAdapter(getContext(), profilefragViewModel.getFavads().getValue());

        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        LinearLayoutManager llm3 = new LinearLayoutManager(getContext());

        binding.adsrec.setLayoutManager(llm1);
        binding.adsrec.setAdapter(myadsAdapter);

        binding.favrec.setLayoutManager(llm2);
        binding.favrec.setAdapter(favadsAdapter);

        binding.promrec.setLayoutManager(llm3);
        binding.promrec.setAdapter(promadsAdapter);


        favadsAdapter.setonitemClickListener(new FavadsAdapter.onitemClick() {
            @Override
            public void onHeartClick(String id) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ApiWork apiWork = retrofit.create(ApiWork.class);

                Call<AuthResponse.SendOtp> call1 = apiWork.remove_favourite(id,userid);

                call1.enqueue(new Callback<AuthResponse.SendOtp>() {
                    @Override
                    public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {
                        if(!response.isSuccessful()) {
                            Log.d("error code",String.valueOf(response.code()));
                            return;
                        }

                        AuthResponse.SendOtp resp=response.body();

                        if(resp.getCode().equals("200")) {
                            //
                            Toast.makeText(getContext(), "Ad removed from favourite.", Toast.LENGTH_SHORT).show();
                            getActivity().getViewModelStore().clear();
                            profileFragment homeFragment=new profileFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                            transaction.replace(R.id.mainFragment, homeFragment);
                            transaction.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                        Log.d("Failure",t.getMessage());
                    }
                });
            }
        });

    }

    private void showpopup(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.profoptionmenu);
        MenuItem item = popupMenu.getMenu().findItem(R.id.logout_item);
        SpannableString s = new SpannableString("Delete Account");
        item.setTitle(s);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#F24747")), 0, s.length(), 0);
        popupMenu.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCropActivity();
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
                editedimage = String.valueOf(result.getUri());
                final int radius = 150;
                final int margin = 50;
                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get().load(editedimage).transform(new CropCircleTransformation()).into(binding.userImg);

            }

        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.terms:
                // this is aboutus
                break;

//            case R.id.toglmode:
//                 toggle theme here
//                UiModeManager  uiModeManager = (UiModeManager)getActivity().getSystemService(UI_MODE_SERVICE);
//                if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
//                    uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
//                }
//                else {
//                    uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
//                }
//                break;
            case R.id.aboutbtn:
                // this is terms and conditions
                break;
            case R.id.logout_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("Delete?")
                        .setMessage("Do you want to Delete Account?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                ApiWork apiWork = retrofit.create(ApiWork.class);

                                Call<AuthResponse.SendOtp> call1 = apiWork.delete_account(userid);

                                call1.enqueue(new Callback<AuthResponse.SendOtp>() {
                                    @Override
                                    public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {
                                        if(!response.isSuccessful()) {
                                            Log.d("error code",String.valueOf(response.code()));
                                            return;
                                        }

                                        AuthResponse.SendOtp resp=response.body();

                                        if(resp.getCode().equals("200")) {
                                            Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.clear();
                                            editor.apply();
                                            startActivity(new Intent(getActivity(), AuthActivity.class));
                                            getActivity().finish();
                                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                                        Log.d("Failure",t.getMessage());
                                    }
                                });

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

        }
        return false;
    }
}
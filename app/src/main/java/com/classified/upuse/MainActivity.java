package com.classified.upuse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.classified.upuse.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color));
        }

        handleFirebaseMessageIntent();
    }

    private void handleFirebaseMessageIntent() {

        SharedPreferences shpref = getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");

        if (!userid.isEmpty() || !userid.equals("")) {

            if (getIntent() != null) {
                if (getIntent().getStringExtra("noti_type") != null) {
                    String noti_type = getIntent().getStringExtra("noti_type");

                    if (noti_type.equals("product")) {
                        Log.d("noti_type",noti_type);
                        String product_id = getIntent().getStringExtra("order_id");
                        String user_id = getIntent().getStringExtra("user_id");
                        String catname= getIntent().getStringExtra("category_name");
                        Intent openact;
                        openact = new Intent(MainActivity.this,Ad_posterActivity.class);
                        openact.putExtra("ad_id", product_id);
                        openact.putExtra("user_id", user_id);
                        openact.putExtra("cat_name", catname);
                        startActivity(openact);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                    }

                    else if(noti_type.equals("chat")) {
                        Log.d("noti_type",noti_type);
                        String product_id = getIntent().getStringExtra("product_id");
                        String user_id = getIntent().getStringExtra("user_id");
                        String person_id=getIntent().getStringExtra("person_id");
                        Intent openact = new Intent(MainActivity.this, chatActivity.class);
                        openact.putExtra("product_id", product_id);
                        openact.putExtra("user_id", user_id);
                        openact.putExtra("person_id",person_id);
                        startActivity(openact);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    }

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this,AuthActivity.class));
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                            finish();
                        }
                    },2000);
                }
            }
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this,AuthActivity.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                    finish();
                }
            },2000);
        }

    }

}
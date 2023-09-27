package com.classified.upuse.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.classified.upuse.R;
import com.classified.upuse.adposted_successful;
import com.classified.upuse.promote_ad;

public class askBoost_Dialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.askboost_dialog,null);
        builder.setView(view);
        View prombtn=view.findViewById(R.id.prombtn);
        View skipbtn=view.findViewById(R.id.skipbtn);
        Bundle bundle=getArguments();
        String adid=bundle.getString("ad_id");
        prombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), promote_ad.class);
                intent.putExtra("ad_id",adid);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), adposted_successful.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                getActivity().finish();

            }
        });
        return builder.create();

    }
}

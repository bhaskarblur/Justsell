package com.classified.justsell.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.classified.justsell.R;
import com.classified.justsell.adposted_successful;
import com.classified.justsell.promote_ad;

public class deletepost_Dialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.deletepost_dialog,null);
        builder.setView(view);

        View delbtn=view.findViewById(R.id.delbtn_done);
        View cancbtn=view.findViewById(R.id.cancbtn);

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the ad
                getActivity().finish();
            }
        });

        cancbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        return builder.create();

    }
}

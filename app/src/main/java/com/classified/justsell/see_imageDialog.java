package com.classified.justsell;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.classified.justsell.R;
import com.classified.justsell.adposted_successful;
import com.classified.justsell.promote_ad;
import com.squareup.picasso.Picasso;

public class see_imageDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.fullscreenalert);
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.see_imagedialog,null);
        builder.setView(view);
        ImageView bigimg=view.findViewById(R.id.big_img);
        View backbtn=view.findViewById(R.id.backbtn8);
        Bundle bundle=getArguments();
        String image=bundle.getString("image");
        Picasso.get().load(image).into(bigimg);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }
}

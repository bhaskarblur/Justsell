package com.classified.upuse.helpingCode;

import android.app.ProgressDialog;
import android.content.Context;
import com.classified.upuse.R;

public class progressDialog {

    private ProgressDialog progressDialog;
    public void showLoadingDialog(Context context, String title, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideLoadingDialog() {

        progressDialog.cancel();
    }
}
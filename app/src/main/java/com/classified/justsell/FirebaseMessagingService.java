package com.classified.justsell;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("token", s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();
        final String CHANNEL="APP_NOTIFICATION";
        NotificationChannel channel=new NotificationChannel(CHANNEL,
                "App Notification", NotificationManager.IMPORTANCE_HIGH);



        Intent actintent = new Intent(this,MainActivity.class);
        if(remoteMessage.getData()!=null) {
            Log.d("noti_type",remoteMessage.getData().get("noti_type"));
            if(remoteMessage.getData().get("noti_type").equals("product")) {
                actintent.putExtra("noti_type", remoteMessage.getData().get("noti_type"));
                actintent.putExtra("product_id",remoteMessage.getData().get("product_id"));
                actintent.putExtra("user_id", remoteMessage.getData().get("user_id"));
                actintent.putExtra("category_name", remoteMessage.getData().get("category_name"));

            }

            else if(remoteMessage.getData().get("noti_type").equals("chat")) {
                actintent.putExtra("noti_type", remoteMessage.getData().get("noti_type"));
                actintent.putExtra("person_id",remoteMessage.getData().get("person_id"));
                actintent.putExtra("product_id",remoteMessage.getData().get("product_id"));
                actintent.putExtra("user_id", remoteMessage.getData().get("user_id"));
            }

            PendingIntent.getActivity(this,01,actintent,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent intent=PendingIntent.getActivity(this,01,actintent,PendingIntent.FLAG_UPDATE_CURRENT);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder builder=new Notification.Builder(this,CHANNEL)
                .setContentTitle(title).setContentText(body)
                .setSmallIcon(R.mipmap.app_logo_main)
                .setAutoCancel(true).setContentIntent(intent);

        NotificationManagerCompat.from(this).notify(1,builder.build());


        super.onMessageReceived(remoteMessage);

    }

}

package com.classified.upuse;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.classified.upuse.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

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
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        final String CHANNEL = "APP_NOTIFICATION";

        NotificationChannel channel = new NotificationChannel(CHANNEL,
                "App Notification", NotificationManager.IMPORTANCE_HIGH);


        Log.d("activity", String.valueOf(isActivityRunning()));
        if (!isActivityRunning()) {
            Intent actintent = new Intent(this, MainActivity.class);
            if (remoteMessage.getData() != null) {
                Log.d("noti_type", remoteMessage.getData().get("noti_type"));
                if (remoteMessage.getData().get("noti_type").equals("product")) {
                    actintent.putExtra("noti_type", remoteMessage.getData().get("noti_type"));
                    actintent.putExtra("product_id", remoteMessage.getData().get("product_id"));
                    actintent.putExtra("user_id", remoteMessage.getData().get("user_id"));
                    actintent.putExtra("category_name", remoteMessage.getData().get("category_name"));

                } else if (remoteMessage.getData().get("noti_type").equals("chat")) {
                    actintent.putExtra("noti_type", remoteMessage.getData().get("noti_type"));
                    actintent.putExtra("person_id", remoteMessage.getData().get("person_id"));
                    actintent.putExtra("product_id", remoteMessage.getData().get("product_id"));
                    actintent.putExtra("user_id", remoteMessage.getData().get("user_id"));
                }

                PendingIntent.getActivity(this, 01, actintent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            PendingIntent intent = PendingIntent.getActivity(this, 01, actintent, PendingIntent.FLAG_UPDATE_CURRENT);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(this, CHANNEL)
                    .setContentTitle(title).setContentText(body)
                    .setSmallIcon(R.mipmap.app_logo_main)
                    .setAutoCancel(true).setContentIntent(intent);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            NotificationManagerCompat.from(this).notify(1, builder.build());

        }
        super.onMessageReceived(remoteMessage);

    }


    public boolean isActivityRunning() {

        ActivityManager activityManager = (ActivityManager)this.getSystemService (Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;
        for (int i = 0; i < activitys.size(); i++) {
            if (activitys.get(i).topActivity.getClassName().equals("com.classified.upuse.chatActivity")) {
                isActivityFound = true;
            }
        }
        return isActivityFound;
    }

}

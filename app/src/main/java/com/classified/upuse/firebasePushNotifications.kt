package com.classified.upuse

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.classified.upuse.MainActivity
import com.classified.upuse.R
import com.classified.upuse.chatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class firebasePushNotifications : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
   Log.d(TAG, "Refreshed token: $token")
  }

 @RequiresApi(Build.VERSION_CODES.O)
 override fun onMessageReceived(remoteMessage: RemoteMessage) {

  if (remoteMessage.notification != null) {

   Log.d(TAG, "From: " + remoteMessage.from)
   val title = remoteMessage.notification!!.title
   val body = remoteMessage.notification!!.body
   val CHANNEL = "APP_NOTIFICATION"

   val channel = NotificationChannel(
    CHANNEL,
    "App Notification", NotificationManager.IMPORTANCE_HIGH
   )


    val actintent = Intent(this, MainActivity::class.java)
    if (!remoteMessage.data.isEmpty()) {
     if (remoteMessage.data["noti_type"] == "product") {
      actintent.putExtra("noti_type", remoteMessage.data["noti_type"])
      actintent.putExtra("product_id", remoteMessage.data["product_id"])
      actintent.putExtra("user_id", remoteMessage.data["user_id"])
      actintent.putExtra("category_name", remoteMessage.data["category_name"])
     }
     else if (remoteMessage.data["noti_type"] == "chat") {
      actintent.putExtra("noti_type", remoteMessage.data["noti_type"])
      actintent.putExtra("person_id", remoteMessage.data["person_id"])
      actintent.putExtra("product_id", remoteMessage.data["product_id"])
      actintent.putExtra("user_id", remoteMessage.data["user_id"])
     }
     PendingIntent.getActivity(this, 1, actintent,
      PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    }
    val intent = PendingIntent.getActivity(this, 1, actintent,
     PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

    val builder: Notification.Builder = Notification.Builder(this, CHANNEL)
     .setContentTitle(title).setContentText(body)
     .setSmallIcon(R.mipmap.upuse_logo)
     .setAutoCancel(true).setContentIntent(intent)

   val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
   // Check if the Android Version is greater than Oreo
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val notificationChannel = NotificationChannel(
     CHANNEL, "call",
     NotificationManager.IMPORTANCE_HIGH
    )
    notificationManager.createNotificationChannel(notificationChannel)
   }
   Log.d("notify", "yesPlz");
   notificationManager.notify(0, builder.build())

  }

 }

 fun showNotification(
  title: String?,
  message: String?
 ) {
  // Pass the intent to switch to the MainActivity
  val intent = Intent(this, chatActivity::class.java)
  // Assign channel ID
  val channel_id = "notification_channel"
  // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
  // the activities present in the activity stack,
  // on the top of the Activity that is to be launched
  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  // Pass the intent to PendingIntent to start the
  // next Activity
  val pendingIntent = PendingIntent.getActivity(
   this, 0, intent,
   PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
  )

  // Create a Builder object using NotificationCompat
  // class. This will allow control over all the flags
  var builder: NotificationCompat.Builder = NotificationCompat.Builder(
   applicationContext,
   channel_id
  )
   .setContentIntent(pendingIntent)

  // A customized design for the notification can be
  // set only for Android versions 4.1 and above. Thus
  // condition for the same is checked here.
  if (Build.VERSION.SDK_INT
   >= Build.VERSION_CODES.JELLY_BEAN
  ) {
   builder = builder.setContentTitle(title)
    .setContentText(message)
//    .setSmallIcon(R.drawable.presence_video_online)
  } // If Android Version is lower than Jelly Beans,
  else {
   builder = builder.setContentTitle(title)
    .setContentText(message)
//    .setSmallIcon(R.drawable.presence_video_online)
    .setSound(Uri.parse("https://res.cloudinary.com/dsnb1bl19/video/upload/v1695660666/slssaapl93zuq7cuocg7.wav"))
  }

  val notificationManager = getSystemService(
   Context.NOTIFICATION_SERVICE
  ) as NotificationManager
  // Check if the Android Version is greater than Oreo
  if (Build.VERSION.SDK_INT
   >= Build.VERSION_CODES.O
  ) {
   val notificationChannel = NotificationChannel(
    channel_id, "call",
    NotificationManager.IMPORTANCE_HIGH
   )
   notificationManager.createNotificationChannel(
    notificationChannel
   )
  }
  notificationManager.notify(0, builder.build())
 }
}
package com.codemagos.catchmyride.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.codemagos.catchmyride.DriverIncommingCall;
import com.codemagos.catchmyride.LoginActivity;
import com.codemagos.catchmyride.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by prasanth on 16/3/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Catch My Ride | FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String type = remoteMessage.getData().get("type").toString();
        switch (type){
            case "call":
                call(remoteMessage.getData().get("id").toString(),remoteMessage.getData().get("name").toString(),remoteMessage.getData().get("destination").toString(),remoteMessage.getData().get("latitude").toString(),remoteMessage.getData().get("longitude").toString());
                break;
            case "notification":
                sendNotification(remoteMessage.getData().get("title").toString(), remoteMessage.getData().get("message").toString());
                break;

        }


    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Catch My Ride | " + title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public void call(String id,String name,String destination,String latitude, String longitude) {
        Intent intent = new Intent(getApplicationContext(), DriverIncommingCall.class);
        intent.putExtra("id", id); // passenger ID
        intent.putExtra("name", name);
        intent.putExtra("destination", destination);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}

package com.codemagos.catchmyride.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.codemagos.catchmyride.DriverHomeActivity;
import com.codemagos.catchmyride.DriverIncommingCall;
import com.codemagos.catchmyride.LoginActivity;
import com.codemagos.catchmyride.NotVerifiedActivity;
import com.codemagos.catchmyride.PassengerHomeActivity;
import com.codemagos.catchmyride.R;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
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
            case "response":
                response(remoteMessage.getData().get("driver_id").toString(),remoteMessage.getData().get("driver_name").toString(),remoteMessage.getData().get("status").toString());
                break;
            case "notification":
                sendNotification(remoteMessage.getData().get("title").toString(), remoteMessage.getData().get("message").toString(),remoteMessage.getData().get("meta").toString());
                break;

        }


    }

    private void sendNotification(String title, String messageBody,String meta) {
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
        if(meta.equals("exit")){
            new SharedPreferencesStore(getApplicationContext()).isVerified(false);
            Intent i = new Intent(getApplicationContext(), NotVerifiedActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
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
    public void response(String driver_id,String driver_name,String status) {
        Intent intent = new Intent(getApplicationContext(), DriverIncommingCall.class);
        intent.putExtra("driver_id", driver_id); // passenger ID
        intent.putExtra("driver_name", driver_name);
        intent.putExtra("status", status);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //getApplicationContext().startActivity(intent);
      Log.e("--->","hehe" + status);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(PassengerHomeActivity.class);
        stackBuilder.addNextIntent(new Intent(getApplicationContext(),PassengerHomeActivity.class));
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //Toast.makeText(context, balance, Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext())
                // Set Icon
                .setSmallIcon(R.mipmap.ic_launcher)
                // Set Ticker Message
                .setTicker("Wallet")
                // Set Title
                .setContentTitle("Reminder")
                // Set Text
                .setContentText(status)
                // Add an Action Button below Notification
                // Set PendingIntent into Notification
                .setContentIntent(pendingIntent)
                // Dismiss Notification
                .setAutoCancel(false);

        // Create Notification Manager
        NotificationManager  mNotificationmanager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        mNotificationmanager.notify(1, builder.build());
    }
}

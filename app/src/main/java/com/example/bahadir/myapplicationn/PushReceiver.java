package com.example.bahadir.myapplicationn;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class PushReceiver extends BroadcastReceiver
{
    private static final int uniqueID = 31331;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("tago", "notification oluşturuldu ");
        Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();
        //-----------------------------
        // Create a test notification
        //
        // (Use deprecated notification
        // API for demonstration purposes,
        // to avoid having to import
        // the Android Support Library)
        //-----------------------------

        String notificationTitle = "Pushy";
        String notificationDesc = "Test notification";

        //-----------------------------
        // Attempt to grab the message
        // property from the payload
        //
        // We will be sending the following
        // test push notification:
        //
        // {"message":"Hello World!"}
        //-----------------------------

        if ( intent.getStringExtra("message") != null )
        {
            notificationDesc = intent.getStringExtra("message");
        }

        //-----------------------------
        // Create a test notification
        //-----------------------------

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setContentText(notificationDesc);
        notification.setContentTitle(notificationTitle);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setWhen(System.currentTimeMillis());
        Intent i = new Intent(context , PushReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 , i , PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
        Log.i("tago" ,"notification oluşturuldu " + notificationDesc + " " + notificationTitle);
        //-----------------------------
        // Sound + vibrate + light
        //-----------------------------

        //notification.defaults = Notification.DEFAULT_ALL;

        //-----------------------------
        // Dismisses when pressed
        //-----------------------------

        //notification.flags = Notification.FLAG_AUTO_CANCEL;

        //-----------------------------
        // Create pending intent
        // without a real intent
        //-----------------------------

        //notification.setLatestEventInfo(context, notificationTitle, notificationDesc, null);

        //-----------------------------
        // Get notification manager
        //-----------------------------

        //NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //-----------------------------
        // Show the notification
        //-----------------------------

        //mNotificationManager.notify(0, notification);
    }
}
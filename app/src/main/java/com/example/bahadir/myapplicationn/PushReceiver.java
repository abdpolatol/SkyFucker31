package com.example.bahadir.myapplicationn;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class PushReceiver extends BroadcastReceiver
{
    boolean notificationver = true;
    private static final int uniqueID = 31331;

    public void onReceive(Context context, Intent intent)
    {
        notificationSharedAl(context);
        Log.i("tago", "notification oluşturuldu ");
        //Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();

        String notificationTitle = "Shappy";
        String notificationDesc = "Test notification";
        String yazaninid = "Defaultid";
        String yazaninurl = "Defaulturl";
        String yazaninadi = "Defaultisim";
        Bundle bundle;

        if ( intent.getStringExtra("message") != null )
        {
            notificationDesc = intent.getStringExtra("message");
            if(intent.getStringExtra("message").substring(0,18).equals("default kanal chat")){
                bundle = intent.getExtras();
                Intent inte = new Intent("groupchatbroadcast");
                inte.putExtra("mesaj" , bundle.getString("message"));
                context.sendBroadcast(inte);
                Log.i("tago" , "groupchat broadcast gönderildi");
            }

        }

        bundle = intent.getExtras();
        Intent inte = new Intent("broadCastName");
        inte.putExtra("mesaj", bundle.getString("message"));
        context.sendBroadcast(inte);
        Log.i("tago", "broadcast gonderildi");
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        String date = String.valueOf(hour) + ":" + String.valueOf(minute);
        DatabaseClass db = new DatabaseClass(context);
        db.open();
        db.olusturx(bundle.getString("message"),bundle.getString("id"),date);
        db.close();
        //-----------------------------
        // Create a test notification
        //-----------------------------
        if(notificationver) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setContentText(notificationDesc);
            notification.setContentTitle(notificationTitle);
            notification.setSmallIcon(R.mipmap.ozerprof);
            notification.setWhen(System.currentTimeMillis());
            String duzenlenmemisisim= bundle.getString("message");
            int bitisbolumu = duzenlenmemisisim.indexOf(">");
            String duzenlenmisisim = null;
            if(bitisbolumu!= -1){
                String azduzenlenmisisim = duzenlenmemisisim.substring(0,bitisbolumu);
                int uzunluk = azduzenlenmisisim.length();
                duzenlenmisisim = azduzenlenmisisim.substring(0,(uzunluk-20));
            }
            Intent i = new Intent(context, Mesajlasma.class);
            i.putExtra("mesaj",bundle.getString("message"));
            i.putExtra("yazaninid" , bundle.getString("id"));
            i.putExtra("yazaninurl" , bundle.getString("url"));
            i.putExtra("yazaninisim" , duzenlenmisisim);
            i.putExtra("intentname" , "PushReceiver");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);

            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            nm.notify(uniqueID, notification.build());

            Log.i("tago", "notification oluşturuldu " + notificationDesc + " " + notificationTitle);
        }
        //-----------------------------
        // Sound + vibrate + light
        //-----------------------------

    }

    private void notificationSharedAl(Context context) {
        SharedPreferences sP = context.getSharedPreferences("notification",Context.MODE_PRIVATE);
        notificationver = sP.getBoolean("notificationver" , true);
    }
}
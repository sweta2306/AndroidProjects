package com.example.a1406074.grivancecell.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.a1406074.grivancecell.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MessageReceiver extends BroadcastReceiver {
    int UNIQUE_ID=14552;
    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Bundle extras=intent.getExtras();

        NotificationCompat.Builder  notification= new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);


        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("Notification from Grivance Cell");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Message");

        Log.v("Vabroadcast",MyService.Value_Message);

        notification.setContentText(MyService.Value_Message);

     /*   Intent intents=new  Intent(context,CheckPassDetails.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intents,PendingIntent.FLAG_NO_CREATE);
        notification.setContentIntent(pendingIntent); */

        NotificationManager nm=(NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(UNIQUE_ID,notification.build());
    }
}

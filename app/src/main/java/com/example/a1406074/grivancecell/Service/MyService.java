package com.example.a1406074.grivancecell.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.a1406074.grivancecell.R;
import com.example.a1406074.grivancecell.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {


    public static String Value_Message;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    NotificationCompat.Builder notification;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notifications");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.v("Chilc","helloiz");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String stringBuilder=new String();
                Log.v("Check","Hello123");

                Log.v("Message",s+"....");

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    try
                    {
                        Log.v("Serta",ds.getValue(String.class));

                       Value_Message=ds.getValue(String.class);
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                        {

                            if( !FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("authority@gmail.com"))
                            {

                                Intent Notify=new Intent();
                                Notify.setAction("Grivance_cell_message");
                                Log.v("Values",stringBuilder);
                                Notify.putExtra("Message",Value_Message);
                                sendBroadcast(Notify);

                            }
                        }
                    }catch (Exception e)
                    {
                        Log.v("Serta",e.getLocalizedMessage());
                    }
                }




            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return START_STICKY;

    }
}

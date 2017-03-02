package com.example.a1406074.grivancecell;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by 1406074 on 17-02-2017.
 */

public class GrivanceCell extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }


}

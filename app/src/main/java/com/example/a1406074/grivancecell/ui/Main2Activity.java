package com.example.a1406074.grivancecell.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.example.a1406074.grivancecell.FireChatHelper.ExtraIntent;
import com.example.a1406074.grivancecell.R;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle extras=getIntent().getExtras();
        String RecpId=extras.getString(ExtraIntent.EXTRA_RECIPIENT_ID);

        Intent i=new Intent(Main2Activity.this,ChatActivity.class);
       // i.putExtra(ExtraIntent.EXTRA_CURRENT_USER_ID,mAuth.getCurrentUser().getUid() );
        i.putExtra(ExtraIntent.EXTRA_RECIPIENT_ID, RecpId);
        startActivity(i);


    }

}

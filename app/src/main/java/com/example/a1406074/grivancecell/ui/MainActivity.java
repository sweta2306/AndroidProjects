package com.example.a1406074.grivancecell.ui;
//concerned part
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.a1406074.grivancecell.adapter.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.a1406074.grivancecell.R;
import com.example.a1406074.grivancecell.login.LogInActivity;
import com.example.a1406074.grivancecell.model.User;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {




    private RecyclerView mUsersRecyclerView;

    private ArrayList<User> Users=new ArrayList<User>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private ChildEventListener mChildEventListener;
    private RecyclerViewAdapter mUsersChatAdapter;
    public static String ReceipID;
    public int TO_REMOVE;
    public int count=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Holo);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){

            Intent Login = new Intent(MainActivity.this,LogInActivity.class);
            finish();
            startActivity(Login);

        }
        else
        {


            mUsersRecyclerView=(RecyclerView) findViewById(R.id.recycler_view_users);

            mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAuth=FirebaseAuth.getInstance();



            mUserRefDatabase=FirebaseDatabase.getInstance().getReference().child("users");
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
            databaseReference.child("connection").setValue("online");

            // mUserRefDatabase
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Users=new ArrayList<User>();
                    count=-1;
                    for(DataSnapshot UserDetails: dataSnapshot.getChildren())
                    {

                        User Profile=UserDetails.getValue(User.class);
                        Users.add(Profile);
                        count++;
                        if (Profile.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            TO_REMOVE=count;
                        }
                        Log.v("DisplayName",Profile.displayName);
                    }

                    Users.remove(TO_REMOVE);


                /*mUsersChatAdapter = new UsersChatAdapter(MainActivity.this,Users);
                mUsersRecyclerView.setAdapter(mUsersChatAdapter); */


                    //  FirebaseUser mUt=     mAuth.getCurrentUser();


                    //     mAuth.getCurrentUser().

                    mUsersChatAdapter = new RecyclerViewAdapter(Users,MainActivity.this,mAuth);
                    mUsersRecyclerView.setAdapter(mUsersChatAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
            databaseReference.child("connection").setValue("offline").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {



                    mAuth.signOut();


                    Intent REDIRECT=new Intent(MainActivity.this,LogInActivity.class);
                    finish();
                    startActivity(REDIRECT);


                }
            });
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mAuth!=null)
        {

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
            databaseReference.child("connection").setValue("offline");


        }
    }
}
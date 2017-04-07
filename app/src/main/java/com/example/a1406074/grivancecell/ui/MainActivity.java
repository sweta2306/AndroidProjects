package com.example.a1406074.grivancecell.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a1406074.grivancecell.Service.MyService;
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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{




    private RecyclerView mUsersRecyclerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ArrayList<User> Users = new ArrayList<User>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private ChildEventListener mChildEventListener;
    private RecyclerViewAdapter mUsersChatAdapter;
    private DatabaseReference NotificationRef;
    public static String ReceipID;
    public int TO_REMOVE;
    public int count = -1;
    TextView raised,pending,solved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Holo);
        setContentView(R.layout.activity_main);

        NotificationRef=FirebaseDatabase.getInstance().getReference().child("Notifications").child("AuthorityMessage");
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        raised=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.raised));
        solved=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.resolved));
        pending=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.pending));
        initializeCountDrawer();


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent Login = new Intent(MainActivity.this, LogInActivity.class);
            finish();
            startActivity(Login);

        } else if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("authority@gmail.com")) {
            Intent Chat = new Intent(MainActivity.this, Chat2Activity.class);
            finish();
            startActivity(Chat);

        } else {


            mUsersRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_users);

            mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAuth = FirebaseAuth.getInstance();


            mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
            databaseReference.child("connection").setValue("online");

            // mUserRefDatabase
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Users = new ArrayList<User>();
                    count = -1;
                    for (DataSnapshot UserDetails : dataSnapshot.getChildren()) {

                        User Profile = UserDetails.getValue(User.class);
                        Users.add(Profile);
                        count++;
                        if (Profile.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            TO_REMOVE = count;
                        }
                        Log.v("DisplayName", Profile.displayName);
                    }

                    Users.remove(TO_REMOVE);


                /*mUsersChatAdapter = new UsersChatAdapter(MainActivity.this,Users);
                mUsersRecyclerView.setAdapter(mUsersChatAdapter); */


                    //  FirebaseUser mUt=     mAuth.getCurrentUser();


                    //     mAuth.getCurrentUser().

                    mUsersChatAdapter = new RecyclerViewAdapter(Users, MainActivity.this, mAuth);
                    mUsersRecyclerView.setAdapter(mUsersChatAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


    }
    private void initializeCountDrawer(){
        solved.setGravity(Gravity.CENTER_VERTICAL);
        solved.setTypeface(null, Typeface.BOLD);
        solved.setTextColor(getResources().getColor(R.color.colorAccent));
        solved.setText("99+");
        raised.setGravity(Gravity.CENTER_VERTICAL);
        raised.setTypeface(null,Typeface.BOLD);
        raised.setTextColor(getResources().getColor(R.color.colorAccent));
        raised.setText("7");
        pending.setGravity(Gravity.CENTER_VERTICAL);
        pending.setTypeface(null,Typeface.BOLD);
        pending.setTextColor(getResources().getColor(R.color.colorAccent));
        pending.setText("7");
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.resolved) {
            // Handle the camera action
        } else if (id == R.id.raised) {

        } else if (id == R.id.pending) {

        }
        else  if(id==R.id.Broadcast)
        {
            Dialog d= new Dialog(MainActivity.this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.writemessage);

            final EditText editText=(EditText) d.findViewById(R.id.DialogText);
            Button button=(Button) d.findViewById(R.id.Submit_ID);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String Message=editText.getText().toString().trim();
                    if(!TextUtils.isEmpty(Message)) {
                        NotificationRef.push().setValue(Message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Snackbar.make(MainActivity.this.findViewById(R.id.activity_main),"Success",Snackbar.LENGTH_SHORT).show();

                            }
                        });
                    }


                }
            });

            NotificationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.v("DatasnapshotValue",dataSnapshot.toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            d.show();


        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
            databaseReference.child("connection").setValue("offline").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    mAuth.signOut();

                  //  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                  //  databaseReference.child("connection").setValue("offline");


                    Intent REDIRECT = new Intent(MainActivity.this, LogInActivity.class);
                    finish();
                    startActivity(REDIRECT);


                }
            });
            return true;
        }if(mToggle.onOptionsItemSelected(item)){
            return true;

        }

        return super.onOptionsItemSelected(item);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAuth != null) {

            //DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
            //databaseReference.child("connection").setValue("offline");


        }
    }
}

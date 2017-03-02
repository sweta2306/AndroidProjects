package com.example.a1406074.grivancecell.ui;

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


/*
* CAUTION: This app is still far away from a production app
* Note: (1) Still fixing some code, and functionality and
*       I don't use FirebaseUI, but recommend you to use it.
* */

public class MainActivity extends AppCompatActivity {


    private static String TAG = MainActivity.class.getSimpleName();




    private RecyclerView mUsersRecyclerView;
    private String mCurrentUserUid;
    private List<String> mUsersKeyList;

    private ArrayList<User> Users=new ArrayList<User>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private ChildEventListener mChildEventListener;
    private RecyclerViewAdapter mUsersChatAdapter;
    public static String ReceipID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Holo);


        String date= new SimpleDateFormat("dd/mm/yyyy hh:mma").format(new Date(System.currentTimeMillis()));

        Log.v("Date",date);

        setContentView(R.layout.activity_main);
        mUsersRecyclerView=(RecyclerView) findViewById(R.id.recycler_view_users);

        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth=FirebaseAuth.getInstance();

       /* if(mAuth.getCurrentUser()==null)
        {
            Intent Login = new Intent(MainActivity.this,LogInActivity.class);
            finish();
            startActivity(Login);
        }*/

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //FirebaseUser user = firebaseAuth.getCurrentUser();
               // mAuthListener=FirebaseAuth.AuthStateListener.getInstance();
                if(firebaseAuth.getCurrentUser() != null){

                    Intent Login = new Intent(MainActivity.this,LogInActivity.class);
                    finish();
                    startActivity(Login);

                }
            }
        };

        mUserRefDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mUserRefDatabase.keepSynced(true);
        mUserRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot UserDetails: dataSnapshot.getChildren())
                {
                    User Profile=UserDetails.getValue(User.class);
                    Users.add(Profile);
                    Log.v("DisplayName",Profile.displayName);
                }
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
    /* private void bindButterKnife() {
     ButterKnife.bind(this);
 }

 private void setAuthInstance() {
     mAuth = FirebaseAuth.getInstance();
 }

 private void retrieveUsers()
 {
     mUserRefDatabase.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {

             for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
             {

                 User members=dataSnapshot1.getValue(User.class);
                 Users.add(members);
                 Log.v("Check",members.displayName);


             }
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });
 }

 private void setUsersDatabase() {
     mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
 }

 private void setUserRecyclerView() {
     mUsersChatAdapter = new UsersChatAdapter(this, new ArrayList<User>());
     mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
     mUsersRecyclerView.setHasFixedSize(true);
     mUsersRecyclerView.setAdapter(mUsersChatAdapter);
 }

 private void setUsersKeyList() {
     mUsersKeyList = new ArrayList<String>();
 }

 private void setAuthListener() {
     mAuthListener = new FirebaseAuth.AuthStateListener() {
         @Override
         public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

             hideProgressBarForUsers();
             FirebaseUser user = firebaseAuth.getCurrentUser();

             if (user != null) {
                 setUserData(user);
                 queryAllUsers();
             } else {
                 // User is signed out
                 goToLogin();
             }
         }
     };
 }

 private void setUserData(FirebaseUser user) {
     mCurrentUserUid = user.getUid();
 }

 private void queryAllUsers() {
     mChildEventListener = getChildEventListener();
     mUserRefDatabase.limitToFirst(50).addChildEventListener(mChildEventListener);
 }

 private void goToLogin() {
     Intent intent = new Intent(this, LogInActivity.class);
     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // LoginActivity is a New Task
     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // The old task when coming back to this activity should be cleared so we cannot come back to it.
     startActivity(intent);
 }

 @Override
 public void onStart() {
     super.onStart();
     showProgressBarForUsers();
     mAuth.addAuthStateListener(mAuthListener);
 }

 @Override
 public void onStop() {
     super.onStop();

     clearCurrentUsers();

     if (mChildEventListener != null) {
         mUserRefDatabase.removeEventListener(mChildEventListener);
     }

     if (mAuthListener != null) {
         mAuth.removeAuthStateListener(mAuthListener);
     }

 }

 private void clearCurrentUsers() {
     mUsersChatAdapter.clear();
     mUsersKeyList.clear();
 }

 private void logout() {
     showProgressBarForUsers();
     setUserOffline();
     mAuth.signOut();
 }

 private void setUserOffline() {
     if (mAuth.getCurrentUser() != null) {
         String userId = mAuth.getCurrentUser().getUid();
         mUserRefDatabase.child(userId).child("connection").setValue(UsersChatAdapter.OFFLINE);
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
         logout();
         return true;
     }

     return super.onOptionsItemSelected(item);
 }

 private void showProgressBarForUsers() {
     mProgressBarForUsers.setVisibility(View.VISIBLE);
 }

 private void hideProgressBarForUsers() {
     if (mProgressBarForUsers.getVisibility() == View.VISIBLE) {
         mProgressBarForUsers.setVisibility(View.GONE);
     }
 }

 private ChildEventListener getChildEventListener() {
     return new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {

             if (dataSnapshot.exists()) {

                 String userUid = dataSnapshot.getKey();

                 if (dataSnapshot.getKey().equals(mCurrentUserUid)) {
                  //   User currentUser = dataSnapshot.getValue(User.class);
                   //  mUsersChatAdapter.setCurrentUserInfo(userUid, currentUser.getEmail(), currentUser.getCreatedAt());
                 } else {
                     User recipient = dataSnapshot.getValue(User.class);
                     recipient.setRecipientId(userUid);
                     mUsersKeyList.add(userUid);
                     mUsersChatAdapter.refill(recipient);
                 }
             }

         }

      //   @Override
         public void onChildChanged(DataSnapshot dataSnapshot, String s) {
             if (dataSnapshot.exists()) {
                 String userUid = dataSnapshot.getKey();
                 if (!userUid.equals(mCurrentUserUid)) {

                     User user = dataSnapshot.getValue(User.class);

                     int index = mUsersKeyList.indexOf(userUid);
                     if (index > -1) {
                         mUsersChatAdapter.changeUser(index, user);
                     }
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
     };
 }*/





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();

            Intent REDIRECT=new Intent(MainActivity.this,LogInActivity.class);
            finish();
            startActivity(REDIRECT);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
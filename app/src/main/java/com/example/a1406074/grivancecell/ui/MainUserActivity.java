package com.example.a1406074.grivancecell.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a1406074.grivancecell.R;
import com.example.a1406074.grivancecell.adapter.RecyclerViewAdapter;
import com.example.a1406074.grivancecell.login.LogInActivity;
import com.example.a1406074.grivancecell.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainUserActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main_user);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){

                    Intent Login = new Intent(com.example.a1406074.grivancecell.ui.MainUserActivity.this,LogInActivity.class);
                    finish();
                    startActivity(Login);

                }
                else
                {




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




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }



            }

            public boolean onOptionsItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_logout) {

                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                    databaseReference.child("connection").setValue("offline").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mAuth.signOut();


                            Intent REDIRECT=new Intent(com.example.a1406074.grivancecell.ui.MainUserActivity.this,LogInActivity.class);
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



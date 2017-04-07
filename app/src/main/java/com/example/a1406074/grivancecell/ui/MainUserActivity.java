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
import com.example.a1406074.grivancecell.login.LoginUserActivity;
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

    private ArrayList<User> Users = new ArrayList<User>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private ChildEventListener mChildEventListener;
    private RecyclerViewAdapter mUsersChatAdapter;
    public static String ReceipID;
    public int TO_REMOVE;
    public int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent Login = new Intent(com.example.a1406074.grivancecell.ui.MainUserActivity.this, LoginUserActivity.class);
            finish();
            startActivity(Login);

        } else {
            Intent Chat = new Intent(com.example.a1406074.grivancecell.ui.MainUserActivity.this, Chat2Activity.class);
            finish();
            startActivity(Chat);

        }
    }

}






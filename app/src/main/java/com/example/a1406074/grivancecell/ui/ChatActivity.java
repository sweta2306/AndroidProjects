package com.example.a1406074.grivancecell.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a1406074.grivancecell.adapter.RecyclerViewAdapters;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.a1406074.grivancecell.FireChatHelper.ExtraIntent;
import com.example.a1406074.grivancecell.R;
import com.example.a1406074.grivancecell.model.ChatMessage;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    @BindView(R.id.recycler_view_chat) RecyclerView mChatRecyclerView;
   // @BindView(R.id.edit_text_message) EditText mUserMessageChatText;

    private int Count=0;
    private String mRecipientId;
    private String mCurrentUserId;
    private DatabaseReference messageChatDatabase;
    private ChildEventListener messageChatListener;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText mUserMessageChatText;
    private Button SEND_MESSAGE;
    private RecyclerViewAdapters mUsersChatAdapter;
    private ArrayList<ChatMessage> Chats;
    ArrayList<ChatMessage> NEWCHATS=new ArrayList<>();
    public  String FLAG ="First";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras= getIntent().getExtras();

        mRecipientId=MainActivity.ReceipID;



        databaseReference=FirebaseDatabase.getInstance().getReference().child("Chats");

        mChatRecyclerView=(RecyclerView) findViewById(R.id.recycler_view_chat);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SEND_MESSAGE=(Button) findViewById(R.id.btn_send_message);

        mAuth=FirebaseAuth.getInstance();
        mUserMessageChatText=(EditText) findViewById(R.id.edit_text_message);

        messageChatDatabase = FirebaseDatabase.getInstance().getReference().child("Chats").child(mRecipientId);

        messageChatDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.v("Datasnapshot",dataSnapshot.toString()+"Hello");


                Chats=new ArrayList<ChatMessage>();
                Chats.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {

                    messageChatDatabase.child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotd) {



                            ChatMessage CHAT=dataSnapshotd.getValue(ChatMessage.class);

                            Log.v("Messages",CHAT.Message);

                            Chats.add(CHAT);
                            Count++;
                            Log.v("COunt",Count+"");

                            if(FLAG.equals("First"))
                            {

                                mUsersChatAdapter = new RecyclerViewAdapters(Chats,ChatActivity.this,mAuth);
                                mChatRecyclerView.setAdapter(mUsersChatAdapter);

                            }
                            else
                            {
                                ArrayList<ChatMessage> NES=new ArrayList<ChatMessage>();
                                int Size =Chats.size();
                                Log.v("Size",Size+"");
                                for(int i=0;i<Size/2;i++)
                                {
                                    ChatMessage C = Chats.get(i);
                                    NES.add(C);

                                }
                                mUsersChatAdapter = new RecyclerViewAdapters(NES,ChatActivity.this,mAuth);
                                mChatRecyclerView.setAdapter(mUsersChatAdapter);

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SEND_MESSAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NEWCHATS=new ArrayList<ChatMessage>();
                for(ChatMessage chats:Chats)
                {
                    NEWCHATS.add(chats);
                }

                if(!TextUtils.isEmpty(mUserMessageChatText.getText().toString().trim()))
                {
                   // Chats.clear();
                    FLAG="Second";
                    final  DatabaseReference ds=  databaseReference.child(mAuth.getCurrentUser().getUid()).child((System.currentTimeMillis()-10000)+"");

                    ds.child("Message").setValue(mUserMessageChatText.getText().toString().trim());
                    ds.child("Uid").setValue(mAuth.getCurrentUser().getUid());

                }

            }
        });

       /* setUsersId();
        setChatRecyclerView();*/
       // setTheme(android.R.style.Theme_Holo);
    }

    private void bindButterKnife() {
        ButterKnife.bind(this);
    }
    private void setDatabaseInstance() {


        //.child(chatRef);
    }

    private void setUsersId() {
        mRecipientId = getIntent().getStringExtra(ExtraIntent.EXTRA_RECIPIENT_ID);
        mCurrentUserId = getIntent().getStringExtra(ExtraIntent.EXTRA_CURRENT_USER_ID);
    }

    private void setChatRecyclerView() {
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setHasFixedSize(true);

    }
}
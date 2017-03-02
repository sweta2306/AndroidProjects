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


    private String mRecipientId;
    private String mCurrentUserId;
    private DatabaseReference messageChatDatabase;
    private ChildEventListener messageChatListener;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText mUserMessageChatText;
    private Button SEND_MESSAGE;
    private RecyclerViewAdapters mUsersChatAdapter;
    private ArrayList<ChatMessage> Chats=new ArrayList<ChatMessage>();


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

        messageChatDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.v("Datasnapshot",dataSnapshot.toString()+"Hello");


                for (DataSnapshot ds: dataSnapshot.getChildren())
                {

                    messageChatDatabase.child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotd) {



                            ChatMessage CHAT=dataSnapshotd.getValue(ChatMessage.class);

                            Log.v("Messages",CHAT.Message);

                            Chats.add(CHAT);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                mUsersChatAdapter = new RecyclerViewAdapters(Chats,ChatActivity.this,mAuth);
                mChatRecyclerView.setAdapter(mUsersChatAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SEND_MESSAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(mUserMessageChatText.getText().toString().trim()))
                {
                   // Chats.clear();
                    DatabaseReference ds=  databaseReference.child(mAuth.getCurrentUser().getUid()).child((System.currentTimeMillis()-10000)+"");

                    ds.child("Message").setValue(mUserMessageChatText.getText().toString().trim());
                    ds.child("Uid").setValue(mAuth.getCurrentUser().getUid());


                   messageChatDatabase.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {


                           for (DataSnapshot ds: dataSnapshot.getChildren())
                           {

                               messageChatDatabase.child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshotd) {



                                       ChatMessage CHAT=dataSnapshotd.getValue(ChatMessage.class);

                                       Log.v("Messages",CHAT.Message);

                                       Chats.add(CHAT);


                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {

                                   }
                               });


                           }
                           int i= Chats.size();
                           ChatMessage cM =Chats.get(i-1);

                           ArrayList<ChatMessage> c=new ArrayList<ChatMessage>();

                           c.add(cM);

                           mUsersChatAdapter = new RecyclerViewAdapters(c,ChatActivity.this,mAuth);
                           mChatRecyclerView.setAdapter(mUsersChatAdapter);

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
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

  /*  @Override
    protected void onStart() {
        super.onStart();

        messageChatListener = messageChatDatabase.limitToFirst(20).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {

                if(dataSnapshot.exists()){
                    ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class);
                    if(newMessage.getSender().equals(mCurrentUserId)){
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.SENDER);
                    }else{
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.RECIPIENT);
                    }
                    messageChatAdapter.refillAdapter(newMessage);
                    mChatRecyclerView.scrollToPosition(messageChatAdapter.getItemCount()-1);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    }


    @Override
    protected void onStop() {
        super.onStop();

        if(messageChatListener != null) {
            messageChatDatabase.removeEventListener(messageChatListener);
        }
        messageChatAdapter.cleanUp();

    }

    @OnClick(R.id.btn_send_message)
    public void btnSendMsgListener(View sendButton){

        String senderMessage = mUserMessageChatText.getText().toString().trim();

        if(!senderMessage.isEmpty()){

            ChatMessage newMessage = new ChatMessage(senderMessage,mCurrentUserId,mRecipientId);
            messageChatDatabase.push().setValue(newMessage);

            mUserMessageChatText.setText("");
        }
    } */


}
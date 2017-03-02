package com.example.a1406074.grivancecell.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a1406074.grivancecell.FireChatHelper.ChatHelper;
import com.example.a1406074.grivancecell.FireChatHelper.ExtraIntent;
import com.example.a1406074.grivancecell.R;
import com.example.a1406074.grivancecell.model.User;
import com.example.a1406074.grivancecell.ui.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by 1406074 on 16-02-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holder> {

    private ArrayList<User> ListData;
    private LayoutInflater inflater;
    private Context Main;
    private FirebaseAuth mAuth;
    public static String Sender=null;


    public RecyclerViewAdapter(ArrayList<User> listData, Context c,FirebaseAuth user) {
        this.ListData = listData;
        this.inflater=LayoutInflater.from(c);
        Main=c;
        mAuth=user;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.user_profile,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        User list_item=ListData.get(position);

          //  Integer.getInteger(list_item.avatarId);
          int userAvatarId= ChatHelper.getDrawableAvatarId(
                  java.lang.Integer.parseInt(list_item.avatarId));
         Drawable avatarDrawable = ContextCompat.getDrawable(Main,userAvatarId);
          holder.Profile_pic.setImageDrawable(avatarDrawable);


        holder.Name.setText(list_item.displayName);
        holder.Connection.setText(list_item.connection);
      //  holder.Profile_pic.setImageDrawable(ChatHelper.getDrawableAvatarId(fireChatUser.getAvatarId())R.drawable.headshot_7);



    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {
        private TextView Name;
        private TextView Connection;
        private ImageView Profile_pic;
        private RelativeLayout Container;


        public Holder(View itemView) {
            super(itemView);



            Profile_pic=(ImageView) itemView.findViewById(R.id.img_avatar);
            Name=(TextView) itemView.findViewById(R.id.text_view_display_name) ;
            Connection=(TextView) itemView.findViewById(R.id.text_view_connection_status);
            Container=(RelativeLayout) itemView.findViewById(R.id.CONTAINER);

            Container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.v("ISCLICKED","CLICKED");
                    User user = ListData.get(getLayoutPosition());

                    //  String chatRef = user.createUniqueChatRef(mCurrentUserCreatedAt,mCurrentUserEmail);

                    Log.v("users",user.uid+"   EMAILA");

                    Intent chatIntent = new Intent(Main, ChatActivity.class);
                    chatIntent.putExtra(ExtraIntent.EXTRA_CURRENT_USER_ID,mAuth.getCurrentUser().getUid() );
                    chatIntent.putExtra(ExtraIntent.EXTRA_RECIPIENT_ID, user.uid);


                    // Start new activity
                    Main.startActivity(chatIntent);

                }
            });



        }



    }


}
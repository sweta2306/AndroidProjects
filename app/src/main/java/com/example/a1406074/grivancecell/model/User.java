package com.example.a1406074.grivancecell.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author Marcelino Yax-marce7j@gmail.com-Android Developer
 *         Created on 12/23/2016.
 */

public class User {

    public String displayName;
    public String email;
    public String connection;
    public String avatarId;
    public String createdAt;
    public String uid;

    public User() {
    }

    public User(String displayName, String email, String connection, String avatarId, String createdAt, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.connection = connection;
        this.avatarId = avatarId;
        this.createdAt = createdAt;
        this.uid = uid;
    }
    /*    public String createUniqueChatRef(long createdAtCurrentUser, String currentUserEmail){
        String uniqueChatRef="";
        if(createdAtCurrentUser > getCreatedAt()){
            uniqueChatRef = cleanEmailAddress(currentUserEmail)+"-"+cleanEmailAddress(getUserEmail());
        }else {

            uniqueChatRef=cleanEmailAddress(getUserEmail())+"-"+cleanEmailAddress(currentUserEmail);
        }
        return uniqueChatRef;
    }

   public long getCreatedAt() {
        return createdAt;
    }

    private String cleanEmailAddress(String email){
        //replace dot with comma since firebase does not allow dot
        return email.replace(".","-");
    }

    private String getUserEmail() {
        //Log.e("user email  ", userEmail);
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getConnection() {
        return connection;
    }

    public int getAvatarId() {
        return avatarId;
    }

    @Exclude
    public String getRecipientId() {
        return mRecipientId;
    }

    public void setRecipientId(String recipientId) {
        this.mRecipientId = recipientId;
    } */
}
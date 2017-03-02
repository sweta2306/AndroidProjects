package com.example.a1406074.grivancecell.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Marcel on 11/7/2015.
 */
public class ChatMessage {

    public String Message;
    public String Uid;

    public ChatMessage() {
    }

    public ChatMessage(String message, String uid) {
        Message = message;
        Uid = uid;
    }
}
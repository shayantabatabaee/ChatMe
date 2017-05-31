package com.gravity.chatme.app.ui.chat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public class ChatPresenter implements ChatContract.Presenter {

    private DatabaseReference mDatabaseReference;
    private ChatContract.View view;
    private ArrayList<Message> messageList = new ArrayList<>();

    public ChatPresenter(ChatActivity chatActivity) {
        this.view = chatActivity;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void sendMessage(String messageContent) {
        if (!messageContent.equals("")) {
            Message message = new Message();
            message.setMessageContent(messageContent);
            message.setMessageUser("default");
            mDatabaseReference.child("messages").push().setValue(message);
        }
    }

    @Override
    public void retrieveMessage() {
        mDatabaseReference.child("messages").orderByChild("messageTime").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                view.displayMessages(messageList);

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
}

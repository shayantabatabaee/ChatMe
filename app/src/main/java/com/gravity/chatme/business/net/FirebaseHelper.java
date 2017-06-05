package com.gravity.chatme.business.net;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.model.User;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    //Database Reference Object
    private DatabaseReference mDatabaseReference;
    //Instance Object
    private static FirebaseHelper sInstance;

    public static FirebaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseHelper();
        }
        return sInstance;
    }

    private FirebaseHelper() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }


    public void sendMessage(Message message, DatabaseReference.CompletionListener completionListener) {
        mDatabaseReference.child("messages").push().setValue(message, completionListener);
    }

    public void addUser(String username, String token) {
        User user = new User(token);
        mDatabaseReference.child("users").child(username).setValue(user);
    }

    public void updateUserToken(String username, String token) {
        User user = new User(token);
        Map<String, Object> userValue = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + username, userValue);
        mDatabaseReference.updateChildren(childUpdates);
    }

    public void removeUser(String username) {
        mDatabaseReference.child("users").child(username).removeValue();
    }

    public void retrieveMessage(final FirebaseHelperListener listener, Long messageTime) {
        mDatabaseReference.child("messages").orderByChild("messageTime").startAt(messageTime + 1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                listener.onMessageRecieved(message);
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

        /*mDatabaseReference.child("messages").orderByChild("messageTime").startAt(messageTime + 1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    listener.onMessageRecieved(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }


    public interface FirebaseHelperListener {

        void onMessageRecieved(Message message);

        void onFailure(String error);

    }

}

package com.gravity.chatme.business.net;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    //Database Reference Object
    private DatabaseReference mDatabaseReference;
    //Instance Object
    private static FirebaseHelper sInstance;
    //Primary Key
    private String primaryKey;

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
        primaryKey = mDatabaseReference.child("messages").push().getKey();
        message.setUid(primaryKey);
        mDatabaseReference.child("messages").child(primaryKey).setValue(message, completionListener);
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

    public void addUser(String username, String email, String userImgUrl, String token) {
        User user = new User(username, email, token, userImgUrl);
        mDatabaseReference.child("users").child(username).setValue(user);
    }

    public void updateUserToken(String username, String token) {
       /* User user = new User(token, userImgUrl);
        Map<String, Object> userValue = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + username, userValue);
        mDatabaseReference.updateChildren(childUpdates);*/
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("userToken", token);
        mDatabaseReference.child("users").child(username).updateChildren(childUpdates);
    }

    public void updateStatus(String username, boolean online, long lastSeen) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("online", online);
        childUpdates.put("lastSeen", lastSeen);
        mDatabaseReference.child("users").child(username).updateChildren(childUpdates);
    }

    public void retrieveUsers(final FirebaseHelperListener.User listener) {
        mDatabaseReference.child("users").orderByChild("lastSeen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.add(user);
                }
                listener.onUserRetrieved(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeUser(String username) {
        mDatabaseReference.child("users").child(username).removeValue();
    }

    public void getUserNumber(final FirebaseHelperListener.UserNumber listener) {
        mDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onUserNumberRetrieved(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public interface FirebaseHelperListener {

        void onMessageRecieved(Message message);

        void onFailure(String error);

        interface User {
            void onUserRetrieved(ArrayList<com.gravity.chatme.business.model.User> users);
        }

        interface UserNumber {
            void onUserNumberRetrieved(long number);
        }

    }

}

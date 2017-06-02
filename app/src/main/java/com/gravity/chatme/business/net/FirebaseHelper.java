package com.gravity.chatme.business.net;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gravity.chatme.business.model.Message;

public class FirebaseHelper {

    private DatabaseReference mDatabaseReference;

    public FirebaseHelper() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
        FirebaseDatabase.getInstance().goOnline();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //mDatabaseReference.keepSynced(false);

    }

    public void sendMessage(Message message, DatabaseReference.CompletionListener completionListener) {
        mDatabaseReference.child("messages").push().setValue(message, completionListener);
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

        /*mDatabaseReference.child("messages").orderByChild("messageTime").startAt(messageTime+1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                listener.onMessageRecieved(messageList);
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

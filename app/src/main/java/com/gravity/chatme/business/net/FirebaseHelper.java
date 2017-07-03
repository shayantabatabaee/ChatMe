package com.gravity.chatme.business.net;

import android.util.Log;

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

    //Message Section
    public void saveMessage(final Message message, final FirebaseHelperListener.messageDao listener) {
        primaryKey = mDatabaseReference.child("messages").push().getKey();
        message.setUid(primaryKey);
        mDatabaseReference.child("messages").child(primaryKey).setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    message.setMessageSent(true);
                    listener.onComplete(message);
                } else {
                    message.setTimeout(true);
                    listener.onTimeout(message);
                }
            }
        });
    }

    public void purgeWrites() {
        FirebaseDatabase.getInstance().goOffline();
        FirebaseDatabase.getInstance().purgeOutstandingWrites();
    }

    public void goOnline() {
        FirebaseDatabase.getInstance().goOnline();
    }

    public void fetchLowerMessages(final FirebaseHelperListener.Message listener, Long lastMessageTime) {
        mDatabaseReference.child("messages").orderByChild("messageTime").startAt(lastMessageTime + 1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    listener.onSingleMessageRecieved(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchChatMessages(final FirebaseHelperListener.Message listener, long lastMessageTime) {
        mDatabaseReference.child("messages").orderByChild("messageTime").startAt(lastMessageTime + 1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                listener.onSingleMessageRecieved(message);
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

    public void fetchUpperMessages(final FirebaseHelperListener.Message listener, long firstMessageTime) {
        mDatabaseReference.child("messages").orderByChild("messageTime").endAt(firstMessageTime - 1).limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Message> messages = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                listener.onListMessageRecieved(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //User Section
    public void addUser(User user) {
        mDatabaseReference.child("users").child(user.getUsername()).setValue(user);
    }

    public void updateUserToken(String username, String token) {
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

    public void removeUser(String username) {
        mDatabaseReference.child("users").child(username).removeValue();
    }

    public void retrieveUsersStatus(final FirebaseHelperListener.Status listener) {
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

    public void retrieveUserNumbers(final FirebaseHelperListener.Number listener) {
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

    public void getUserByUsername(String username, final FirebaseHelperListener.User listener) {
        mDatabaseReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                listener.onGetUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getIsTyping(final FirebaseHelperListener.User listener) {
        mDatabaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                listener.onGetUser(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                listener.onGetUser(user);
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

    public void setIsTyping(String username, boolean typing) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("typing", typing);
        mDatabaseReference.child("users").child(username).updateChildren(childUpdates);
    }

    public void isConnecting(final FirebaseHelperListener.connecting listener) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(boolean.class);
                if (connected) {
                    Log.d("-conn", "connected");
                    listener.onConnect();
                } else {
                    Log.d("-conn", "disconnected");
                    listener.onDisconnect();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface FirebaseHelperListener {

        interface Message {
            void onListMessageRecieved(ArrayList<com.gravity.chatme.business.model.Message> messages);

            void onSingleMessageRecieved(com.gravity.chatme.business.model.Message message);

            void onFailure(String error);
        }

        interface Number {

            void onUserNumberRetrieved(long number);
        }

        interface Status {
            void onUserRetrieved(ArrayList<com.gravity.chatme.business.model.User> users);
        }

        interface User {
            void onGetUser(com.gravity.chatme.business.model.User user);
        }

        interface messageDao {
            void onComplete(com.gravity.chatme.business.model.Message message);

            void onTimeout(com.gravity.chatme.business.model.Message message);
        }

        interface connecting {
            void onConnect();

            void onDisconnect();
        }

    }

}

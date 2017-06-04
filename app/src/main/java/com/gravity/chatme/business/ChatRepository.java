package com.gravity.chatme.business;

import android.content.Context;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.net.AuthHelper;
import com.gravity.chatme.business.net.FirebaseHelper;
import com.gravity.chatme.business.storage.database.ChatMeDatabase;
import com.gravity.chatme.business.storage.database.MessageDao;

import java.util.ArrayList;
import java.util.Date;

public class ChatRepository {

    private FirebaseHelper firebaseHelper;
    //Database Access Object
    private MessageDao messageDao;
    //Last Message Time
    private long lastMessageTime;
    //Authentication helper
    private AuthHelper mAuthHelper;

    public ChatRepository(Context context, AuthHelper authHelper) {
        firebaseHelper = FirebaseHelper.getInstance();
        this.mAuthHelper = authHelper;
        this.messageDao = ChatMeDatabase.getDatabase(context).messageDao();
    }

    public void addMessage(String messageContent) {
        final Message message = new Message();
        message.setMessageUser(mAuthHelper.getCurrentUser().getDisplayName());
        message.setMessageTime(new Date().getTime());
        message.setMessageContent(messageContent);
        if (!message.getMessageContent().equals("")) {
            firebaseHelper.sendMessage(message, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    //messageDao.insertMessage(message);
                }
            });
        }
    }


    public void retrieveDBMessage(ChatRepositoryListener.DbListener listener) {
        ArrayList<Message> dbMessageList = new ArrayList<>();
        dbMessageList.addAll(messageDao.getAllMessages());
        if (!dbMessageList.isEmpty()) {
            lastMessageTime = dbMessageList.get(dbMessageList.size() - 1).getMessageTime();
            listener.onRetrieveDBMessage(dbMessageList);
        }

    }

    public void fetchRemoteMessage(final ChatRepositoryListener.FirebaseListener listener) {
        firebaseHelper.retrieveMessage(new FirebaseHelper.FirebaseHelperListener() {

            @Override
            public void onMessageRecieved(Message message) {
                listener.OnRetrieveFirebaseMessage(message);
                /*if (!message.getMessageUser().equals(mAuthHelper.getCurrentUser().getDisplayName())) {

                }*/
                messageDao.insertMessage(message);
            }


            @Override
            public void onFailure(String error) {
                listener.onFailure(error);
            }
        }, lastMessageTime);
    }

    public interface ChatRepositoryListener {

        interface DbListener {
            void onRetrieveDBMessage(ArrayList<Message> messages);
        }

        interface FirebaseListener {
            void OnRetrieveFirebaseMessage(Message message);

            void onFailure(String message);
        }
    }
}

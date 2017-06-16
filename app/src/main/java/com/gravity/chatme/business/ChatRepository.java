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
import java.util.Collections;
import java.util.Date;

public class ChatRepository {

    private FirebaseHelper firebaseHelper;
    //Database Access Object
    private MessageDao messageDao;
    //Authentication helper
    private AuthHelper mAuthHelper;
    //Message Time
    private long messageTime;

    public ChatRepository(Context context, AuthHelper authHelper) {
        firebaseHelper = FirebaseHelper.getInstance();
        this.mAuthHelper = authHelper;
        this.messageDao = ChatMeDatabase.getDatabase(context).messageDao();
    }

    public void getMessages(final ChatRepositoryListener listener) {

        ArrayList<Message> dbMessageList = new ArrayList<>();
        dbMessageList.addAll(messageDao.getAllMessages());
        if (!dbMessageList.isEmpty()) {
            Collections.reverse(dbMessageList);
            messageTime = dbMessageList.get(dbMessageList.size() - 1).getMessageTime();
            listener.onGetLowerMessages(dbMessageList);
            firebaseHelper.fetchLowerMessages(new FirebaseHelper.FirebaseHelperListener.Message() {

                @Override
                public void onSingleMessageRecieved(Message message) {

                }

                @Override
                public void onListMessageRecieved(ArrayList<Message> messages) {
                    listener.onGetLowerMessages(messages);
                    messageDao.insertMessage(messages);
                }

                @Override
                public void onFailure(String error) {
                    listener.onFailure(error);
                }
            }, messageTime);
        } else {

            messageTime = new Date().getTime();
            firebaseHelper.fetchUpperMessages(new FirebaseHelper.FirebaseHelperListener.Message() {
                @Override
                public void onListMessageRecieved(ArrayList<Message> messages) {
                    listener.onGetUpperMessages(messages);
                    messageDao.insertMessage(messages);
                }

                @Override
                public void onSingleMessageRecieved(Message message) {

                }

                @Override
                public void onFailure(String error) {

                }
            }, messageTime);
        }
        messageTime = new Date().getTime();
        firebaseHelper.fetchChatMessages(new FirebaseHelper.FirebaseHelperListener.Message() {
            @Override
            public void onListMessageRecieved(ArrayList<Message> messages) {

            }

            @Override
            public void onSingleMessageRecieved(Message message) {
                listener.onGetMessage(message);
                messageDao.insertMessage(message);
            }

            @Override
            public void onFailure(String error) {

            }
        }, messageTime);

    }

    public void retrieveOnScrolledMessages(long firstMessageTime, final ChatRepositoryListener listener) {
        ArrayList<Message> scrolledMessages = new ArrayList<>();
        scrolledMessages.addAll(messageDao.getOnScrolledMessages(firstMessageTime));
        if (!scrolledMessages.isEmpty()) {
            Collections.reverse(scrolledMessages);
            listener.onGetUpperMessages(scrolledMessages);
        } else {
            firebaseHelper.fetchUpperMessages(new FirebaseHelper.FirebaseHelperListener.Message() {
                @Override
                public void onListMessageRecieved(ArrayList<Message> messages) {
                    listener.onGetUpperMessages(messages);

                }

                @Override
                public void onSingleMessageRecieved(Message message) {

                }

                @Override
                public void onFailure(String error) {

                }
            }, firstMessageTime);
        }
    }


    public void sendMessage(String messageContent) {
        final Message message = new Message();
        message.setMessageUser(mAuthHelper.getCurrentUser().getDisplayName());
        message.setMessageTime(new Date().getTime());
        message.setMessageContent(messageContent);
        if (!message.getMessageContent().equals("")) {
            firebaseHelper.saveMessage(message, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    //messageDao.insertMessage(messagein);
                }
            });
        }
    }

    public interface ChatRepositoryListener {

        void onGetUpperMessages(ArrayList<Message> messages);

        void onGetLowerMessages(ArrayList<Message> messages);

        void onGetMessage(Message message);

        void onFailure(String message);

    }
}

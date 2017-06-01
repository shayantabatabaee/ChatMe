package com.gravity.chatme.business;

import android.content.Context;

import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.net.FirebaseHelper;
import com.gravity.chatme.business.storage.database.ChatMeDatabase;
import com.gravity.chatme.business.storage.database.MessageDao;

import java.util.ArrayList;
import java.util.Date;

public class ChatRepository {

    private ArrayList<Message> finalMessageList;
    private ArrayList<Message> dbMessageList;
    private FirebaseHelper firebaseHelper;
    private Context context;
    private MessageDao messageDao;
    private long lastMessageTime;

    public ChatRepository(Context context) {
        dbMessageList = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();
        this.context = context;
        this.messageDao = ChatMeDatabase.getDatabase(context).messageDao();
    }

    public void addMessage(String messageContent) {
        Message message = new Message();
        message.setMessageUser("default");
        message.setMessageTime(new Date().getTime());
        message.setMessageContent(messageContent);
        if (!message.getMessageContent().equals("")) {
            firebaseHelper.sendMessage(message);
        }
    }

    public void retrieveMessage(final ChatRepositoryListener listener) {
        retrieveDBMessage(listener);
        fetchFirebaseMessage(listener);
    }


    private void retrieveDBMessage(ChatRepositoryListener listener) {
        dbMessageList.addAll(messageDao.getAllMessages());
        if (!dbMessageList.isEmpty()) {
            lastMessageTime = dbMessageList.get(dbMessageList.size() - 1).getMessageTime();
            listener.onRetrieveDBMessage(dbMessageList);
        }

    }

    private void fetchFirebaseMessage(final ChatRepositoryListener listener) {
        firebaseHelper.retrieveMessage(new FirebaseHelper.FirebaseHelperListener() {

            @Override
            public void onMessageRecieved(Message message) {
                listener.OnRetrieveFirebaseMessage(message);
                messageDao.insertMessage(message);
            }


            @Override
            public void onFailure(String error) {


            }
        }, lastMessageTime);
    }

    public interface ChatRepositoryListener {

        void onRetrieveDBMessage(ArrayList<Message> messages);

        void OnRetrieveFirebaseMessage(Message message);

        void onFailure(String message);
    }
}

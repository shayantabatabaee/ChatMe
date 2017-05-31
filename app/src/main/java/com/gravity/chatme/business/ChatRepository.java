package com.gravity.chatme.business;

import android.content.Context;

import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.net.FirebaseHelper;

import java.util.ArrayList;
import java.util.Date;

public class ChatRepository {

    private ArrayList<Message> firebaseMessageList;
    private FirebaseHelper firebaseHelper;
    private Context context;

    public ChatRepository(Context context) {
        firebaseMessageList = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();
        this.context = context;
    }

    public void sendMessage(String messageContent) {
        Message message = new Message();
        message.setMessageUser("default");
        message.setMessageTime(new Date().getTime());
        message.setMessageContent(messageContent);
        if (!message.getMessageContent().equals("")) {
            firebaseHelper.sendMessage(message);
        }
       /* ChatMeDatabase db = Room.databaseBuilder(context, ChatMeDatabase.class, "chatme").build();
        db.messageDao().insertMessage(message);*/
    }

    public void retrieveMessage(final ChatRepositoryListener listener) {
        firebaseHelper.retrieveMessage(new FirebaseHelper.FirebaseHelperListener() {

            @Override
            public void onPassMessage(ArrayList<Message> messages) {
                listener.onRetrieveMessages(messages);
            }


            @Override
            public void onFailure(String error) {


            }
        }, 0l);

    }


    public interface ChatRepositoryListener {

        void onRetrieveMessages(ArrayList<Message> messages);

        void onFailure(String message);
    }
}

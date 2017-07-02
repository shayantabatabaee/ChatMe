package com.gravity.chatme.business;

import android.content.Context;
import android.util.Log;

import com.gravity.chatme.business.model.Message;
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
    //Message Time
    private long messageTime;
    //User Repository
    private UserRepository userRepository;
    //Message ArrayList
    private ArrayList<Message> messageList;
    //Timeout
    private static final long timeout = 7000;


    public ChatRepository(Context context) {
        firebaseHelper = FirebaseHelper.getInstance();
        this.messageDao = ChatMeDatabase.getDatabase(context).messageDao();
        this.userRepository = UserRepository.getInstance();
        this.messageList = new ArrayList<>();
    }

    public void getMessages(final ChatRepositoryListener listener) {

        if (!messageList.isEmpty()) {
            listener.onGetLowerMessages(messageList);
            return;
        }
        ArrayList<Message> dbMessageList = new ArrayList<>();
        dbMessageList.addAll(messageDao.getAllMessages());
        if (!dbMessageList.isEmpty()) {
            Collections.reverse(dbMessageList);
            messageTime = dbMessageList.get(dbMessageList.size() - 1).getMessageTime();
            messageList.addAll(dbMessageList);
            listener.onGetLowerMessages(dbMessageList);
            firebaseHelper.fetchLowerMessages(new FirebaseHelper.FirebaseHelperListener.Message() {

                @Override
                public void onSingleMessageRecieved(Message message) {
                    if (!message.getMessageUser().equals(userRepository.getCurrentUser().getUsername())
                            && !messageList.contains(message)) {
                        messageDao.insertMessage(message);
                        listener.onGetMessage(message);
                        messageList.add(message);
                    }
                }

                @Override
                public void onListMessageRecieved(ArrayList<Message> messages) {
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
                    if (!messages.isEmpty()) {
                        listener.onGetUpperMessages(messages);
                        messageList.addAll(0, messages);
                        messageDao.insertMessage(messages);
                    }
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
                messageList.add(message);
                if (!message.getMessageUser().equals(userRepository.getCurrentUser().getUsername())) {
                    messageDao.insertMessage(message);
                }
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
            messageList.addAll(0, scrolledMessages);
        } else {
            firebaseHelper.fetchUpperMessages(new FirebaseHelper.FirebaseHelperListener.Message() {
                @Override
                public void onListMessageRecieved(ArrayList<Message> messages) {
                    if (!messages.isEmpty()) {
                        listener.onGetUpperMessages(messages);
                        messageList.addAll(0, messages);
                    }

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

    public void sendMessage(String messageContent, final ChatSentListener listener) {
        final Message tempMessage = new Message();
        tempMessage.setMessageUser(userRepository.getCurrentUser().getUsername());
        tempMessage.setMessageTime(new Date().getTime());
        tempMessage.setMessageContent(messageContent);
        if (!tempMessage.getMessageContent().equals("")) {
            final Thread thread = new Thread() {
                boolean isComplete = false;
                @Override
                public void run() {
                    firebaseHelper.saveMessage(tempMessage, new FirebaseHelper.FirebaseHelperListener.messageDao() {
                        @Override
                        public void onComplete(final Message message) {
                            if (message != null) {
                                isComplete = true;
                                messageDao.insertMessage(message);
                                messageList.get(messageList.indexOf(message)).setMessageSent(true);
                                listener.onSent(message);
                                Log.d("timeout", "setvalue to db");
                            }
                        }
                    });
                    try {
                        sleep(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isComplete) {
                        Log.d("timeout", "do Timeout here");
                        firebaseHelper.purgeWrites();
                    }
                }
            };
            thread.start();
        }
    }

    public ArrayList<Message> getMessageList() {
        return messageList;
    }

    public interface ChatRepositoryListener {

        void onGetUpperMessages(ArrayList<Message> messages);

        void onGetLowerMessages(ArrayList<Message> messages);

        void onGetMessage(Message message);

        void onFailure(String message);

    }

    public interface ChatSentListener {
        void onSent(Message message);
    }
}

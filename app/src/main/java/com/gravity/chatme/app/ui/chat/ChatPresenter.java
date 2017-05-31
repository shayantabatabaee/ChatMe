package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.ChatRepository;
import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View view;
    private ChatRepository chatRepository;

    public ChatPresenter(ChatActivity chatActivity) {
        this.view = chatActivity;
        chatRepository = new ChatRepository(chatActivity.getApplicationContext());
    }

    @Override
    public void sendMessage(String messageContent) {
        chatRepository.sendMessage(messageContent);
    }

    @Override
    public void retrieveMessage() {
        chatRepository.retrieveMessage(new ChatRepository.ChatRepositoryListener() {

            @Override
            public void onRetrieveMessages(ArrayList<Message> messages) {
                view.displayMessages(messages);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}

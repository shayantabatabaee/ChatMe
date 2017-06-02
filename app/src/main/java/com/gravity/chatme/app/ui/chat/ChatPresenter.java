package com.gravity.chatme.app.ui.chat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.gravity.chatme.business.ChatRepository;
import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View view;
    private ChatRepository chatRepository;

    public ChatPresenter(ChatActivity chatActivity, GoogleApiClient.Builder builder) {
        this.view = chatActivity;
        chatRepository = new ChatRepository(chatActivity.getApplicationContext(),builder);
    }

    @Override
    public void sendMessage(String messageContent) {
        chatRepository.addMessage(messageContent);
    }

    @Override
    public void retrieveMessage() {
        chatRepository.retrieveMessage(new ChatRepository.ChatRepositoryListener() {
            @Override
            public void OnRetrieveFirebaseMessage(Message message) {
                view.displayMessage(message);
            }

            public void onRetrieveDBMessage(ArrayList<Message> messages) {
                view.displayMessages(messages);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}

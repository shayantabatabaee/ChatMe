package com.gravity.chatme.app.ui.chat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.gravity.chatme.business.ChatRepository;
import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.net.AuthHelper;

import java.util.ArrayList;

public class ChatPresenter implements ChatContract.Presenter {

    //View Object
    private ChatContract.View view;
    //Repository Object
    private ChatRepository chatRepository;
    //Authentication helper
    private AuthHelper mAuthHelper;

    public ChatPresenter(ChatActivity view) {
        this.view = view;
        GoogleApiClient.Builder mGoogleApiClientBuilder = new GoogleApiClient.Builder(view)
                .enableAutoManage(view, null);
        mAuthHelper = AuthHelper.getInstance(mGoogleApiClientBuilder);
        chatRepository = new ChatRepository(view.getApplicationContext(), mAuthHelper);
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

    @Override
    public void getWelcomeMessage() {
        view.showWelcomeMessage("Welcome Dear " + getCurrentUser());
    }

    @Override
    public String getCurrentUser() {
        return mAuthHelper.getCurrentUser().getDisplayName();
    }
}

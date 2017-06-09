package com.gravity.chatme.app.ui.chat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.gravity.chatme.business.ChatRepository;
import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.net.AuthHelper;
import com.gravity.chatme.business.net.FirebaseHelper;

import java.util.ArrayList;

public class ChatPresenter implements ChatContract.Presenter {

    //View Object
    private ChatContract.View view;
    //Repository Object
    private ChatRepository chatRepository;
    //Authentication helper
    private AuthHelper mAuthHelper;

    public ChatPresenter(ChatActivity view, GoogleApiClient.Builder builder) {
        this.view = view;
        mAuthHelper = AuthHelper.getInstance(builder);
        chatRepository = new ChatRepository(view.getApplicationContext(), mAuthHelper);
    }

    @Override
    public void sendMessage(String messageContent) {
        chatRepository.addMessage(messageContent);
    }

    @Override
    public void retrieveLocalMessage() {
        chatRepository.retrieveDBMessage(new ChatRepository.ChatRepositoryListener.DbListener() {
            @Override
            public void onRetrieveDBMessage(ArrayList<Message> messages) {
                view.displayMessages(messages);
            }
        });
    }

    @Override
    public void fetchRemoteMessage() {
        chatRepository.fetchRemoteMessage(new ChatRepository.ChatRepositoryListener.FirebaseListener() {
            @Override
            public void OnRetrieveFirebaseMessage(Message message) {
                view.displayMessage(message);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void getOnScrolledMessages(long firstMessageTime) {
        chatRepository.retrieveOnScrolledMessages(firstMessageTime, new ChatRepository.ChatRepositoryListener.DbListener() {
            @Override
            public void onRetrieveDBMessage(ArrayList<Message> messages) {
                view.displayMessages(messages);
            }
        });
    }

    @Override
    public void signOut() {
        mAuthHelper.signOut(new AuthHelper.AuthHelperListener() {
            @Override
            public void onSignOut() {
                view.startActivity();
            }

            @Override
            public void onSignIn() {

            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void getNavHeader() {
        view.loadNavHeader(mAuthHelper.getCurrentUser().getDisplayName(),
                mAuthHelper.getCurrentUser().getEmail(),
                mAuthHelper.getCurrentUser().getPhotoUrl().toString());
    }

    @Override
    public void getMemberNumber() {
        FirebaseHelper.getInstance().getUserNumber(new FirebaseHelper.FirebaseHelperListener.UserNumber() {
            @Override
            public void onUserNumberRetrieved(long number) {
                view.displayMemberNumber(number);
            }
        });
    }

    @Override
    public String getCurrentUser() {
        return mAuthHelper.getCurrentUser().getDisplayName();
    }


    @Override
    public void updateStatus(boolean online, long lastSeen) {
        if (mAuthHelper.getCurrentUser() != null) {
            FirebaseHelper.getInstance().updateStatus(mAuthHelper.getCurrentUser().getDisplayName(),
                    online, lastSeen);
        }

    }
}

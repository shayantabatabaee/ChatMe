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
    public void getData() {
        //Load Navigation Header Information
        view.loadNavHeader(mAuthHelper.getCurrentUser().getDisplayName(),
                mAuthHelper.getCurrentUser().getEmail(),
                mAuthHelper.getCurrentUser().getPhotoUrl().toString());
        //Load Members In Group
        FirebaseHelper.getInstance().getUserNumber(new FirebaseHelper.FirebaseHelperListener.UserNumber() {
            @Override
            public void onUserNumberRetrieved(long number) {
                view.displayMemberNumber(number);
            }
        });

        //Load Messages
        chatRepository.getMessages(new ChatRepository.ChatRepositoryListener() {
            @Override
            public void onGetMessages(ArrayList<Message> messages) {
                view.displayData(messages);
            }

            @Override
            public void onGetMessages(Message message) {
                view.displayData(message);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void getOnScrolledData(long firstTime) {
        chatRepository.retrieveOnScrolledMessages(firstTime, new ChatRepository.ChatRepositoryListener() {
            @Override
            public void onGetMessages(ArrayList<Message> messages) {
                view.displayData(messages);
            }

            @Override
            public void onGetMessages(Message message) {

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void sendData(String content) {
        chatRepository.sendMessage(content);
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
    public void updateStatus(boolean online, long lastSeen) {
        if (mAuthHelper.getCurrentUser() != null) {
            FirebaseHelper.getInstance().updateStatus(mAuthHelper.getCurrentUser().getDisplayName(),
                    online, lastSeen);
        }

    }


    @Override
    public String getCurrentUser() {
        return mAuthHelper.getCurrentUser().getDisplayName();
    }
}

package com.gravity.chatme.app.ui.chat;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.gravity.chatme.app.ui.contact.ContactActivity;
import com.gravity.chatme.business.ChatRepository;
import com.gravity.chatme.business.UserRepository;
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
    //UserRepository object
    private UserRepository userRepository;
    //FirebaseHelper object
    private FirebaseHelper firebaseHelper;
    //Instance Object
    private static ChatPresenter sInstance;

    public static ChatPresenter getInstance(ChatActivity view, GoogleApiClient.Builder builder) {
        if (sInstance == null) {
            sInstance = new ChatPresenter(view, builder);
        }
        return sInstance;
    }


    private ChatPresenter(ChatActivity view, GoogleApiClient.Builder builder) {
        this.view = view;
        mAuthHelper = AuthHelper.getInstance(builder);
        userRepository = UserRepository.getInstance();
        userRepository.setAuthHelper(mAuthHelper);
        chatRepository = new ChatRepository(view.getApplicationContext());
        firebaseHelper = FirebaseHelper.getInstance();
    }

    @Override
    public void attachView(ChatActivity view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public ArrayList<Message> getDataList() {
        return chatRepository.getMessageList();
    }

    @Override
    public void getData() {
        if (view != null) {
            //Load Navigation Header Information
            view.loadNavHeader(userRepository.getCurrentUser().getUsername(),
                    userRepository.getCurrentUser().getEmail(),
                    userRepository.getCurrentUser().getUserImgUrl());
        }

        //Load Members In Group
        userRepository.getUserNumbers(new UserRepository.UserRepositoryListener.Number() {
            @Override
            public void onUserNumberRetrieved(long number) {
                if (view != null) {
                    view.displayMemberNumber(number);
                }
            }
        });
        //TODO:NotifyItemInserted and remove upper lower
        //Load Messages
        chatRepository.getMessages(new ChatRepository.ChatRepositoryListener() {
            @Override
            public void onGetUpperMessages(ArrayList<Message> messages) {
                if (view != null) {
                    view.displayData(ChatActivity.UPPER_LEVEL);
                }
            }

            @Override
            public void onGetLowerMessages(ArrayList<Message> messages) {
                if (view != null) {
                    view.displayData(ChatActivity.LOWER_LEVEL);
                }
            }

            @Override
            public void onGetMessage(Message message) {

                if (view != null) {
                    view.displayData(ChatActivity.LOWER_LEVEL);
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
        //Subscribe to setIsTyping Method
        userRepository.getIsTyping(new UserRepository.UserRepositoryListener.Typing() {
            @Override
            public void onTypingUserChanged(ArrayList<String> username) {

                String typingContent = "";
                if (username.size() > 1) {
                    typingContent = username.get(0) +
                            " and others are typing . . .";
                } else if (username.size() == 1) {
                    typingContent = username.get(0) + " is typing . . .";
                } else if (username.size() == 0) {
                    typingContent = "";
                }
                if (view != null) {
                    view.displayTyping(typingContent);
                }
            }
        });

        firebaseHelper.isConnecting(new FirebaseHelper.FirebaseHelperListener.connecting() {
            @Override
            public void onConnect() {
                view.displayConnecting();
            }

            @Override
            public void onDisconnect() {
                view.displayDisconnecting();
                firebaseHelper.goOnline();
            }
        });

    }

    @Override
    public void getScrolledData(long firstTime) {

        chatRepository.retrieveOnScrolledMessages(firstTime, new ChatRepository.ChatRepositoryListener() {
            @Override
            public void onGetUpperMessages(ArrayList<Message> messages) {
                if (view != null) {
                    view.displayData(ChatActivity.UPPER_LEVEL);
                }
            }

            @Override
            public void onGetLowerMessages(ArrayList<Message> messages) {

            }

            @Override
            public void onGetMessage(Message message) {

            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

    @Override
    public void sendData(String content) {
        chatRepository.sendMessage(content, new ChatRepository.ChatSentListener() {
            @Override
            public void onSent(Message message) {
                if (view != null) {
                    view.displayData(ChatActivity.LOWER_LEVEL);
                }
            }
        });
    }

    @Override
    public void retrySendData(int index) {
        chatRepository.retrySendMessage(index, new ChatRepository.ChatSentListener() {
            @Override
            public void onSent(Message message) {
                if (view != null) {
                    view.displayData(ChatActivity.LOWER_LEVEL);
                }
            }
        });
    }

    @Override
    public void signOut() {
        getDataList().clear();
        mAuthHelper.signOut(new AuthHelper.AuthHelperListener() {
            @Override
            public void onSignOut() {
                if (view != null) {
                    view.startActivity();
                }
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
        userRepository.updateStatus(online, lastSeen);
    }

    @Override
    public void setIsTyping(boolean typing) {
        userRepository.setIsTyping(typing);
    }


}

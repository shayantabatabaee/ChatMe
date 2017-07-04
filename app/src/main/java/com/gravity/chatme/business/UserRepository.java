package com.gravity.chatme.business;


import com.google.firebase.iid.FirebaseInstanceId;
import com.gravity.chatme.app.ChatApplication;
import com.gravity.chatme.business.model.User;
import com.gravity.chatme.business.net.AuthHelper;
import com.gravity.chatme.business.net.FirebaseHelper;
import com.gravity.chatme.business.storage.database.ChatMeDatabase;
import com.gravity.chatme.business.storage.database.UserDao;

import java.util.ArrayList;

public class UserRepository {

    //Firebase Db Helper
    private FirebaseHelper firebaseHelper;
    //Database Access Object
    private UserDao userDao;
    //Instance Object
    private static UserRepository sInstance;
    //IsTyping Lists
    private ArrayList<String> typingList;
    //Auth Helper
    private AuthHelper mAuthHelper;
    //Current User
    private User mCurrentUser;

    private UserRepository() {
        this.userDao = ChatMeDatabase.getDatabase(ChatApplication.getInstance().getApplicationContext()).userDao();
        this.firebaseHelper = FirebaseHelper.getInstance();
        this.typingList = new ArrayList<>();
    }

    public static UserRepository getInstance() {
        if (sInstance == null) {
            sInstance = new UserRepository();
        }
        return sInstance;
    }

    public void setAuthHelper(AuthHelper mAuthHelper) {
        this.mAuthHelper = mAuthHelper;
        mCurrentUser = new User(mAuthHelper.getCurrentUser().getDisplayName(), mAuthHelper.getCurrentUser().getEmail(),
                FirebaseInstanceId.getInstance().getToken(), mAuthHelper.getCurrentUser().getPhotoUrl().toString());
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void getUserNumbers(final UserRepositoryListener.Number listener) {
        firebaseHelper.retrieveUserNumbers(new FirebaseHelper.FirebaseHelperListener.Number() {
            @Override
            public void onUserNumberRetrieved(long number) {
                listener.onUserNumberRetrieved(number);
            }
        });
    }


    public void getUsersStatus(final UserRepositoryListener.Status listener) {
        firebaseHelper.retrieveUsersStatus(new FirebaseHelper.FirebaseHelperListener.Status() {
            @Override
            public void onUserRetrieved(ArrayList<User> users) {
                listener.onUserRetrieved(users);
            }
        });
    }

    public void getUserStatus(String username, FirebaseHelper.FirebaseHelperListener.LastSeen listener) {
        firebaseHelper.retrieveUserStatus(username, listener);
    }


    public void addUser(String username, String email, String userImgUrl, String token) {
        User user = new User(username, email, token, userImgUrl);
        firebaseHelper.addUser(user);
    }

    public void updateUserToken(String token) {
        if (getCurrentUser() != null) {
            firebaseHelper.updateUserToken(getCurrentUser().getUsername(), token);
        }
    }

    public void updateStatus(boolean online, long lastSeen) {
        if (getCurrentUser() != null) {
            firebaseHelper.updateStatus(getCurrentUser().getUsername(), online, lastSeen);
        }
    }

    public void removeUser() {
        if (getCurrentUser() != null) {
            firebaseHelper.removeUser(getCurrentUser().getUsername());
        }
    }

    public void getUser(String username, final FirebaseHelper.FirebaseHelperListener.User listener) {
        User user = userDao.getUser(username);
        if (user != null) {
            listener.onGetUser(user);
        } else {
            firebaseHelper.getUserByUsername(username, new FirebaseHelper.FirebaseHelperListener.User() {
                @Override
                public void onGetUser(User user) {
                    userDao.insertUser(user);
                    listener.onGetUser(user);
                }
            });

        }
    }

    public void setIsTyping(boolean typing) {
        firebaseHelper.setIsTyping(getCurrentUser().getUsername(), typing);
    }

    public void getIsTyping(final UserRepositoryListener.Typing listener) {
        firebaseHelper.getIsTyping(new FirebaseHelper.FirebaseHelperListener.User() {
            @Override
            public void onGetUser(User user) {
                if (user.isTyping() && !typingList.contains(user.getUsername())
                        && !user.getUsername().equals(getCurrentUser().getUsername())) {
                    typingList.add(user.getUsername());
                } else if (!user.isTyping()) {
                    typingList.remove(user.getUsername());

                }
                listener.onTypingUserChanged(typingList);
            }
        });
    }

    public interface UserRepositoryListener {

        interface Number {
            void onUserNumberRetrieved(long number);
        }

        interface Status {
            void onUserRetrieved(ArrayList<User> users);
        }

        interface Typing {
            void onTypingUserChanged(ArrayList<String> username);
        }

    }
}

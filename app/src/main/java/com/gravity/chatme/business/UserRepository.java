package com.gravity.chatme.business;


import com.gravity.chatme.app.ChatApplication;
import com.gravity.chatme.business.model.User;
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
    //Current Username
    private String currentUser;

    public void setCurrentUsername(String currentUser) {
        this.currentUser = currentUser;
    }

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

    public void addUser(String username, String email, String userImgUrl, String token) {
        User user = new User(username, email, token, userImgUrl);
        firebaseHelper.addUser(user);
    }

    public void updateUserToken(String username, String token) {
        firebaseHelper.updateUserToken(username, token);
    }

    public void updateStatus(String username, boolean online, long lastSeen) {
        firebaseHelper.updateStatus(username, online, lastSeen);
    }

    public void removeUser(String username) {
        firebaseHelper.removeUser(username);
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

    public void setIsTyping(String username, boolean typing) {
        firebaseHelper.setIsTyping(username, typing);
    }

    public void getIsTyping(final UserRepositoryListener.Typing listener) {
        firebaseHelper.getIsTyping(new FirebaseHelper.FirebaseHelperListener.User() {
            @Override
            public void onGetUser(User user) {
                if (user.isTyping() && !typingList.contains(user.getUsername())
                        && !user.getUsername().equals(currentUser)) {
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

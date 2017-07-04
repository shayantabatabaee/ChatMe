package com.gravity.chatme.app.ui.contact;

import com.gravity.chatme.business.UserRepository;
import com.gravity.chatme.business.model.User;
import com.gravity.chatme.business.net.FirebaseHelper;

public class ContactPresenter implements ContactContract.presenter {

    //View Object
    private ContactContract.view view;
    //Repository Objects
    private UserRepository userRepository;

    public ContactPresenter() {
        userRepository = UserRepository.getInstance();
    }

    @Override
    public void attachView(ContactActivity view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getData(String username) {
        userRepository.getUser(username, new FirebaseHelper.FirebaseHelperListener.User() {
            @Override
            public void onGetUser(User user) {
                if (view != null) {
                    view.displayData(user);
                }
            }
        });

        userRepository.getUserStatus(username, new FirebaseHelper.FirebaseHelperListener.LastSeen() {
            @Override
            public void onGetUser(User user) {
                if (view != null) {
                    view.displayData(user.getLastSeen(), user.isOnline());
                }
            }
        });
    }
}

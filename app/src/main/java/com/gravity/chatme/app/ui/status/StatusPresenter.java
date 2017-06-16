package com.gravity.chatme.app.ui.status;


import com.gravity.chatme.business.UserRepository;
import com.gravity.chatme.business.model.User;

import java.util.ArrayList;

public class StatusPresenter implements StatusContract.presenter {

    private StatusContract.view view;

    public StatusPresenter(StatusContract.view view) {
        this.view = view;
    }

    @Override
    public void retrieveData() {
        UserRepository.getInstance().getUsersStatus(new UserRepository.UserRepositoryListener.Status() {
            @Override
            public void onUserRetrieved(ArrayList<User> users) {
                view.displayData(users);
            }

        });
    }
}

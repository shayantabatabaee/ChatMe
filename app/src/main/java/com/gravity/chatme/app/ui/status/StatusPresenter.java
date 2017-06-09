package com.gravity.chatme.app.ui.status;


import com.gravity.chatme.business.model.User;
import com.gravity.chatme.business.net.FirebaseHelper;

import java.util.ArrayList;

public class StatusPresenter implements StatusContract.presenter {

    private StatusContract.view view;

    public StatusPresenter(StatusContract.view view) {
        this.view = view;
    }

    @Override
    public void retrieveData() {
        FirebaseHelper.getInstance().retrieveUsers(new FirebaseHelper.FirebaseHelperListener.User() {
            @Override
            public void onUserRetrieved(ArrayList<User> users) {
                view.displayData(users);
            }
        });
    }
}

package com.gravity.chatme.app.ui.signing;


import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gravity.chatme.business.net.AuthHelper;

public class GoogleSignInPresenter implements GoogleSignInContract.presenter, AuthHelper.AuthHelperListener {

    //View Object
    private GoogleSignInContract.view view;
    //Authentication Object
    private AuthHelper mAuthHelper;
    //Request Code
    private static final int RC_SIGN_IN = 9001;
    //Topics for FCM
    private String topic;

    public GoogleSignInPresenter(GoogleSignInActivity view, GoogleApiClient.Builder builder) {
        mAuthHelper = AuthHelper.getInstance(builder);
        this.view = view;
    }

    @Override
    public void signIn() {
        Intent signIntent = mAuthHelper.getSignInIntent();
        view.startActivityForResult(signIntent);
    }


    @Override
    public void signInResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                mAuthHelper.firebaseAuthWithGoogle(account, this);
            } else {
                view.updateUI(false);
            }
        }
    }

    @Override
    public void singOut() {
        mAuthHelper.signOut(this);
    }

    @Override
    public void checkSignedIn() {
        FirebaseUser currentUser = mAuthHelper.getCurrentUser();
        if (currentUser != null) {
            subscribeToTopic();
            view.updateUI(true);
        } else {
            view.updateUI(false);
        }
    }

    private void subscribeToTopic() {
        topic = "user_" + mAuthHelper.getCurrentUser().getDisplayName().replaceAll("\\s+", "");
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    @Override
    public void onSignIn() {
        subscribeToTopic();
        view.updateUI(true);
    }


    @Override
    public void onSignOut() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        view.updateUI(false);
    }

    @Override
    public void onFailed() {

    }
}

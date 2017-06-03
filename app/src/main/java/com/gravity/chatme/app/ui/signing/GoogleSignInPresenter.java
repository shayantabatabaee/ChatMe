package com.gravity.chatme.app.ui.signing;


import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.gravity.chatme.business.net.AuthHelper;

public class GoogleSignInPresenter implements GoogleSignInContract.presenter, AuthHelper.AuthHelperListener {

    //View Object
    private GoogleSignInContract.view view;
    //Authentication Object
    private AuthHelper mAuthHelper;
    //Request Code
    private static final int RC_SIGN_IN = 9001;

    public GoogleSignInPresenter(GoogleSignInActivity view) {

        GoogleApiClient.Builder mGoogleApiClientBuilder = new GoogleApiClient.Builder(view)
                .enableAutoManage(view, null);
        mAuthHelper = AuthHelper.getInstance(mGoogleApiClientBuilder);
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
            view.updateUI(true);
        } else {
            view.updateUI(false);
        }
    }

    @Override
    public void onSignIn() {
        view.updateUI(true);
    }

    @Override
    public void onSignOut() {
        view.updateUI(false);
    }

    @Override
    public void onFailed() {

    }
}

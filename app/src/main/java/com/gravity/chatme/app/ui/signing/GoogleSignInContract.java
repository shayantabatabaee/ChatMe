package com.gravity.chatme.app.ui.signing;


import android.content.Intent;

public interface GoogleSignInContract {

    interface view {

        void updateUI(boolean signedIn);

        void startActivityForResult(Intent intent);
    }

    interface presenter {

        void signIn();

        void singOut();

        void signInResult(int requestCode, int resultCode, Intent data);

        void checkSignedIn();
    }
}

package com.gravity.chatme.app.ui.signing;


import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

public interface GoogleSignInContract {

    interface view {
        void updateUI(FirebaseUser user);

        void startActivityForResult(Intent intent);
    }

    interface presenter {
        void signIn();

        void signInResult(int requestCode, int resultCode, Intent data);

        void checkSignedIn();

        void SingOut();
    }
}

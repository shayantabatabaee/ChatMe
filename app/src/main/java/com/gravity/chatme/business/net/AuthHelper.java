package com.gravity.chatme.business.net;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ui.ChatApplication;

public class AuthHelper {

    private FirebaseAuth mAuth;
    private static AuthHelper sInstance;
    private GoogleApiClient mGoogleApiClient;

    public static AuthHelper getInstance(GoogleApiClient.Builder builder) {
        if (sInstance == null) {
            sInstance = new AuthHelper(builder);
        }

        return sInstance;
    }

    private AuthHelper(GoogleApiClient.Builder builder) {

        Context context = ChatApplication.getInstance().getApplicationContext();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = builder.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

             /*   .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

        mAuth = FirebaseAuth.getInstance();
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, final AuthHelperListener listener) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    listener.onSignIn(user);
                } else {
                    listener.onFailed();
                }
            }
        });
    }


    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public Intent getSignInIntent() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public void signOut(final AuthHelperListener listener) {
        mAuth.signOut();


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                listener.onSignOut();
            }
        });
    }

    public interface AuthHelperListener {

        void onSignIn(FirebaseUser user);

        void onSignOut();

        void onFailed();
    }
}

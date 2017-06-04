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
import com.google.firebase.iid.FirebaseInstanceId;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ChatApplication;

public class AuthHelper {

    //Firebase Authentication Object
    private FirebaseAuth mAuth;
    //AutHelper instance
    private static AuthHelper sInstance;
    //Google Api Client Object
    private GoogleApiClient mGoogleApiClient;

    public static AuthHelper getInstance(GoogleApiClient.Builder builder) {
        return new AuthHelper(builder);
    }

    private AuthHelper(GoogleApiClient.Builder builder) {

        Context context = ChatApplication.getInstance().getApplicationContext();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = builder.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, final AuthHelperListener listener) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSignIn();
                    FirebaseUser user = mAuth.getCurrentUser();
                    FirebaseHelper.getInstance().sendUser(user.getDisplayName()
                            , FirebaseInstanceId.getInstance().getToken());
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
        FirebaseHelper.getInstance().removeUser(mAuth.getCurrentUser().getDisplayName());
        mAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                listener.onSignOut();
            }
        });
    }

    public interface AuthHelperListener {

        void onSignIn();

        void onSignOut();

        void onFailed();
    }
}

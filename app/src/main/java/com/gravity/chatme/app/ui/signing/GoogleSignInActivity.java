package com.gravity.chatme.app.ui.signing;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ui.chat.ChatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoogleSignInActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleSignInContract.view, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInContract.presenter presenter;
    private GoogleApiClient.Builder mGoogleApiClientBuilder;
    private final int DELAY = 1000;

    private Unbinder unbinder;
    @BindView(R.id.signInButton)
    SignInButton signInButton;
    @BindView(R.id.signOutButton)
    Button signOutButton;
    @BindView(R.id.googleSignInLayout)
    LinearLayout googleSignInLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlesignin);

        unbinder = ButterKnife.bind(this);
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        mGoogleApiClientBuilder = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this);

        presenter = new GoogleSignInPresenter(this, mGoogleApiClientBuilder);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.checkSignedIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.signInResult(requestCode, resultCode, data);

    }

    @Override
    public void startActivityForResult(Intent intent) {
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void updateUI(FirebaseUser user) {

        if (user != null) {
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            Snackbar.make(googleSignInLayout, "Welcome Dear " + user.getDisplayName(), Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, DELAY);

        } else {
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.signInButton) {
            presenter.signIn();
        } else if (id == R.id.signOutButton)
            presenter.SingOut();
    }

}

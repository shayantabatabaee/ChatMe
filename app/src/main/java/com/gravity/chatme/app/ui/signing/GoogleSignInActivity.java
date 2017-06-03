package com.gravity.chatme.app.ui.signing;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.SignInButton;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ui.chat.ChatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoogleSignInActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleSignInContract.view {

    //Request Code
    private static final int RC_SIGN_IN = 9001;
    //Presenter Object
    private GoogleSignInContract.presenter presenter;
    //View Binding
    private Unbinder unbinder;
    @BindView(R.id.signInButton)
    SignInButton signInButton;
    @BindView(R.id.googleSignInLayout)
    LinearLayout googleSignInLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlesignin);

        initObject();

        signInButton.setOnClickListener(this);
    }

    private void initObject() {
        unbinder = ButterKnife.bind(this);
        presenter = new GoogleSignInPresenter(this);
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
    public void updateUI(boolean signedIn) {

        if (signedIn) {
            signInButton.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
            finish();

        } else {
            signInButton.setVisibility(View.VISIBLE);
            signInButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        presenter.signIn();
        v.setEnabled(false);
    }

}

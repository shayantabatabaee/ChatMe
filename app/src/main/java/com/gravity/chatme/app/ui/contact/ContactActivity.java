package com.gravity.chatme.app.ui.contact;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ChatApplication;
import com.gravity.chatme.business.model.User;
import com.gravity.chatme.util.CircleTransform;
import com.gravity.chatme.util.DateComparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactActivity extends AppCompatActivity implements ContactContract.view {

    //constant objects
    public static final String ARG_USERNAME = "ARG_USERNAME";
    //data objects
    private String username;
    //Presenter Object
    private ContactContract.presenter presenter;
    //View objects
    Unbinder unbinder;
    @BindView(R.id.contact_image_view)
    ImageView imageView;
    @BindView(R.id.contact_last_seen)
    TextView contactLastSeen;
    @BindView(R.id.contact_username)
    TextView contactUsername;
    @BindView(R.id.contact_email)
    TextView contactEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initObjects();

        presenter.getData(username);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initObjects() {
        presenter = new ContactPresenter();
        presenter.attachView(this);
        unbinder = ButterKnife.bind(this);
        username = getIntent().getExtras().getString(ARG_USERNAME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void displayData(User user) {
        contactUsername.setText(user.getUsername());
        contactEmail.setText(user.getEmail());


        Glide.with(ChatApplication.getInstance().getApplicationContext()).load(user.getUserImgUrl())
                .crossFade()
                .bitmapTransform(new CircleTransform(ChatApplication.getInstance().getApplicationContext()))
                .thumbnail(0.5f)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);


    }

    @Override
    public void displayData(long lastSeen, boolean online) {
        if (online) {
            contactLastSeen.setText("online");
        } else {
            contactLastSeen.setText(DateComparator.compareDate(lastSeen));
        }
    }
}

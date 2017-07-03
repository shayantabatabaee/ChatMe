package com.gravity.chatme.app.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.GoogleApiClient;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ChatApplication;
import com.gravity.chatme.app.ui.signing.GoogleSignInActivity;
import com.gravity.chatme.app.ui.status.StatusActivity;
import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.util.CircleTransform;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChatActivity extends AppCompatActivity implements ChatContract.View, View.OnClickListener {

    //Presenter Objects
    private ChatContract.Presenter presenter;
    //View Binding
    private Unbinder unbinder;
    View navHeader;
    @BindView(R.id.sendButton)
    ImageView sendButton;
    @BindView(R.id.messageSendingContent)
    EditText messageSendingContent;
    @BindView(R.id.messageRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.chatLinearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.membersTitle)
    TextView membersTitle;
    @BindView(R.id.connectivityStatus)
    TextView connectivityStatus;
    @BindView(R.id.isTyping)
    TextView isTyping;
    //View Objects
    private TextView txtUsername;
    private TextView txtEmail;
    private ImageView imgProfile;
    private Toolbar toolbar;

    //RecyclerView Objects
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //Message List Object
    private ArrayList<Message> messageList;
    //RecyclerView Listener Class
    private ScrollListener scrollListener;
    //IsTyping Boolean Object
    private boolean typing;
    //static Variables
    protected static final int LOWER_LEVEL = 0;
    protected static final int UPPER_LEVEL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initObjects();

        setupNavigationView();

        messageSendingContent.setOnClickListener(this);
        messageSendingContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {
                    typing = true;
                    presenter.setIsTyping(typing);
                } else if (s.toString().trim().length() == 0 && typing) {
                    typing = false;
                    presenter.setIsTyping(typing);
                }
            }
        });
        sendButton.setOnClickListener(this);
        membersTitle.setOnClickListener(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new ScrollListener();
        recyclerView.addOnScrollListener(scrollListener);

        presenter.getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ChatApplication.isInBackground = false;
        presenter.updateStatus(true, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.updateStatus(false, new Date().getTime());
        ChatApplication.isInBackground = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbinder.unbind();
        presenter.detachView();
    }

    private void initObjects() {
        unbinder = ButterKnife.bind(this);
        navHeader = navigationView.getHeaderView(0);

        txtUsername = (TextView) navHeader.findViewById(R.id.username);
        txtEmail = (TextView) navHeader.findViewById(R.id.email);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);


        layoutManager = new LinearLayoutManager(this);
        GoogleApiClient.Builder mGoogleApiClientBuilder = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, null);
        //presenter = new ChatPresenter(this, mGoogleApiClientBuilder);
        presenter = ChatPresenter.getInstance(this, mGoogleApiClientBuilder);
        presenter.attachView(this);

        messageList = presenter.getMessageList();
        //adapter = new RecyclerViewAdapter(messageList);
        adapter = new RecyclerViewAdapter(messageList,presenter);
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_sign_out:
                        presenter.signOut();
                        break;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void loadNavHeader(String username, String email, String urlProfileImg) {
        txtUsername.setText(username);
        txtEmail.setText(email);
        //TODO:Read From DB
        Glide.with(getApplicationContext()).load(urlProfileImg)
                .crossFade()
                .bitmapTransform(new CircleTransform(getApplicationContext()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgProfile);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.messageSendingContent) {
            recyclerView.smoothScrollToPosition(adapter.getItemCount());

        } else if (v.getId() == R.id.sendButton) {
            presenter.sendData(messageSendingContent.getText().toString());
            messageSendingContent.setText("");
        } else if (v.getId() == R.id.membersTitle) {
            Intent intent = new Intent(this, StatusActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void startActivity() {
        Intent intent = new Intent(getApplicationContext(), GoogleSignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void displayMemberNumber(long number) {
        membersTitle.setText(number + " Members");
    }

    @Override
    public void displayTyping(String typingContent) {
        isTyping.setText(typingContent);
    }

    @Override
    public void displayDisconnectEvent() {
        membersTitle.setVisibility(View.GONE);
        connectivityStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayConnectEvent() {
        membersTitle.setVisibility(View.VISIBLE);
        connectivityStatus.setVisibility(View.GONE);
    }

    @Override
    public void displayData(int level) {
        switch (level) {
            case UPPER_LEVEL:
                recyclerView.scrollToPosition(12);
                break;
            case LOWER_LEVEL:
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(-1) && (dy < 0)) {
                recyclerView.removeOnScrollListener(this);
                long firstTime = messageList.get(0).getMessageTime();
                presenter.getScrolledData(firstTime);
                recyclerView.addOnScrollListener(scrollListener);
            }
        }
    }
}


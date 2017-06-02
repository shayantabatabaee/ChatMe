package com.gravity.chatme.app.ui.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.gravity.chatme.R;
import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChatActivity extends AppCompatActivity implements ChatContract.View, View.OnClickListener {

    //MVP Objects
    private ChatContract.Presenter presenter;

    //ButterKnife Objects
    private Unbinder unbinder;
    @BindView(R.id.sendButton)
    ImageView sendButton;
    @BindView(R.id.messageSendingContent)
    EditText messageSendingContent;
    @BindView(R.id.messageRecyclerView)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ArrayList<Message> messageList;

    private GoogleApiClient.Builder mGoogleApiClientBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initObjects();
        unbinder = ButterKnife.bind(this);
        sendButton.setOnClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        presenter.retrieveMessage();

    }

    private void initObjects() {
        messageList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        mGoogleApiClientBuilder = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null);
        presenter = new ChatPresenter(this, mGoogleApiClientBuilder);
        adapter = new RecyclerViewAdapter(messageList, presenter.getCurrentUser());
    }

    @Override
    public void onClick(View v) {
        presenter.sendMessage(messageSendingContent.getText().toString());
        messageSendingContent.setText("");

    }


    @Override
    public void displayMessage(Message message) {
        messageList.add(message);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(adapter.getItemCount());

    }

    @Override
    public void displayMessages(ArrayList<Message> messages) {
        messageList.addAll(messages);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
    }
}

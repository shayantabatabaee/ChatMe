package com.gravity.chatme.app.ui.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ArrayList<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initObjects();
        presenter.retrieveMessage();
        unbinder = ButterKnife.bind(this);
        sendButton.setOnClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initObjects() {
        messageList = new ArrayList<>();
        presenter = new ChatPresenter(this);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerViewAdapter(messageList);

    }

    @Override
    public void onClick(View v) {
        presenter.sendMessage(messageSendingContent.getText().toString());
        messageSendingContent.setText("");
    }

    @Override
    public void displayMessages(ArrayList<Message> messageList) {
        this.messageList = messageList;
        adapter.notifyDataSetChanged();
    }

}

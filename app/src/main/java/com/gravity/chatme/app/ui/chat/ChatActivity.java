package com.gravity.chatme.app.ui.chat;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gravity.chatme.R;
import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChatActivity extends AppCompatActivity implements ChatContract.View, View.OnClickListener {

    //Presenter Objects
    private ChatContract.Presenter presenter;
    //View Binding
    private Unbinder unbinder;
    @BindView(R.id.sendButton)
    ImageView sendButton;
    @BindView(R.id.messageSendingContent)
    EditText messageSendingContent;
    @BindView(R.id.messageRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.chatLinearLayout)
    LinearLayout linearLayout;
    //RecyclerView Objects
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //Message List Object
    private ArrayList<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initObjects();

        messageSendingContent.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getWelcomeMessage();
        presenter.retrieveMessage();
    }

    private void initObjects() {
        unbinder = ButterKnife.bind(this);
        messageList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        presenter = new ChatPresenter(this);
        //TODO:Fix presenter.getCurrentUser
        adapter = new RecyclerViewAdapter(messageList, presenter.getCurrentUser());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.messageSendingContent) {
            recyclerView.smoothScrollToPosition(adapter.getItemCount());

        } else if (v.getId() == R.id.sendButton) {
            presenter.sendMessage(messageSendingContent.getText().toString());
            messageSendingContent.setText("");
        }
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

    @Override
    public void showWelcomeMessage(String welcomeMessage) {
        Snackbar.make(linearLayout, welcomeMessage, Snackbar.LENGTH_LONG).show();
    }
}

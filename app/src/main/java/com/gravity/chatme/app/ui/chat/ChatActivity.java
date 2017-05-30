package com.gravity.chatme.app.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gravity.chatme.R;
import com.gravity.chatme.business.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChatActivity extends AppCompatActivity implements ChatContract.View, View.OnClickListener {

    //MVP Objects
    private ChatContract.Presenter presenter;

    //ButterKnife Objects
    Unbinder unbinder;
    @BindView(R.id.sendButton)
    ImageView sendButton;
    @BindView(R.id.messageContent)
    EditText messageContent;
    @BindView(R.id.messageList)
    LinearLayout messageListLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    RelativeLayout layout;

    //Inflater Objects
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initObjects();
        presenter.retrieveMessage();


    }

    private void initObjects() {
        presenter = new ChatPresenter(this);
        unbinder = ButterKnife.bind(this);
        sendButton.setOnClickListener(this);

        messageListLayout.removeAllViews();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onClick(View v) {
        presenter.sendMessage(messageContent.getText().toString());
        messageContent.setText("");
    }

    @Override
    public void displayMessages(Message message) {

        layout = (RelativeLayout) inflater.inflate(R.layout.message, null);
        TextView messageContent = (TextView) layout.findViewById(R.id.messageContent);
        TextView messageUser = (TextView) layout.findViewById(R.id.messageUser);
        messageContent.setText(message.getMessageContent());
        messageUser.setText(message.getMessageUser());
        messageListLayout.addView(layout);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

}

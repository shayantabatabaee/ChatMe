package com.gravity.chatme.app.ui.chat;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ChatApplication;
import com.gravity.chatme.business.UserRepository;
import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.model.User;
import com.gravity.chatme.business.net.FirebaseHelper;
import com.gravity.chatme.util.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Message> mMessageList;
    private UserRepository userRepository;
    private ChatContract.Presenter presenter;

    final static int MESSAGE_OUT_PRE_SEND = 2;
    final static int MESSAGE_OUT_SENT = 0;
    final static int MESSAGE_IN = 1;
    final static int MESSAGE_OUT_FAILED = 3;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView messageUser;
        public TextView messageContent;
        public TextView messageTime;
        public ImageView retrySend;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_msg_profile);
            messageUser = (TextView) itemView.findViewById(R.id.messageUser);
            messageContent = (TextView) itemView.findViewById(R.id.messageContent);
            messageTime = (TextView) itemView.findViewById(R.id.messageTime);
            retrySend = (ImageView) itemView.findViewById(R.id.retrySend);
            this.itemView = itemView;
        }

    }

    public RecyclerViewAdapter(ArrayList<Message> mMessageList, ChatContract.Presenter presenter) {
        this.mMessageList = mMessageList;
        this.userRepository = UserRepository.getInstance();
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == MESSAGE_OUT_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.messageout, viewGroup, false);
        } else if (i == MESSAGE_IN) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.messagein, viewGroup, false);
        } else if (i == MESSAGE_OUT_FAILED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.messageoutfailed, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.messageoutpresend, viewGroup, false);
        }
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessageList.get(position).getMessageUser().equals(userRepository.getCurrentUser().getUsername())) {
            if (mMessageList.get(position).isMessageSent()) {
                return MESSAGE_OUT_SENT;
            } else if (mMessageList.get(position).isTimeout()) {
                return MESSAGE_OUT_FAILED;
            }
            return MESSAGE_OUT_PRE_SEND;
        } else {
            return MESSAGE_IN;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.messageContent.setText(mMessageList.get(i).getMessageContent());
        viewHolder.messageUser.setText(mMessageList.get(i).getMessageUser());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        viewHolder.messageTime.setText(simpleDateFormat.format(mMessageList.get(i).getMessageTime()));

        userRepository.getUser(mMessageList.get(i).getMessageUser(), new FirebaseHelper.FirebaseHelperListener.User() {
            @Override
            public void onGetUser(User user) {
                Glide.with(ChatApplication.getInstance().getApplicationContext()).
                        load(user.getUserImgUrl())
                        .crossFade()
                        .bitmapTransform(new CircleTransform(ChatApplication.getInstance().getApplicationContext()))
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(viewHolder.imageView);
            }
        });

        if (viewHolder.retrySend != null) {
            viewHolder.retrySend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.retrySendData(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}

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
    private String userName;
    UserRepository userRepository;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView messageUser;
        public TextView messageContent;
        public TextView messageTIme;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_msg_profile);
            messageUser = (TextView) itemView.findViewById(R.id.messageUser);
            messageContent = (TextView) itemView.findViewById(R.id.messageContent);
            messageTIme = (TextView) itemView.findViewById(R.id.messageTime);
            this.itemView = itemView;
        }

    }

    public RecyclerViewAdapter(ArrayList<Message> mMessageList, String userName) {
        this.mMessageList = mMessageList;
        this.userName = userName;
        this.userRepository = UserRepository.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == 0) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.messageout, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.messagein, viewGroup, false);
        }
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessageList.get(position).getMessageUser().equals(userName)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.messageContent.setText(mMessageList.get(i).getMessageContent());
        viewHolder.messageUser.setText(mMessageList.get(i).getMessageUser());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        viewHolder.messageTIme.setText(simpleDateFormat.format(mMessageList.get(i).getMessageTime()));
        //viewHolder.setPosition(mMessageList.get(i).getMessageUser(), userName);

        userRepository.getUser(mMessageList.get(i).getMessageUser(), new FirebaseHelper.FirebaseHelperListener.User() {
            @Override
            public void onGetUserByUserName(User user) {
                Glide.with(ChatApplication.getInstance().getApplicationContext()).
                        load(user.getUserImgUrl())
                        .crossFade()
                        .bitmapTransform(new CircleTransform(ChatApplication.getInstance().getApplicationContext()))
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(viewHolder.imageView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}

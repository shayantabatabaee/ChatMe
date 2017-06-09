package com.gravity.chatme.app.ui.status;


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
import com.gravity.chatme.business.model.User;
import com.gravity.chatme.util.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusRecyclerViewAdapter.ViewHolder> {


    private ArrayList<User> userList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView userEmail;
        public TextView lastSeen;
        public ImageView userImage;


        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.user);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            lastSeen = (TextView) itemView.findViewById(R.id.userLastSeen);
            userImage = (ImageView) itemView.findViewById(R.id.userImg);
        }
    }


    public StatusRecyclerViewAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.username.setText(userList.get(i).getUsername());
        viewHolder.userEmail.setText(userList.get(i).getEmail());
        if (userList.get(i).isOnline()) {
            viewHolder.lastSeen.setText("Online");
        } else {
            viewHolder.lastSeen.setText("Last Seen : " + new SimpleDateFormat("MMM d, hh:mm a").format(userList.get(i).getLastSeen())
            );
        }
        Glide.with(ChatApplication.getInstance().getApplicationContext()).
                load(userList.get(i).getUserImgUrl())
                .crossFade()
                .bitmapTransform(new CircleTransform(ChatApplication.getInstance().getApplicationContext()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewHolder.userImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

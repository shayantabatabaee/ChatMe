package com.gravity.chatme.app.ui.status;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ChatApplication;
import com.gravity.chatme.app.ui.contact.ContactActivity;
import com.gravity.chatme.business.model.User;
import com.gravity.chatme.util.CircleTransform;
import com.gravity.chatme.util.DateComparator;

import java.util.ArrayList;

public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusRecyclerViewAdapter.ViewHolder> {


    private ArrayList<User> userList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView lastSeen;
        public ImageView userImage;
        public final Context context;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.user);
            lastSeen = (TextView) itemView.findViewById(R.id.userLastSeen);
            userImage = (ImageView) itemView.findViewById(R.id.userImg);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.main_user_layout);
            context = itemView.getContext();
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.username.setText(userList.get(i).getUsername());
        if (userList.get(i).isOnline()) {
            viewHolder.lastSeen.setText("Online");
        } else {
            viewHolder.lastSeen.setText(DateComparator.compareDate(userList.get(i).getLastSeen()));
        }
        Glide.with(ChatApplication.getInstance().getApplicationContext()).
                load(userList.get(i).getUserImgUrl())
                .crossFade()
                .bitmapTransform(new CircleTransform(ChatApplication.getInstance().getApplicationContext()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.userImage);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.context, ContactActivity.class);
                intent.putExtra(ContactActivity.ARG_USERNAME, userList.get(i).getUsername());
                viewHolder.context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}

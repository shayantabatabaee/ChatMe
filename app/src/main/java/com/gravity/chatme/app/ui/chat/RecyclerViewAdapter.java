package com.gravity.chatme.app.ui.chat;


import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gravity.chatme.R;
import com.gravity.chatme.business.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Message> mMessageList;
    private String userName;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageUser;
        public TextView messageContent;
        public TextView messageTIme;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageUser = (TextView) itemView.findViewById(R.id.messageUser);
            messageContent = (TextView) itemView.findViewById(R.id.messageContent);
            messageTIme = (TextView) itemView.findViewById(R.id.messageTime);
            this.itemView = itemView;
        }

        public void setPosition(String messageUsername, String userName) {
            LinearLayout messageLayout = (LinearLayout) itemView.findViewById(R.id.messageLayout);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) messageLayout.getLayoutParams();

            if (messageUsername.equals(userName)) {
                messageLayout.setBackgroundResource(R.drawable.msg_out_new);
                layoutParams.gravity = Gravity.END;
                messageLayout.setGravity(Gravity.END);
                messageContent.setGravity(Gravity.END);
                messageTIme.setGravity(Gravity.START);
            } else {
                messageLayout.setBackgroundResource(R.drawable.msg_in);
                layoutParams.gravity = Gravity.START;
                messageLayout.setGravity(Gravity.START);
                messageContent.setGravity(Gravity.START);
                messageTIme.setGravity(Gravity.END);
            }
        }
    }

    public RecyclerViewAdapter(ArrayList<Message> mMessageList, String userName) {
        this.mMessageList = mMessageList;
        this.userName = userName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.messageContent.setText(mMessageList.get(i).getMessageContent());
        viewHolder.messageUser.setText(mMessageList.get(i).getMessageUser());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        viewHolder.messageTIme.setText(simpleDateFormat.format(mMessageList.get(i).getMessageTime()));
        viewHolder.setPosition(mMessageList.get(i).getMessageUser(), userName);

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}

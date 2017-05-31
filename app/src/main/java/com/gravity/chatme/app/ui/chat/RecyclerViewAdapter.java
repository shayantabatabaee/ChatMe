package com.gravity.chatme.app.ui.chat;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gravity.chatme.R;
import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Message> mMessageList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageUser;
        public TextView messageContent;

        public ViewHolder(View itemView) {
            super(itemView);
            messageUser = (TextView) itemView.findViewById(R.id.messageUser);
            messageContent = (TextView) itemView.findViewById(R.id.messageContent);
        }
    }

    public RecyclerViewAdapter(ArrayList<Message> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message, viewGroup, false);
        view.setBackgroundResource(R.drawable.rounded_corner);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.messageContent.setText(mMessageList.get(i).getMessageContent());
        viewHolder.messageUser.setText(mMessageList.get(i).getMessageUser());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}

package com.gravity.chatme.app.ui.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gravity.chatme.R;
import com.gravity.chatme.business.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatusActivity extends AppCompatActivity implements StatusContract.view {

    //Presenter Objects
    StatusContract.presenter presenter;
    //ButterKnife Objects
    private Unbinder unbinder;
    @BindView(R.id.userRecyclerView)
    RecyclerView userRecyclerView;
    //RecyclerView Objects
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //Number List Object
    ArrayList<User> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        initObjects();

        userRecyclerView.setLayoutManager(layoutManager);
        userRecyclerView.setAdapter(adapter);

        presenter.retrieveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public void initObjects() {
        unbinder = ButterKnife.bind(this);
        presenter = new StatusPresenter(this);
        userList = new ArrayList<>();
        adapter = new StatusRecyclerViewAdapter(userList);
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    public void displayData(ArrayList<User> users) {
        userList.clear();
        userList.addAll(users);
        adapter.notifyDataSetChanged();
    }
}

package com.agiliziumapps.whats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class FindUsers extends AppCompatActivity {
    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);
        InitializeRecyclerView();



    }

    private void InitializeRecyclerView() {
        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);
    }
}

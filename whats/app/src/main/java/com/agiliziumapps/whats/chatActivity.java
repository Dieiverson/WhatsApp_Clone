package com.agiliziumapps.whats;

import android.os.Bundle;

import com.agiliziumapps.whats.adapter.MessageAdapter;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chatActivity extends AppCompatActivity {

    private RecyclerView mChat;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private FloatingActionButton btnSend;
    ArrayList<MessageObject> messageList;
    String chatID, nameUser;
    DatabaseReference dbMessages;
    private TextView txtViewNomeChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatID = getIntent().getExtras().getString("chatID");
        nameUser = getIntent().getExtras().getString("nameUser");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        btnSend = findViewById(R.id.btnSend);
        txtViewNomeChat = findViewById(R.id.txtViewNomeChat);
        //txtViewNomeChat.setText(nameUser);
        dbMessages = ConfiguracaoFirebase.getDatabaseFirebase().child("chat").child(chatID);
        setSupportActionBar(toolbar);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        messageList = new ArrayList<>();
        inicializeRecyclerView();
        getChatMessages();
    }

    private void sendMessage()
    {
        EditText message = findViewById(R.id.edtMessage);
        if(!message.getText().toString().isEmpty())
        {
            DatabaseReference newMessageDb = dbMessages.push();
            Map newMassageMap = new HashMap<>();
            newMassageMap.put("text",message.getText().toString());
            newMassageMap.put("creator", UsuarioFirebase.getIdentificadorUsuario());
            newMessageDb.updateChildren(newMassageMap);
        }
        message.setText(null);
    }
    private void getChatMessages()
    {
        dbMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    String text = "", creatorId = "";
                    if(snapshot.child("text").getValue() != null)
                        text = snapshot.child("text").getValue().toString();
                    if(snapshot.child("creator").getValue() != null)
                        creatorId = snapshot.child("creator").getValue().toString();
                   MessageObject messageObject = new MessageObject(snapshot.getKey(),text,creatorId);
                   messageList.add(messageObject);
                   mChatLayoutManager.scrollToPosition(messageList.size()-1);
                   mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void inicializeRecyclerView()
    {
        messageList = new ArrayList<>();
        mChat = findViewById(R.id.recyclerViewMessages);
        mChat.setNestedScrollingEnabled(false);
        mChat.setHasFixedSize(true);
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        mChat.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new MessageAdapter(messageList,getApplicationContext());
        mChat.setAdapter(mChatAdapter);
    }
}
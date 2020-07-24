package com.agiliziumapps.whats;

import android.net.Uri;
import android.os.Bundle;

import com.agiliziumapps.whats.adapter.MensagensAdapter;
import com.agiliziumapps.whats.helper.Base64Custom;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.bumptech.glide.Glide;
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
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatActivity extends AppCompatActivity {

    private FloatingActionButton btnSend;
    private Usuario usuarioDestinatario;
    private TextView txtViewNomeChat;
    private CircleImageView circleImageFoto;
    private EditText mensagem;
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private RecyclerView recyclerViewMessages;
    private MensagensAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    List<Mensagem> mensagens;

    ChildEventListener childEventListenerMensagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        circleImageFoto         = findViewById(R.id.circleImageFoto);
        recyclerViewMessages    = findViewById(R.id.recyclerViewMessages);
        btnSend                 = findViewById(R.id.btnSend);
        mensagem                = findViewById(R.id.edtMessage);
        txtViewNomeChat         = findViewById(R.id.txtViewNomeChat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mensagens = new ArrayList<>();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            usuarioDestinatario = (Usuario)bundle.getSerializable("chatContato");
            txtViewNomeChat.setText(usuarioDestinatario.getNome());
            String foto = usuarioDestinatario.getFoto();
            if(foto != null)
            {
                Uri url = Uri.parse(foto);
                Glide.with(chatActivity.this).load(url).into(circleImageFoto);
            }
            else
            {
                circleImageFoto.setImageResource(R.drawable.padrao);
            }
            idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getNumeroTelefone());
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        adapter = new MensagensAdapter(mensagens, getApplicationContext());
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();
        database = ConfiguracaoFirebase.getDatabaseFirebase();
        mensagensRef = database.child("mensagens").child(idUsuarioRemetente).child(idUsuarioDestinatario);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setAdapter(adapter);
    }

    private void salvarMensagem(Mensagem mensagem)
    {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseFirebase();
        DatabaseReference mensagemRef = database.child("mensagens");
        mensagemRef.child(idUsuarioRemetente).child(idUsuarioDestinatario).push().setValue(mensagem);
    }

    @Override
    protected void onStart() {
        recuperarMensagens();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mensagensRef.removeEventListener(childEventListenerMensagens);
        super.onStop();
    }

    private void recuperarMensagens()
    {
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
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

    private void sendMessage()
    {
        String Textomsg = mensagem.getText().toString();
        if(!Textomsg.isEmpty())
        {
            Mensagem msg = new Mensagem();
            msg.setIdUsuario(idUsuarioRemetente);
            msg.setMensagem(Textomsg);
            salvarMensagem(msg);
            mensagem.setText("");

        }
    }
}
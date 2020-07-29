package com.agiliziumapps.whats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.agiliziumapps.whats.adapter.MensagensAdapter;
import com.agiliziumapps.whats.helper.Base64Custom;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private List<Mensagem> mensagens;
    private ImageView imgCameraSend;
    private StorageReference storageReference;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    ChildEventListener childEventListenerMensagens;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        circleImageFoto         = findViewById(R.id.circleImageFoto);
        recyclerViewMessages    = findViewById(R.id.recyclerViewMessages);
        btnSend                 = findViewById(R.id.btnSend);
        mensagem                = findViewById(R.id.edtMessage);
        txtViewNomeChat         = findViewById(R.id.txtViewNomeChat);
        imgCameraSend           = findViewById(R.id.imgCameraSend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mensagens = new ArrayList<>();
        storageReference = ConfiguracaoFirebase.getStorageFirebase();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        imgCameraSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });
    }

    private void carregarInicio() {
        if(usuarioDestinatario != null)
        {
            txtViewNomeChat.setText(usuarioDestinatario.getNome());
            String foto = usuarioDestinatario.getFoto();
            if(foto != null && !foto.isEmpty())
            {
                Uri url = Uri.parse(foto);
                Glide.with(chatActivity.this).load(url).into(circleImageFoto);
            }
            else
            {
                circleImageFoto.setImageResource(R.drawable.padrao);
            }
            idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getNumeroTelefone());
            adapter = new MensagensAdapter(mensagens, getApplicationContext());
            idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();
            database = ConfiguracaoFirebase.getDatabaseFirebase();
            mensagensRef = database.child("mensagens").child(idUsuarioRemetente).child(idUsuarioDestinatario);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewMessages.setLayoutManager(layoutManager);
            recyclerViewMessages.setHasFixedSize(true);
            recyclerViewMessages.setAdapter(adapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            //layoutManager.scrollToPosition(0);
            Bitmap imagem = null;
            try {
                switch (requestCode)
                {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap)data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                        break;
                }
                if(imagem != null)
                {
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
                    byte[] dadosImagem = byteArray.toByteArray();
                    String nomeImagem = UUID.randomUUID().toString();
                    final StorageReference imagemRef = storageReference.
                          child("imagens").
                          child("fotos").
                          child(idUsuarioRemetente).
                          child(nomeImagem);
                    UploadTask uploadTask  = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Erro ao fazer upload da imagem!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUsuario(idUsuarioRemetente);
                                    mensagem.setMensagem("Foto");
                                    mensagem.setImagem(url.toString());
                                    salvarMensagem(mensagem);
                                }
                            });
                        }
                    });
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
    private void salvarMensagem(Mensagem mensagem)
    {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseFirebase();
        DatabaseReference mensagemRef = database.child("mensagens");
        mensagemRef.child(idUsuarioRemetente).child(idUsuarioDestinatario).push().setValue(mensagem);//Salvar para remetente
        mensagemRef.child(idUsuarioDestinatario).child(idUsuarioRemetente).push().setValue(mensagem);//Salvar para destinatario
        salvarConversa(mensagem);
    }
    private void salvarConversa(Mensagem msg)
    {
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idUsuarioRemetente);
        conversaRemetente.setIdDesinatario(idUsuarioRemetente);
        conversaRemetente.setUltimaMensage(msg.getMensagem());
        conversaRemetente.setUsuarioExibicao(usuarioDestinatario);
        conversaRemetente.Salvar();
    }

    @Override
    protected void onStart() {
        sharedPreferences = this.getSharedPreferences("userChat", this.MODE_PRIVATE);
        String saved = sharedPreferences.getString("UserChatSaved", null);
        if(saved != null && usuarioDestinatario == null){
            usuarioDestinatario = new Gson().fromJson(saved, Usuario.class);
        }
        sharedPreferences.edit().putString("UserChatSaved",null).apply();
        carregarInicio();
        recuperarMensagens();
        super.onStart();
    }



    @Override
    protected void onStop() {
        sharedPreferences.edit().putString("UserChatSaved", new Gson().toJson(usuarioDestinatario)).apply();
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
                layoutManager.scrollToPosition(mensagens.size()-1);
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
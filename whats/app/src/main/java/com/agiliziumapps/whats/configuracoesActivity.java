package com.agiliziumapps.whats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agiliziumapps.whats.helper.Base64Custom;
import com.agiliziumapps.whats.helper.Permissao;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class configuracoesActivity extends AppCompatActivity {
    Toolbar toolbarPrincipal;
    ImageButton camera,imgAtualizarNome;
    CircleImageView profile_image;
    StorageReference storageReference;
    String identificadorUsuario;
    private Usuario usuarioLogado;
    TextView editNome;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private String[] permissoesNecessarias = new String[]
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.setTitle("Configurações");
        storageReference = ConfiguracaoFirebase.getStorageFirebase();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        camera = findViewById(R.id.imgButtonCamera);
        profile_image = findViewById(R.id.profile_image);
        editNome = findViewById(R.id.editNome);
        imgAtualizarNome = findViewById(R.id.imgAtualizarNome);
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        setSupportActionBar(toolbarPrincipal);
        final FirebaseUser usuarioFirebase = UsuarioFirebase.getUsuario();
        Uri url = usuarioFirebase.getPhotoUrl();
        if(url != null)
        {
            Glide.with(configuracoesActivity.this).load(url).into(profile_image);
        }
        else
        {

            profile_image.setImageResource(R.drawable.padrao);
        }
        editNome.setText(usuarioFirebase.getDisplayName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Permissao.validarPermissoes(permissoesNecessarias,this,1);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });

        imgAtualizarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UsuarioFirebase.atualizarNomeUsuario(editNome.getText().toString()))
                {
                    usuarioLogado.setNome(editNome.getText().toString());
                    usuarioLogado.atualizar();
                    Toast.makeText(getApplicationContext(),"Nome alterado com sucesso!",Toast.LENGTH_SHORT);
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
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
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,byteArray);
                    byte[] dadosImagem = byteArray.toByteArray();
                    profile_image.setImageBitmap(imagem);
                    final StorageReference imagemRef = storageReference.
                            child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg");
                    UploadTask uploadTask  = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Erro ao fazer upload da imagem!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"Imagem salva com sucesso!",Toast.LENGTH_SHORT).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                   Uri url = task.getResult();
                                   atualizaFotoUsuario(url);
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

    public void atualizaFotoUsuario(Uri url)
    {
        if(UsuarioFirebase.atualizarFotoUsuario(url))
        {
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();
            Toast.makeText(getApplicationContext(),"Foto atualizada com sucesso!",Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults)
        {
            if(permissaoResultado == PackageManager.PERMISSION_DENIED)
            {
                alertaValidacaoPermissao();
            }
        }
    }
    private void alertaValidacaoPermissao()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões.");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
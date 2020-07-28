package com.agiliziumapps.whats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.agiliziumapps.whats.helper.Base64Custom;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class InserirDados extends AppCompatActivity {

    Button btnContinuar;
    EditText edtTextName;
    CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_dados);
        btnContinuar = findViewById(R.id.btnContinuar);
        edtTextName = findViewById(R.id.edtTextName);
        profile_image = findViewById(R.id.profile_image);
        final FirebaseUser usuarioFirebase = UsuarioFirebase.getUsuario();
        Uri url = usuarioFirebase.getPhotoUrl();
        if(url != null)
            Glide.with(InserirDados.this).load(url).into(profile_image);
        else
            profile_image.setImageResource(R.drawable.padrao);

        edtTextName.setText(usuarioFirebase.getDisplayName());
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Util.usuario.setNome(edtTextName.getText().toString());
                    UsuarioFirebase.atualizarNomeUsuario(edtTextName.getText().toString());
                    Util.usuario.setId(UsuarioFirebase.getIdentificadorUsuario());
                    Util.usuario.setFoto("");
                    Util.usuario.Salvar();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                    return;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
}
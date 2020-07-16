package com.agiliziumapps.whats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.agiliziumapps.whats.helper.Base64Custom;

public class InserirDados extends AppCompatActivity {

    Button btnContinuar;
    EditText edtTextName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_dados);
        btnContinuar = findViewById(R.id.btnContinuar);
        edtTextName = findViewById(R.id.edtTextName);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try
                    {
                        Util.usuario.setNome(edtTextName.getText().toString());
                        String IdUsuario = Base64Custom.codificarBase64(Util.usuario.getNumeroTelefone());
                        Util.usuario.setId(IdUsuario);
                        Util.usuario.Salvar();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        return;
                    }
                    catch (Exception e)
                    {

                    }

            }
        });
    }
}
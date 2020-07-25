package com.agiliziumapps.whats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificarNumero extends AppCompatActivity {

    private Toolbar toolbarPrincipal;
    private Button btnVerificarCodigo;
    private EditText edtCodigoRecebido;
    private TextView txtError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_numero);
        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        edtCodigoRecebido = findViewById(R.id.edtCodigoRecebido);
        txtError = findViewById(R.id.txtError);
        btnVerificarCodigo  = findViewById(R.id.btn_verificarCodigo);
        toolbarPrincipal.setTitle("Verificar número de telefone");
        setSupportActionBar(toolbarPrincipal);
        txtError.setText("");
        btnVerificarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCodigo();
            }
        });
    }
    private void verificarCodigo() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Util.mVerificationId, edtCodigoRecebido.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Util.firebaseUser = task.getResult().getUser();
                            startActivity(new Intent(MainActivity.ctx,InserirDados.class));
                            finish();
                            return;
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                txtError.setText("O código informado é invalido!");
                            }
                        }
                    }
                });
    }
}
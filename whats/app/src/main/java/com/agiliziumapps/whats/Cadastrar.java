package com.agiliziumapps.whats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class Cadastrar extends AppCompatActivity {
    private Button btnVerificarNumero, btnVerificarCodigo;
    private ProgressBar progressBar;
    private TextView txt_error;
    private EditText edtCodigo, edtNumero;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        btnVerificarNumero  = findViewById(R.id.btn_VerificarNumero);
        progressBar         = findViewById(R.id.progressBar);
        txt_error           = findViewById(R.id.txt_error);
        btnVerificarCodigo  = findViewById(R.id.btnVerificarCódigo);
        edtCodigo           = findViewById(R.id.edt_Codigo);
        edtNumero           = findViewById(R.id.edt_Numero);
        Util.usuario = new Usuario();

        progressBar.setVisibility(View.GONE);
        txt_error.setText("");
        btnVerificarNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVerificarNumero.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable()
                                          {
                                              public void run()
                                              {
                                                  btnVerificarNumero.setEnabled(true);
                                                  progressBar.setVisibility(View.GONE);
                                              }
                                          }, 65000    //Specific time in milliseconds
                );
                verificarNumero(edtNumero.getText().toString());
            }
        });
        btnVerificarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, edtCodigo.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                txt_error.setText(R.string.erroTelefoneInvalidoFirebase + " - " + e.getMessage());
                progressBar.setVisibility(View.GONE);
                edtCodigo.setEnabled(false);
                btnVerificarNumero.setEnabled(true);
                btnVerificarCodigo.setEnabled(false);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    txt_error.setText(R.string.solicitacaoInvalidaFirebase);
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    txt_error.setText(R.string.erroCotaFirebase);
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                edtCodigo.setEnabled(true);
                btnVerificarCodigo.setEnabled(true);
                mVerificationId = verificationId;
                mResendToken = token;
                //btnVerificarNumero.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    private void verificarNumero(String numeroTelefone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                numeroTelefone,      // Phone number to verify
                60,               // Timeout duration
                TimeUnit.SECONDS,    // Unit of timeout
                this,        // Activity (for callback binding)
                mCallbacks);         // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
       mAuth = FirebaseAuth.getInstance();
       mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Util.firebaseUser = task.getResult().getUser();
                            Util.usuario.setNumeroTelefone(edtNumero.getText().toString());
                            startActivity(new Intent(getApplicationContext(),InserirDados.class));
                            finish();
                            return;
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                txt_error.setText("O código informado é invalido!");
                                btnVerificarNumero.setEnabled(true);
                            }
                        }
                    }
                });
    }



}

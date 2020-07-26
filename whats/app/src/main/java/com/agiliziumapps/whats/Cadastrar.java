package com.agiliziumapps.whats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.agiliziumapps.whats.helper.MaskEditUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Cadastrar extends AppCompatActivity {
    private FloatingActionButton btnVerificarNumero ;
    private ProgressBar progressBar;
    private TextView txt_error;
    private EditText edtNumero, edtCodeCountry;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Spinner spinnerCountry;
    private Toolbar toolbarPrincipal;
    List<String> paises;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        edtCodeCountry      = findViewById(R.id.edtTextCode);
        btnVerificarNumero  = findViewById(R.id.btn_VerificarNumero);
        toolbarPrincipal    = findViewById(R.id.toolbarPrincipal);
        progressBar         = findViewById(R.id.progressBar);
        txt_error           = findViewById(R.id.txt_error);
        edtNumero           = findViewById(R.id.edt_Numero);
        spinnerCountry      = findViewById(R.id.spinnerCountry);
        toolbarPrincipal.setTitle("Verificar número de telefone");
        setSupportActionBar(toolbarPrincipal);
        final CountryCodes countryCodes = new CountryCodes();
        paises = countryCodes.getAllCountries();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,paises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);
        String countryISO = Util.getCoutryISO(getApplicationContext());//Pegar o código do país
        edtCodeCountry.setText(countryISO);
        String countryToSelect = CountryToPhonePrefix.getCountrySigla(countryISO);//Pegar a sigla do país
        countryToSelect = countryCodes.getCountry(countryToSelect); //Pegar o nome do país
        int spinnerPosition = adapter.getPosition(countryToSelect); //Pegar a posição do país no spinner, baseado no nome
        spinnerCountry.setSelection(spinnerPosition);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
              String pais = paises.get(position);
              pais = countryCodes.getSigla(pais);
              pais = CountryToPhonePrefix.getPhone(pais);
              edtCodeCountry.setText(pais);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                edtCodeCountry.setText("+55");
            }
        });

        edtNumero.addTextChangedListener(MaskEditUtil.mask(edtNumero,MaskEditUtil.FORMAT_FONE));
        Util.usuario = new Usuario();
        progressBar.setVisibility(View.GONE);
        txt_error.setText("");

        btnVerificarNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarNumero();
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
                btnVerificarNumero.setEnabled(true);
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
                Util.mVerificationId = verificationId;
                mResendToken = token;
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),VerificarNumero.class));
                finish();
                return;
            }
        };
    }

    private void verificarNumero() {
        btnVerificarNumero.setEnabled(false);
        edtNumero.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        String numeroTelefone = edtCodeCountry.getText().toString() + edtNumero.getText().toString();
        numeroTelefone = MaskEditUtil.unmask(numeroTelefone);
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
                        startActivity(new Intent(MainActivity.ctx,InserirDados.class));
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

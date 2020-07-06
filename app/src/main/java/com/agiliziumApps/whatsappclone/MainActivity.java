package com.agiliziumApps.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private EditText phone, code;
    private Button sendCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = findViewById(R.id.edt_phone);
        code = findViewById(R.id.edt_code);
        sendCode = findViewById(R.id.btn_sendCode);
        FirebaseApp.initializeApp(this);
        userIsLoggedIn();
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVerificationId != null)
                {
                    verifyPhoneNumberWithCode(mVerificationId,code.getText().toString());
                }
                startNumberVeification();
            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                sigInWithAuthPhoneCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String VerificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(VerificationId, forceResendingToken);
                mVerificationId = VerificationId;
                sendCode.setText("Verificar Código");
            }
        };

    }
    private void verifyPhoneNumberWithCode(String VerificationId,String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId,code);
        sigInWithAuthPhoneCredential(credential);
    }
    private void sigInWithAuthPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    userIsLoggedIn();
                }
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
            finish();
            return;
        }
    }

    private void startNumberVeification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone.getText().toString(),60, TimeUnit.SECONDS,this,mCallBacks);
    }
}
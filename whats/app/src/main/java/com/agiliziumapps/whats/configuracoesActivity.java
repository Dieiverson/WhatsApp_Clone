package com.agiliziumapps.whats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class configuracoesActivity extends AppCompatActivity {
    Toolbar toolbarPrincipal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.setTitle("Configurações");
        setSupportActionBar(toolbarPrincipal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
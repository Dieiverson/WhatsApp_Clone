package com.agiliziumapps.whats;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {
    private String Id;
    private String nome;
    private String numeroTelefone;

    public void Salvar()
    {
        try {
            DatabaseReference firebaseRef = ConfiguracaoFirebase.getDatabaseFirebase();
            DatabaseReference usuario = firebaseRef.child("usuarios").child(getId());
            usuario.setValue(this);
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

    @Exclude
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
}

package com.agiliziumapps.whats;

import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {
    private String Id;
    private String nome;
    private String numeroTelefone;
    private String foto;

    public Usuario()
    {

    }
    public Usuario(String nome, String numeroTelefone) {
        this.nome = nome;
        this.numeroTelefone = numeroTelefone;
    }

    public Usuario(String id,String nome, String numeroTelefone, String foto)
    {
        this.Id = id;
        this.nome = nome;
        this.numeroTelefone = numeroTelefone;
        this.foto = foto;
    }



    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

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
    public void atualizar()
    {
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference DatabaseReference = ConfiguracaoFirebase.getDatabaseFirebase();
        DatabaseReference usuarioReference = DatabaseReference.child("usuarios").child(identificadorUsuario);
        usuarioReference.updateChildren(converterParaMap());
    }

    @Exclude
    public Map<String,Object> converterParaMap()
    {
        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("numeroTelefone",getNumeroTelefone());
        usuarioMap.put("nome",getNome());
        usuarioMap.put("foto",getFoto());
        return  usuarioMap;
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

package com.agiliziumapps.whats;

import com.google.firebase.database.DatabaseReference;

public class Conversa {
    private String idRemetente, idDesinatario, ultimaMensage;
    private Usuario usuarioExibicao;

    public Conversa() {
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDesinatario() {
        return idDesinatario;
    }

    public void setIdDesinatario(String idDesinatario) {
        this.idDesinatario = idDesinatario;
    }

    public String getUltimaMensage() {
        return ultimaMensage;
    }

    public void setUltimaMensage(String ultimaMensage) {
        this.ultimaMensage = ultimaMensage;
    }

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }
    public void Salvar()
    {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseFirebase();
        DatabaseReference conversaRef = database.child("conversas");
        conversaRef.child(this.getIdRemetente()).child(this.getIdDesinatario()).setValue(this);
    }
}

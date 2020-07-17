package com.agiliziumapps.whats.helper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.agiliziumapps.whats.ConfiguracaoFirebase;
import com.agiliziumapps.whats.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {
    public static String getIdentificadorUsuario()
    {
        FirebaseAuth usuario = ConfiguracaoFirebase.getAuth();
        String fone = usuario.getCurrentUser().getPhoneNumber();
        return Base64Custom.codificarBase64(fone);
    }

    public static FirebaseUser getUsuario()
    {
        FirebaseAuth usuario = ConfiguracaoFirebase.getAuth();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarFotoUsuario(Uri url)
    {
        try
        {
            FirebaseUser user = getUsuario();
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();
            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Erro", "erro ao atualizar foto de perfil");
                    }
                }
            });
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario getDadosUsuarioLogado()
    {
        FirebaseUser usuarioFirebase =  getUsuario();
        Usuario usuario = new Usuario();
        usuario.setNumeroTelefone(usuarioFirebase.getPhoneNumber());
        usuario.setNome(usuarioFirebase.getDisplayName());
        if(usuarioFirebase.getPhotoUrl() == null)
        {
            usuario.setFoto("");
        }
        else
        {
            usuario.setFoto(usuarioFirebase.getPhotoUrl().toString());
        }
        return usuario;
    }

    public static boolean atualizarNomeUsuario(String nome)
    {
        try
        {
            FirebaseUser user = getUsuario();
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Erro", "erro ao nome do perfil");
                    }
                }
            });
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}

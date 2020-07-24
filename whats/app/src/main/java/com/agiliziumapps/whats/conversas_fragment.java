package com.agiliziumapps.whats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agiliziumapps.whats.adapter.ConversasAdapter;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class conversas_fragment extends Fragment {
    RecyclerView recyclerViewConversas;
    private List<Conversa> listaConversa;
    private ConversasAdapter adapter;

    public conversas_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
    DatabaseReference database = ConfiguracaoFirebase.getDatabaseFirebase();
    DatabaseReference conversasRef = database.child("conversas").child(identificadorUsuario);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas_fragment, container, false);
        recyclerViewConversas = view.findViewById(R.id.recyclerViewListaConversas);
        adapter = new ConversasAdapter(listaConversa,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        return view;
    }
    public void recuperarConversas()
    {

        conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })

    }
}
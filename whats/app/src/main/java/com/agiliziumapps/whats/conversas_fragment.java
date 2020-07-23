package com.agiliziumapps.whats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agiliziumapps.whats.adapter.conversasAdapter;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class conversas_fragment extends Fragment {

    private RecyclerView recyclerViewListaConversas;
    private conversasAdapter adapter;
    private ArrayList<ChatObject> conversas = new ArrayList<>();

    public conversas_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getUserChatList()
    {
        DatabaseReference userChatDb = ConfiguracaoFirebase.getDatabaseFirebase().child("usuarios").child(UsuarioFirebase.getIdentificadorUsuario()).child("chat");
        userChatDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        ChatObject chatObject = new ChatObject(dataSnapshot.getKey());
                        boolean exists = false;
                        for (ChatObject chatObjectIterator : conversas)
                        {
                            if(chatObjectIterator.getChatId().equals(chatObject.getChatId()))
                                exists = true;
                        }
                        if(exists)
                            continue;
                        conversas.add(chatObject);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas_fragment, container, false);
        recyclerViewListaConversas = view.findViewById(R.id.recyclerViewListaConversas);
        recyclerViewListaConversas.setNestedScrollingEnabled(false);
        recyclerViewListaConversas.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerViewListaConversas.setLayoutManager(layoutManager);
        adapter = new conversasAdapter(conversas,getActivity());
        recyclerViewListaConversas.setAdapter(adapter);
        getUserChatList();
        return view;
    }
}
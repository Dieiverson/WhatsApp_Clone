package com.agiliziumapps.whats;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.agiliziumapps.whats.adapter.contatosAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class fragment_contatos extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private contatosAdapter adapter;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Usuario> listaDeContatos= new ArrayList<>();
    public fragment_contatos() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaConversas);
        recyclerViewListaContatos.setNestedScrollingEnabled(false);
        recyclerViewListaContatos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        adapter = new contatosAdapter(usuarios,getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        getContactList();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getContactList()
    {
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        String ISOPrefix = getCoutryISO();
        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replace(" ","");
            phoneNumber = phoneNumber.replace("-","");
            phoneNumber = phoneNumber.replace(")","");
            phoneNumber = phoneNumber.replace("(","");

            if(!String.valueOf(phoneNumber.charAt(0)).equals("+"))
            {
                phoneNumber = ISOPrefix + phoneNumber;
            }
            Usuario mContact = new Usuario(name, phoneNumber);
            getUserDetail(mContact);
            listaDeContatos.add(mContact);
        }
    }

    private void getUserDetail(Usuario contato) {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseFirebase().child("usuarios");
        Query query = databaseReference.orderByChild("numeroTelefone").equalTo(contato.getNumeroTelefone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                   String phone = "",name = "", foto = "";
                   for (DataSnapshot childSnap: snapshot.getChildren())
                   {
                       if(childSnap.child("numeroTelefone") != null)
                            phone = childSnap.child("numeroTelefone").getValue().toString();
                       if(childSnap.child("nome") != null)
                           name = childSnap.child("nome").getValue().toString();
                       if(childSnap.child("foto") != null)
                           foto = childSnap.child("foto").getValue().toString();
                       Usuario user = new Usuario(childSnap.getKey(), name,phone,foto);
                       boolean exists = false;
                       for (Usuario usuarioIterator : usuarios)
                       {
                           if(usuarioIterator.getNumeroTelefone().equals(user.getNumeroTelefone()))
                               exists = true;
                       }
                       if(exists)
                           continue;
                       usuarios.add(user);
                       adapter.notifyDataSetChanged();
                       return;
                   }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getCoutryISO()
    {
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso() != null)
        {
            if (!telephonyManager.getNetworkCountryIso().equals(""))
            {
                iso = telephonyManager.getNetworkCountryIso();
            }

        }
        return CountryToPhonePrefix.getPhone(iso);
    }

    @Override
    public void onStop() {
        super.onStop();

    }


}
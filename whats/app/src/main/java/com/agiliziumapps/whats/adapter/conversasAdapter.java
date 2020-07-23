package com.agiliziumapps.whats.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.agiliziumapps.whats.ChatObject;
import com.agiliziumapps.whats.R;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class conversasAdapter extends RecyclerView.Adapter<conversasAdapter.ViewHolder> {
    List<ChatObject> conversas;
    Context context;
    public conversasAdapter(List<ChatObject> listaContatos, Context c) {
        this.conversas = listaContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public conversasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent,false);
        return new conversasAdapter.ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull conversasAdapter.ViewHolder holder, int position) {

        final ChatObject conversa = conversas.get(position);
        /*holder.nome.setText(usuario.getNome());
        holder.telefone.setText(usuario.getNumeroTelefone());
        if(usuario.getFoto() != null && !usuario.getFoto().equals(""))
        {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }
        else
        {
            holder.foto.setImageResource(R.drawable.padrao);
        }*/
        holder.nome.setText(conversa.getChatId());
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome, conversa;
        public LinearLayout mLinearLayout;
        public ViewHolder(View itemView)
        {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            conversa = itemView.findViewById(R.id.texTelefoneContato);
            mLinearLayout = itemView.findViewById(R.id.linearLayout);
        }

    }
}

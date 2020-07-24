package com.agiliziumapps.whats.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agiliziumapps.whats.Conversa;
import com.agiliziumapps.whats.R;
import com.agiliziumapps.whats.Usuario;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.ViewHolder>
{
    List<Conversa> conversas;
    Context context;
    public ConversasAdapter(List<Conversa> lista, Context c)
    {
        this.conversas = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent,false);
        return new ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conversa conversa = conversas.get(position);
        holder.ultimaMensagem.setText(conversa.getUltimaMensage());
        Usuario usuario = conversa.getUsuarioExibicao();
        holder.nome.setText(usuario.getNome());
        if(usuario.getFoto() != null && !usuario.getFoto().equals(""))
        {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }
        else
        {
            holder.foto.setImageResource(R.drawable.padrao);
        }

    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, ultimaMensagem;
        public LinearLayout mLinearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            ultimaMensagem = itemView.findViewById(R.id.texTelefoneContato);
            mLinearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}

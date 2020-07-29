package com.agiliziumapps.whats.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agiliziumapps.whats.FullScreenImage;
import com.agiliziumapps.whats.Mensagem;
import com.agiliziumapps.whats.R;
import com.agiliziumapps.whats.helper.UsuarioFirebase;
import com.bumptech.glide.Glide;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.viewHolder> {

    List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensagensAdapter(List<Mensagem> mensagens, Context context) {
        this.mensagens = mensagens;
        this.context = context;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
       if(viewType == TIPO_REMETENTE)
       {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente,parent,false);
       }
       else
       {
           item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario,parent,false);
       }
       return new viewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        final String img = mensagem.getImagem();
        if(img != null)
        {
            Uri url = Uri.parse(img);
            holder.imagem.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(url)
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .into(holder.imagem);
            holder.mensagem.setVisibility(View.GONE);
            holder.imagem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("imagem",img);
                    context.startActivity(intent);
                }
            });
        }
        else
        {
            holder.mensagem.setText(msg);
            holder.imagem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        if(idUsuario.equals(mensagem.getIdUsuario()))
        {
            return TIPO_REMETENTE;
        }
        return TIPO_DESTINATARIO;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
    TextView mensagem;
    ImageView imagem;
        public viewHolder(View itemView) {
            super(itemView);
            mensagem = itemView.findViewById(R.id.txtMensagemTexto);
            imagem = itemView.findViewById(R.id.imageMensagemFoto);

        }
    }
}

package com.agiliziumapps.whats.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.agiliziumapps.whats.MessageObject;
import com.agiliziumapps.whats.R;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<MessageObject> messageList;
    Context context;
    public MessageAdapter(List<MessageObject> messageList, Context c) {
        this.messageList = messageList;
        this.context = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Mmessage.setText(messageList.get(position).getText());
        holder.Msender.setText(messageList.get(position).getSenderId());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Mmessage, Msender;
        LinearLayout mLinearLayout;
        ViewHolder(View itemView)
        {
            super(itemView);
            Mmessage = itemView.findViewById(R.id.message);
            Msender = itemView.findViewById(R.id.sender);
            mLinearLayout = itemView.findViewById(R.id.linearLayout);
        }

    }
}

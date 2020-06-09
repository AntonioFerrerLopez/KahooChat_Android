package com.afl.kahootchat.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.afl.kahootchat.ENTITIES.MODELS.MensajeRecibir;
import com.afl.kahootchat.HOLDERS.Mensajeria_Holder;
import com.afl.kahootchat.R;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mensajeria_Adapter extends RecyclerView.Adapter<Mensajeria_Holder> {

    private static final String TYPE_IMAGE = "2";
    private static final String TYPE_MENSAJE = "1";
    private List<MensajeRecibir> listMensaje = new ArrayList<>();
    private Context c;

    public Mensajeria_Adapter(Context c) {
        this.c = c;
    }

    public void addMensaje(MensajeRecibir m) {
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());

    }

    @Override
    public Mensajeria_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new Mensajeria_Holder(v);
    }

    @Override
    public void onBindViewHolder(Mensajeria_Holder holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        if (listMensaje.get(position).getType_mensaje().equals(TYPE_IMAGE)) {
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(listMensaje.get(position).getFotoUri()).into(holder.getFotoMensaje());
        } else if (listMensaje.get(position).getType_mensaje().equals(TYPE_MENSAJE)) {
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }
        if (listMensaje.get(position).getFotoPerfil().isEmpty()) {
            holder.getFotoMensajePerfil().setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(c).load(listMensaje.get(position).getFotoPerfil()).into(holder.getFotoMensajePerfil());
        }

        Long codHora = listMensaje.get(position).getHora();
        Date date = new Date(codHora);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        holder.getHora().setText(simpleDateFormat.format(date));

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
package com.afl.kahootchat.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS.MensajeDMO;
import com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS.UsuarioDMO;
import com.afl.kahootchat.ENTITIES.HOLDERS.Mensajeria_Holder;
import com.afl.kahootchat.HELPERS.Constants;
import com.afl.kahootchat.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Mensajeria_Adapter extends RecyclerView.Adapter<Mensajeria_Holder> {

    private List<MensajeDMO> listMensaje = new ArrayList<>();
    private Context c;

    public Mensajeria_Adapter(Context c) {
        this.c = c;
    }

    public int addMensaje(MensajeDMO msjDMO) {
        listMensaje.add(msjDMO);
        int position = listMensaje.size() - Constants.LIST_STARTS_IN_ZERO;
        notifyItemInserted(listMensaje.size());
        return position;

    }

    public void updateMesage(int position , MensajeDMO msj){
        listMensaje.set(position,msj);
        notifyItemChanged(position);
    }

    @Override
    public Mensajeria_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new Mensajeria_Holder(v);
    }

    @Override
    public void onBindViewHolder(Mensajeria_Holder holder, int position) {

        MensajeDMO  mesajeDMO = listMensaje.get(position);
        UsuarioDMO usuarioDMO = mesajeDMO.getUsuarioDMO();
        if(usuarioDMO != null){
            holder.getNombre().setText(usuarioDMO.getUsuario().getNombre());
            Glide.with(c).load(usuarioDMO.getUsuario().getFotoPerfilUri()).into(holder.getFotoMensajePerfil());
        }


        holder.getMensaje().setText(mesajeDMO.getMensaje().getMensaje());
        if(mesajeDMO.getMensaje().isContainsPhoto()){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(mesajeDMO.getMensaje().getFotoUri()).into(holder.getFotoMensaje());
        }else{
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }

        holder.getHora().setText(mesajeDMO.getMesajeDateCreation());
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
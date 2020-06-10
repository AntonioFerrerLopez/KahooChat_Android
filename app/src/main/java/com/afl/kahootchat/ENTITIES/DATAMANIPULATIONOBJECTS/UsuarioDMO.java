package com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS;

import com.afl.kahootchat.ENTITIES.DAO.UsuarioDAO;
import com.afl.kahootchat.ENTITIES.MODELS.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UsuarioDMO {
    private String key;
    private Usuario usuario ;

    public UsuarioDMO(String key, Usuario usuario) {
        this.key = key;
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public String getCreationDateFormatted(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy" , Locale.getDefault());
        Date createdDate = new Date(UsuarioDAO.getInstance().obtainDateCreationLong());
        return dateFormat.format(createdDate);

    }    public String getLastSignInLogDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy" , Locale.getDefault());
        Date createdDate = new Date(UsuarioDAO.getInstance().obtainDateLastSignInLong());
        return dateFormat.format(createdDate);
    }

}

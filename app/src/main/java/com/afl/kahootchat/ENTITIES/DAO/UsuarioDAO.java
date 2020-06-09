package com.afl.kahootchat.ENTITIES.DAO;

import com.afl.kahootchat.ENTITIES.MODELS.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UsuarioDAO {
    private String key;
    private Usuario usuario ;

    public UsuarioDAO(String key, Usuario usuario) {
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

    public Long getCreateTimestampReturnLong(){
        return (Long) usuario.getCreateTimestamp();
    }

    public String getCreationDateFormatted(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy" , Locale.getDefault());
        Date createdDate = new Date(getCreateTimestampReturnLong());
        return dateFormat.format(createdDate);
    }
}

package com.afl.kahootchat.ENTITIES.DAO;

import com.afl.kahootchat.ENTITIES.MODELS.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UsuarioDAO {
    private Usuario usuario ;

    public Long getCreateTimestampReturnLong(){
        return (Long) usuario.getCreateTimestamp();
    }

    public String getCreationDateFormatted(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy" , Locale.getDefault());
        Date createdDate = new Date(getCreateTimestampReturnLong());
        return dateFormat.format(createdDate);
    }
}

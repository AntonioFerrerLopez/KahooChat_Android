package com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS;

import com.afl.kahootchat.ENTITIES.MODELS.Mensaje;

import org.ocpsoft.prettytime.PrettyTime;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.Date;
import java.util.Locale;

public class MensajeDMO {

    private String msjKey;
    private Mensaje mensaje;
    private UsuarioDMO usuarioDMO;

    public MensajeDMO(String msjKey, Mensaje mensaje) {
        this.msjKey = msjKey;
        this.mensaje = mensaje;
    }

    public String getMsjKey() {
        return msjKey;
    }

    public void setMsjKey(String msjKey) {
        this.msjKey = msjKey;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public Long getCreatedTimestampLong(){
        return (Long) mensaje.getCreatedTimestamp();
    }

    public UsuarioDMO getUsuarioDMO() {
        return usuarioDMO;
    }

    public void setUsuarioDMO(UsuarioDMO usuarioDMO) {
        this.usuarioDMO = usuarioDMO;
    }

    public String getMesajeDateCreation(){
        Date date = new Date(getCreatedTimestampLong());
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        return  prettyTime.format(date);
    }
}


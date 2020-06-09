package com.afl.kahootchat.ENTITIES.MODELS;

import com.google.firebase.database.ServerValue;

public class Mensaje {

    private String mensaje ;
    private String fotoUri;
    private boolean containsPhoto;
    private String senderKey;
    private Object createdTimestamp;


    public Mensaje() {
        createdTimestamp = ServerValue.TIMESTAMP;
    }


    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isContainsPhoto() {
        return containsPhoto;
    }

    public void setContainsPhoto(boolean containsPhoto) {
        this.containsPhoto = containsPhoto;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }
}

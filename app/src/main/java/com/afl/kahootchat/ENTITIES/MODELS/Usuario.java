package com.afl.kahootchat.ENTITIES.MODELS;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class Usuario {

    private String fotoPerfilUri;
    private String nombre;
    private String email;
    private Object createTimestamp;

    public Usuario() {
        createTimestamp = ServerValue.TIMESTAMP;
    }

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoPerfilUri() {
        return fotoPerfilUri;
    }

    public void setFotoPerfilUri(String fotoPerfilUri) {
        this.fotoPerfilUri = fotoPerfilUri;
    }

    public Object getCreateTimestamp() {
        return createTimestamp;
    }


}

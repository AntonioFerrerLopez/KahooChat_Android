package com.afl.kahootchat.ENTITIES.MODELS;

import com.google.firebase.database.ServerValue;

public class Usuario {

    private String fotoPerfilUri;
    private String nombre;
    private String email;
    private Long birthDate;


    public Usuario() {

    }

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }
    public String getFotoPerfilUri() {
        return fotoPerfilUri;
    }

    public void setFotoPerfilUri(String fotoPerfilUri) {
        this.fotoPerfilUri = fotoPerfilUri;
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

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }




}

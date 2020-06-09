package com.afl.kahootchat.ENTITIES.MODELS;

public class Mensaje {

    private String mensaje ;
    private String fotoUri;
    private String nombre;
    private String fotoPerfil ;
    private String type_mensaje;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String type_mensaje) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;
    }

    public Mensaje(String mensaje, String fotoUri, String nombre, String fotoPerfil, String type_mensaje) {
        this.mensaje = mensaje;
        this.fotoUri = fotoUri;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "mensaje='" + mensaje + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                ", type_mensaje='" + type_mensaje + '\'' +
                '}';
    }
}

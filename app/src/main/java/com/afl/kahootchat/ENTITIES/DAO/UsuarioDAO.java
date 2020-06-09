package com.afl.kahootchat.ENTITIES.DAO;

import com.google.firebase.auth.FirebaseAuth;

public class UsuarioDAO {

    public static String getUserKey(){
        return FirebaseAuth.getInstance().getUid();
    }
}

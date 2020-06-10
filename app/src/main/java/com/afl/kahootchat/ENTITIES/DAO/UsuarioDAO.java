package com.afl.kahootchat.ENTITIES.DAO;

import androidx.annotation.NonNull;

import com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS.UsuarioDMO;
import com.afl.kahootchat.ENTITIES.MODELS.Usuario;
import com.afl.kahootchat.HELPERS.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static UsuarioDAO usuarioDAO = null ;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static UsuarioDAO getInstance(){
        if(usuarioDAO == null){
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    private UsuarioDAO(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.USERS_FIREBASE_NODE);
    }


    public  String getUserKey(){
        return FirebaseAuth.getInstance().getUid();
    }

    public Long obtainDateCreationLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }

    public Long obtainDateLastSignInLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }



}

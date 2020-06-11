package com.afl.kahootchat.ENTITIES.DAO;

import android.net.Uri;
import android.os.Build;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS.UsuarioDMO;
import com.afl.kahootchat.ENTITIES.MODELS.Usuario;
import com.afl.kahootchat.HELPERS.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;


public class UsuarioDAO {

    public interface PhotoUriSender{
        void sendUriPhotoString(String uri);
    }

    public interface UserFromFirebaseSender{
        void sendUserFromFirebase(UsuarioDMO usuarioDMO);
        void errorHasOccurred(String error);
    }

    private static UsuarioDAO usuarioDAO = null ;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference referenceUsers;
    private FirebaseStorage storage;
    private StorageReference referencePhotoProfile;

    public static UsuarioDAO getInstance(){
        if(usuarioDAO == null){
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    private UsuarioDAO(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUsers = firebaseDatabase.getReference(Constants.USERS_FIREBASE_NODE);
        storage = FirebaseStorage.getInstance();
        referencePhotoProfile = storage.getReference("" + Constants.FOTO_DIR + Constants.SLASH + Constants.FOTO_PROFILE_DIR + Constants.SLASH + getUserKey());
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

    public void obtainInfoOfUserByKey(final String userKey , final UserFromFirebaseSender userFromFirebaseSender ){
        referenceUsers.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                UsuarioDMO usuarioDMO = new UsuarioDMO(userKey,usuario);
                userFromFirebaseSender.sendUserFromFirebase(usuarioDMO);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userFromFirebaseSender.errorHasOccurred(databaseError.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updatePhotoByUri(Uri uri,PhotoUriSender photoUriSender){
        final PhotoUriSender photoUritoSend = photoUriSender;
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("SSS.ss-mm-hh-dd-MM-yyyy");
        String photoName = currentDateTime.format(formatter);
        final StorageReference fotoReferencia = referencePhotoProfile.child(photoName);

        fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fotoReferencia.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri uriPhotoOnFirebase = task.getResult();
                    photoUritoSend.sendUriPhotoString(uriPhotoOnFirebase.toString());
                }
            }
        });
    }

}

















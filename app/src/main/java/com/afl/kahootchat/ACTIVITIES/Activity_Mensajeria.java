package com.afl.kahootchat.ACTIVITIES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afl.kahootchat.ENTITIES.DAO.UsuarioDAO;
import com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS.MensajeDMO;
import com.afl.kahootchat.ENTITIES.DATAMANIPULATIONOBJECTS.UsuarioDMO;
import com.afl.kahootchat.ENTITIES.MODELS.Mensaje;
import com.afl.kahootchat.ENTITIES.MODELS.Usuario;
import com.afl.kahootchat.ADAPTERS.Mensajeria_Adapter;
import com.afl.kahootchat.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Mensajeria extends AppCompatActivity {

    private static final String LOBY_NAME = "Sala_KahooChat";
    private final Integer SEND_IMAGE_OK = 1 ;
    private final Integer SEND_FOTO_PERFIL_OK = 2 ;

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje ;
    private Button btnEnviar;
    private Button btnLogOut;
    private ImageButton btnEnviarFoto;

    private Mensajeria_Adapter adapter;
    private FirebaseDatabase database ;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth ;
    private String nombreUsuarioLogeado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        fotoPerfil = findViewById(R.id.fotoPerfil);
        nombre =  findViewById(R.id.nombre);
        rvMensajes = findViewById(R.id.rvMensajes);
        txtMensaje =  findViewById(R.id.txtMensaje);
        btnEnviar =  findViewById(R.id.btnEnviar);
        btnEnviarFoto =  findViewById(R.id.btnEnviarFoto);
        btnLogOut =  findViewById(R.id.btnlogOut);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(LOBY_NAME);
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new Mensajeria_Adapter(this);
        LinearLayoutManager linearMensaje = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(linearMensaje);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!txtMensaje.getText().toString().isEmpty()){
                Mensaje msj = new Mensaje();
                msj.setMensaje(txtMensaje.getText().toString());
                msj.setContainsPhoto(false);
                msj.setSenderKey(UsuarioDAO.getInstance().getUserKey());
                databaseReference.push().setValue(msj);
                txtMensaje.setText("");
            }

            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Selecciona una foto" ), SEND_IMAGE_OK);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                gotoLoginActivityAndFinish();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Selecciona una foto" ), SEND_FOTO_PERFIL_OK);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            Map<String, UsuarioDMO> temporalUsersList = new HashMap<>();


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Mensaje msjToLoad = dataSnapshot.getValue(Mensaje.class);
                final MensajeDMO mensajeDMO = new  MensajeDMO(dataSnapshot.getKey(),msjToLoad);
                final int position = adapter.addMensaje(mensajeDMO);

                if(temporalUsersList.get(msjToLoad.getSenderKey()) != null ){
                    mensajeDMO.setUsuarioDMO(temporalUsersList.get(msjToLoad.getSenderKey()));
                    adapter.updateMesage(position,mensajeDMO);
                }else{
                    UsuarioDAO.getInstance().obtainInfoOfUserByKey(msjToLoad.getSenderKey(), new UsuarioDAO.UserFromFirebaseSender() {
                        @Override
                        public void sendUserFromFirebase(UsuarioDMO usuarioDMO) {
                            temporalUsersList.put(msjToLoad.getSenderKey(),usuarioDMO);
                            mensajeDMO.setUsuarioDMO(usuarioDMO);
                            adapter.updateMesage(position,mensajeDMO);
                        }

                        @Override
                        public void errorHasOccurred(String error) {
                            Toast.makeText(Activity_Mensajeria.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            btnEnviar.setEnabled(false);
            DatabaseReference reference =  database.getReference("Usuarios/" + currentUser.getUid() );
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    for (DataSnapshot usuario : dataSnapshot.getChildren()){
                   //     nombreUsuarioLogeado = usuario.getValue(Usuario.class).getNombre();
                    //    nombre.setText(nombreUsuarioLogeado);
                        btnEnviar.setEnabled(true);
                   //     Toast.makeText(Activity_Mensajeria.this , "USUARIO :  " + usuario.getValue(Usuario.class).getNombre(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled( DatabaseError databaseError) {

                }
            });

        }else{
            gotoLoginActivityAndFinish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SEND_IMAGE_OK && resultCode == RESULT_OK){
            Uri uri = data.getData();
            storageReference  = storage.getReference("ImagesKahooChat");
            final StorageReference fotoReferencia = storageReference.child(uri.getLastPathSegment());
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
                        Uri uriTask = task.getResult();
                        Mensaje mesajeToSend = new Mensaje();
                        mesajeToSend.setMensaje(" te ha enviado una foto");
                        mesajeToSend.setFotoUri(uriTask.toString());
                        mesajeToSend.setContainsPhoto(true);
                        mesajeToSend.setSenderKey(UsuarioDAO.getInstance().getUserKey());
                        databaseReference.push().setValue(mesajeToSend);
                    }
                }
            });



        }/*else if(requestCode  == SEND_FOTO_PERFIL_OK && resultCode == RESULT_OK){
            Uri uri = data.getData();
            storageReference  = storage.getReference("FotosPerfiles");
            final StorageReference fotoReferencia = storageReference.child(uri.getLastPathSegment());

            fotoReferencia.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fotoReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri u = uri ;
                            fotoPerfilUri = u.toString();
                            MensajeEnviar msj = new MensajeEnviar(nombreUsuarioLogeado + " Ha actualizado su foto de perfil." ,
                                    u.toString(),
                                    nombreUsuarioLogeado,
                                    fotoPerfilUri,
                                    TYPE_IMG,
                                    ServerValue.TIMESTAMP);
                            databaseReference.push().setValue(msj);
                            Glide.with(Activity_Mensajeria.this).load(u.toString()).into(fotoPerfil);
                        }
                    });
                }
            });
        }*/

    }

    private void gotoLoginActivityAndFinish(){
        startActivity(new Intent(Activity_Mensajeria.this, Activity_Login.class));
        finish();
    }
}
package com.afl.kahootchat.ACTIVITIES;

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

import com.afl.kahootchat.ENTITIES.Mensaje;
import com.afl.kahootchat.ENTITIES.MensajeEnviar;
import com.afl.kahootchat.ENTITIES.MensajeRecibir;
import com.afl.kahootchat.ENTITIES.Usuario;
import com.afl.kahootchat.ADAPTERS.Mensajeria_Adapter;
import com.afl.kahootchat.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Mensajeria extends AppCompatActivity {

    private final Integer SEND_IMAGE_OK = 1 ;
    private final Integer SEND_FOTO_PERFIL_OK = 2 ;

    private final String TYPE_MENSAJE = "1";
    private final String TYPE_IMG = "2";

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

    private String fotoPerfilUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        nombre = (TextView) findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);
        btnLogOut = (Button) findViewById(R.id.btnlogOut);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("SalaKahooChat");
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new Mensajeria_Adapter(this);
        LinearLayoutManager linearMensaje = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(linearMensaje);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mensaje msj = new MensajeEnviar(txtMensaje.getText().toString() ,
                               nombreUsuarioLogeado,
                               fotoPerfilUri ,
                                 TYPE_MENSAJE ,
                                 ServerValue.TIMESTAMP);
           databaseReference.push().setValue(msj);
           txtMensaje.setText("");

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
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
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
                        nombreUsuarioLogeado = usuario.getValue(Usuario.class).getNombre();
                        nombre.setText(nombreUsuarioLogeado);
                        btnEnviar.setEnabled(true);
                        Toast.makeText(Activity_Mensajeria.this , "USUARIO :  " + usuario.getValue(Usuario.class).getNombre(), Toast.LENGTH_SHORT).show();
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
            fotoReferencia.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fotoReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri u = uri ;
                            MensajeEnviar msj = new MensajeEnviar(nombreUsuarioLogeado + " ha enviado una imagen" ,
                                    u.toString(),
                                    nombreUsuarioLogeado,
                                    fotoPerfilUri,
                                    TYPE_IMG,
                                    ServerValue.TIMESTAMP);
                            databaseReference.push().setValue(msj);
                        }
                    });
                }
            });

        }else if(requestCode  == SEND_FOTO_PERFIL_OK && resultCode == RESULT_OK){
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
        }

    }

    private void gotoLoginActivityAndFinish(){
        startActivity(new Intent(Activity_Mensajeria.this, Activity_Login.class));
        finish();
    }
}
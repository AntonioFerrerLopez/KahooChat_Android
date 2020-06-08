package com.afl.kahootchat;

import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private final Integer SEND_IMAGE_OK = 1 ;
    private final Integer SEND_FOTO_PERFIL_OK = 2 ;

    private final String TYPE_MENSAJE = "1";
    private final String TYPE_IMG = "2";


    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje ;
    private Button btnEnviar;
    private ImageButton btnEnviarFoto;
    private AdapterMensajes adapter;
    private FirebaseDatabase database ;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String fotoPerfilUri = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        nombre = (TextView) findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("SalaKahooChat");
        storage = FirebaseStorage.getInstance();


        adapter = new AdapterMensajes(this);
        LinearLayoutManager linearMensaje = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(linearMensaje);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mensaje msj = new MensajeEnviar(txtMensaje.getText().toString() ,
                                        nombre.getText().toString(),
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
                            MensajeEnviar msj = new MensajeEnviar("Imagen Enviada" ,
                                    u.toString(),
                                    nombre.getText().toString(),
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
                            MensajeEnviar msj = new MensajeEnviar("Foto de perfil Actualizada" ,
                                    u.toString(),
                                    nombre.getText().toString(),
                                    fotoPerfilUri,
                                    TYPE_IMG,
                                    ServerValue.TIMESTAMP);
                            databaseReference.push().setValue(msj);
                            Glide.with(MainActivity.this).load(u.toString()).into(fotoPerfil);
                        }
                    });
                }
            });
        }

    }
}
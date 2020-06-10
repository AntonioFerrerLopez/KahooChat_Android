package com.afl.kahootchat.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afl.kahootchat.ENTITIES.DAO.UsuarioDAO;
import com.afl.kahootchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Login extends AppCompatActivity {

    private EditText emailLogin ;
    private EditText passwordLogin;
    private Button btnLogin ;
    private Button btnGotoRegistro;

    private FirebaseAuth  mAuth ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);

    emailLogin = (EditText) findViewById(R.id.idLoginEmail);
    passwordLogin = (EditText) findViewById(R.id.idLoginPassword);
    btnLogin = (Button) findViewById(R.id.btnLogin);
    btnGotoRegistro = (Button) findViewById(R.id.btnGotoRegistrar);

    mAuth = FirebaseAuth.getInstance();

        btnGotoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Login.this , Activity_Registro.class));
            }
        });

    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(emailLogin.getText().toString().length() >= 3  && passwordLogin.getText().toString().length() >= 8 ){
                mAuth.signInWithEmailAndPassword(emailLogin.getText().toString(), passwordLogin.getText().toString())
                        .addOnCompleteListener(Activity_Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Activity_Login.this, "LOGIN CORRECTO.", Toast.LENGTH_SHORT).show();
                                    gotoMainActivity();
                                }else{
                                    Toast.makeText(Activity_Login.this, "EMAIL O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(Activity_Login.this, "FALTAN DATOS ", Toast.LENGTH_SHORT).show();
            }

        }
    });


    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(Activity_Login.this, "USUARIO LOGEADO", Toast.LENGTH_SHORT).show();
            gotoMainActivity();
        }
    }

    private void  gotoMainActivity(){
        //startActivity(new Intent(Activity_Login.this , Activity_Mensajeria.class));
       // finish();
    }
}

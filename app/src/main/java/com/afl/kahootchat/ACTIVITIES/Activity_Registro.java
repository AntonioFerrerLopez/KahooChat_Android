package com.afl.kahootchat.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afl.kahootchat.ENTITIES.Usuario;
import com.afl.kahootchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Registro extends AppCompatActivity {

    private static final int MIN_CHARACTERS = 8;
    private static final int MAX_CHARACTERS = 16;
    private static final int MIN_USER_NAME_LENGTH = 3;
    private EditText usuarioRegistro ;
    private EditText emailRegistro ;
    private EditText passwordRegistro ;
    private EditText passwordRepetirRegistro ;
    private Button btnRegistro;
    private Button btnRegistroToLogin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database ;
    private DatabaseReference referenceUsuarios ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioRegistro = (EditText) findViewById(R.id.idRegistroUsuario);
        emailRegistro = (EditText) findViewById(R.id.idRegistroEmail);
        passwordRegistro = (EditText) findViewById(R.id.idRegistroPassword);
        passwordRepetirRegistro = (EditText) findViewById(R.id.idRegistroPasswordRepetir);
        btnRegistro = (Button) findViewById(R.id.btnRegistrar);
        btnRegistroToLogin = (Button) findViewById(R.id.btnRegisterToLogin);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        referenceUsuarios = database.getReference("Usuarios");

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isValidEmail(emailRegistro.getText().toString()) && isSamePassword()  && nameExists() ){
                mAuth.createUserWithEmailAndPassword(emailRegistro.getText().toString(), passwordRegistro.getText().toString())
                    .addOnCompleteListener(Activity_Registro.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity_Registro.this, "USUARIO " + usuarioRegistro.getText().toString() + " registrado correctamente", Toast.LENGTH_SHORT).show();
                            referenceUsuarios.push().setValue(new Usuario(usuarioRegistro.getText().toString(),emailRegistro.getText().toString()));
                            clearForm();
                            finish();
                        } else {
                            Toast.makeText(Activity_Registro.this, "FALLO AL REGISTRAR EL USUARIO", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });

            }
            }
        });

        btnRegistroToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Registro.this , Activity_Login.class));
            }
        });
    }

    private void clearForm() {
        usuarioRegistro.setText("");
        emailRegistro.setText("");
        passwordRegistro.setText("");
        passwordRepetirRegistro.setText("");
    }

    private final boolean nameExists(){
        boolean result = false;
        if(!TextUtils.isEmpty(usuarioRegistro.getText().toString()) && usuarioRegistro.getText().toString().length() >= MIN_USER_NAME_LENGTH){
            result = true;
        }else{
            Toast.makeText(Activity_Registro.this, "REVISE el nombre de usuario", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private final boolean isValidEmail(String email){
        boolean result = false;
        if(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            result = true;
        }else{
            Toast.makeText(Activity_Registro.this, "REVISE el email", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    
    private final boolean isSamePassword(){
        boolean result = false;
        if(passwordRegistro.getText().toString().equals(passwordRepetirRegistro.getText().toString()) &&
                passwordRegistro.getText().toString().length() >= MIN_CHARACTERS &&
                passwordRegistro.getText().toString().length() <= MAX_CHARACTERS){
            result = true;
        }else{
            Toast.makeText(Activity_Registro.this, "LA contraseña debe contener entre 8 y 16 catacteres y deben coincidir ambas", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

}

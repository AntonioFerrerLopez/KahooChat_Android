package com.afl.kahootchat.ACTIVITIES;

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
import com.afl.kahootchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Registro extends AppCompatActivity {

    private static final int MIN_CHARACTERS = 8;
    private static final int MAX_CHARACTERS = 16;
    private EditText usuarioRegistro ;
    private EditText emailRegistro ;
    private EditText passwordRegistro ;
    private EditText passwordRepetirRegistro ;
    private Button btnRegistro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioRegistro = (EditText) findViewById(R.id.idRegistroUsuario);
        emailRegistro = (EditText) findViewById(R.id.idRegistroEmail);
        passwordRegistro = (EditText) findViewById(R.id.idRegistroPassword);
        passwordRepetirRegistro = (EditText) findViewById(R.id.idRegistroPasswordRepetir);
        btnRegistro = (Button) findViewById(R.id.btnRegistrar);

        mAuth = FirebaseAuth.getInstance();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(emailRegistro.getText().toString()) && isSamePassword() ){
                    mAuth.createUserWithEmailAndPassword(emailRegistro.getText().toString(), passwordRegistro.getText().toString())
                            .addOnCompleteListener(Activity_Registro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        clearForm();
                                        Toast.makeText(Activity_Registro.this, "USUARIO " + usuarioRegistro.getText().toString() + " registrado correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Activity_Registro.this, "FALLO AL REGISTRAR EL USUARIO", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }else{
                    Toast.makeText(Activity_Registro.this, "LOS DATOS NO SON VALIDOS", Toast.LENGTH_SHORT).show();

                }
            }
        });



    }

    private void clearForm() {
        usuarioRegistro.setText("");
        emailRegistro.setText("");
        passwordRegistro.setText("");
        passwordRepetirRegistro.setText("");
    }

    private final boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches(); 
    }
    
    private final boolean isSamePassword(){
        return passwordRegistro.getText().toString().equals(passwordRepetirRegistro.getText().toString()) &&
                passwordRegistro.getText().toString().length() >= MIN_CHARACTERS &&
                passwordRegistro.getText().toString().length() <= MAX_CHARACTERS ;
    }

}

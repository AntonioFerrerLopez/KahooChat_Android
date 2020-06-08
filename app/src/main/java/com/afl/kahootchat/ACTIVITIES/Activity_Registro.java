package com.afl.kahootchat.ACTIVITIES;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.afl.kahootchat.R;

public class Activity_Registro extends AppCompatActivity {

    private EditText usuarioRegistro ;
    private EditText emailRegistro ;
    private EditText passwordRegistro ;
    private EditText passwordRepetirRegistro ;
    private Button btnRegistro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioRegistro = (EditText) findViewById(R.id.idRegistroUsuario);
        emailRegistro = (EditText) findViewById(R.id.idRegistroEmail);
        passwordRegistro = (EditText) findViewById(R.id.idRegistroPassword);
        passwordRepetirRegistro = (EditText) findViewById(R.id.idRegistroPasswordRepetir);


    }
}

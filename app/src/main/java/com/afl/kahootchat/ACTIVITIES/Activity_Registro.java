package com.afl.kahootchat.ACTIVITIES;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afl.kahootchat.ENTITIES.DAO.UsuarioDAO;
import com.afl.kahootchat.ENTITIES.MODELS.Usuario;
import com.afl.kahootchat.HELPERS.Constants;
import com.afl.kahootchat.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Registro extends AppCompatActivity {

    private static final int MIN_CHARACTERS = 8;
    private static final int MAX_CHARACTERS = 16;
    private static final int MIN_USER_NAME_LENGTH = 3;
    private static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 200;

    private CircleImageView imgUser;
    private ImagePicker imgPicker;
    private Uri fotoPerfilUri;
    private EditText birthDate;
    private Long birthDateSelected;
    private RadioGroup rgGender;
    private RadioButton menSelected;
    private RadioButton womanSelected;
    private EditText usuarioRegistro ;
    private EditText emailRegistro ;
    private EditText passwordRegistro ;
    private EditText passwordRepetirRegistro ;

    private Button btnRegistro;
    private Button btnRegistroToLogin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database ;

    private CameraImagePicker cameraPicker;
    private String pickerPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        requestAppPermissions();
        imgUser =  findViewById(R.id.imgUser);
        imgPicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);

        birthDate =  findViewById(R.id.birthDate);
        rgGender =  findViewById(R.id.rgGender);
        usuarioRegistro =  findViewById(R.id.idRegistroUsuario);
        emailRegistro = findViewById(R.id.idRegistroEmail);
        passwordRegistro = findViewById(R.id.idRegistroPassword);
        passwordRepetirRegistro =  findViewById(R.id.idRegistroPasswordRepetir);
        menSelected = findViewById(R.id.menSelected);
        womanSelected = findViewById(R.id.womanSelected);
        btnRegistro =  findViewById(R.id.btnRegistrar);
        btnRegistroToLogin = findViewById(R.id.btnRegisterToLogin);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        imgPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path = list.get(Constants.POSITION_ZERO).getOriginalPath();
                    fotoPerfilUri = Uri.fromFile(new File(path));
                    imgUser.setImageURI(fotoPerfilUri);
                }
            }

            @Override
            public void onError(String errorMsj) {
                Toast.makeText(Activity_Registro.this, "Error: " + errorMsj, Toast.LENGTH_SHORT).show();
            }
        });
        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path = list.get(Constants.POSITION_ZERO).getOriginalPath();
                fotoPerfilUri = Uri.fromFile(new File(path));
                imgUser.setImageURI(fotoPerfilUri);
            }

            @Override
            public void onError(String errorCamMsj) {
                Toast.makeText(Activity_Registro.this, "Error: " + errorCamMsj, Toast.LENGTH_SHORT).show();
            }
        });
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] typeOfSourcesList = {"Galeria", "Camara"};
                AlertDialog.Builder dialogImageSelector = new AlertDialog.Builder(Activity_Registro.this);
                dialogImageSelector.setTitle("Foto de perfil");
                dialogImageSelector.setItems(typeOfSourcesList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case Constants.GALLERY_SELECTED:
                                imgPicker.pickImage();
                                break;
                            case Constants.CAMERA_SELECTED:
                                pickerPath = cameraPicker.pickImage();
                                break;
                        }
                    }
                });
                AlertDialog dialogBuilded = dialogImageSelector.create();
                dialogBuilded.show();
            }
        });

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            LocalDate now = LocalDate.now();
                DatePickerDialog datePicker = new DatePickerDialog(Activity_Registro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        LocalDate dateUserSelected = LocalDate.of(year,month,dayOfMonth);
                        DateTimeFormatter formatToLong = DateTimeFormatter.ofPattern("ddMMYYYY");
                        DateTimeFormatter formatToUserView = DateTimeFormatter.ofPattern("dd-MM-YYYY");

                        if( year >= (LocalDate.now().getYear() - Constants.ADULT_AGE) ){
                            Toast.makeText(Activity_Registro.this, "Debes ser mayor de edad para registrarte", Toast.LENGTH_SHORT).show();
                        }else{
                            if(month <= LocalDate.now().getMonthValue() && dayOfMonth <= LocalDate.now().getDayOfMonth()){
                                birthDate.setText(formatToUserView.format(dateUserSelected));
                                birthDateSelected = Long.parseLong(formatToLong.format(dateUserSelected));
                            }else{
                                Toast.makeText(Activity_Registro.this, "Fecha no válida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, now.getYear() , now.getMonthValue() , now.getDayOfMonth());
                datePicker.show();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isValidEmail(emailRegistro.getText().toString()) && isSamePassword()  && nameExists() && !birthDate.getText().equals("") ){
                mAuth.createUserWithEmailAndPassword(emailRegistro.getText().toString(), passwordRegistro.getText().toString())
                    .addOnCompleteListener(Activity_Registro.this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(fotoPerfilUri != null ){
                                UsuarioDAO.getInstance().updatePhotoByUri(fotoPerfilUri, new UsuarioDAO.PhotoUriSender() {
                                    @Override
                                    public void sendUriPhotoString(String uri) {
                                        setUserProfile(uri);
                                    }
                                });
                            }else{
                                setUserProfile(Constants.URI_ANONIMOUS_USER_IMG);
                        }

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

        Glide.with(this).load(Constants.URI_ANONIMOUS_USER_IMG).into(imgUser);
    }

    private String genderIsSelected() {
    String genderSelected = "men";
    if(menSelected.isChecked()){
        genderSelected = "men";
    }else{
        genderSelected = "woman";
    }
    return genderSelected;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){
            imgPicker.submit(data);
        }else if(requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK){
            cameraPicker.reinitialize(pickerPath);
            cameraPicker.submit(data);
        }
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

    private void setUserProfile(String photoProfileUri){
        Toast.makeText(Activity_Registro.this, "USUARIO " + usuarioRegistro.getText().toString() + " registrado correctamente", Toast.LENGTH_SHORT).show();
        Usuario newUser = new Usuario();
        newUser.setNombre(usuarioRegistro.getText().toString());
        newUser.setEmail(emailRegistro.getText().toString());
        newUser.setBirthDate(birthDateSelected);
        newUser.setGender(genderIsSelected());
        newUser.setFotoPerfilUri(photoProfileUri);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference reference =  database.getReference("Usuarios").child(currentUser.getUid());
        reference.setValue(newUser);
        clearForm();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}

package tech.abralica.clinicalaluzapp.ui.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Especialidad;

public class RegistrarEspecialidadActivity extends AppCompatActivity {

    Uri uriImagen;
    String urlDownloadImage;

    ImageView ivImagen;
    Button bElegir, bRegistrar;
    TextInputLayout tilNombre, tilDesc;
    ProgressBar progressBar;
    private DatabaseReference referenceespecialidad;
    StorageReference storageRef;
    CollectionReference espRef;
    private ProgressDialog progressDialog;
    TextInputEditText tenombre,tedescripcion;

    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrar_especialidad);
        ivImagen = findViewById(R.id.fe_iv_imagen);
        bElegir = findViewById(R.id.fe_mb_elegir);
        bRegistrar = findViewById(R.id.fe_mb_registrar);
        tilNombre = findViewById(R.id.fe_til_nombre);
        tilDesc = findViewById(R.id.fe_til_desc);
        progressBar = findViewById(R.id.fe_pb_barra);


        tenombre =findViewById(R.id.fe_tiet_nombre);

        tedescripcion=findViewById(R.id.fe_tiet_desc);

        bElegir.setOnClickListener(v -> abrirGaleria());
        bRegistrar.setOnClickListener(v -> uploadFile());

        espRef = FirebaseFirestore.getInstance().collection("especialidades");
        storageRef = FirebaseStorage.getInstance().getReference().child("imagenes");
        progressBar = new ProgressBar(this);
        referenceespecialidad= FirebaseDatabase.getInstance().getReference("Especialidades");
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                uriImagen = Objects.requireNonNull(result.getData()).getData();
                ivImagen.setImageURI(uriImagen);
            }
        });


    }


    private void abrirGaleria() {
        if (ContextCompat.checkSelfPermission(RegistrarEspecialidadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(RegistrarEspecialidadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activityResultLauncher.launch(galeria);
    }

    private void uploadFile() {
        if (uriImagen != null) {

            progressDialog=new ProgressDialog(RegistrarEspecialidadActivity.this);
            progressDialog.setTitle("Agregando Cliente");
            progressDialog.setMessage("cargando...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference fileRef = storageRef.child(getRandomHexString() + "." + getFileExtension(uriImagen));
            Toast.makeText(this, "subior", Toast.LENGTH_SHORT).show();
            fileRef.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0), 500);
                Toast.makeText(RegistrarEspecialidadActivity.this, "Se ha subido la imagen", Toast.LENGTH_SHORT).show();

                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    urlDownloadImage = uri.toString();
                    String  nombre=tenombre.getText().toString();
                    String descri= tedescripcion.getText().toString();

                    registrarEspecialidad(nombre,descri);

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(RegistrarEspecialidadActivity.this, "Fallo", Toast.LENGTH_SHORT).show();
                    }
                });

            }).addOnFailureListener(e -> Toast.makeText(RegistrarEspecialidadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(RegistrarEspecialidadActivity.this, "No se seleccionó una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarEspecialidad(String nonbre,String decripo) {




        String key =referenceespecialidad.push().getKey();
        Especialidad o =new Especialidad(key,nonbre,decripo,urlDownloadImage);

        referenceespecialidad.child(key).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistrarEspecialidadActivity.this, "Agregado", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrarEspecialidadActivity.this, "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private String getRandomHexString() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 8) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.substring(0, 8);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
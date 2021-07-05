package tech.abralica.clinicalaluzapp.ui.paciente;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.ui.medico.EditarPerfil;

public class EditarPerfilPacienteActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher;
    private ProgressDialog progressDialog;
    EditText etnombreprofesor,etapellidoprofeseor,etnumeroprofesor;
    Button btnregistrrar,btnabrigaleria,btnfinish;
    Uri uriImagen;
    ImageView imgfoto;
    StorageReference storageRef;
    private StorageReference mStorageRef;
    public FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    private DatabaseReference referenceprofesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_paciente);

        etnombreprofesor=(EditText)findViewById(R.id.idnombresprofesor1);
        etapellidoprofeseor=(EditText)findViewById(R.id.idapellidoprofesor1);
        etnumeroprofesor=(EditText)findViewById(R.id.idtelefonoprofesor1);
        imgfoto=(ImageView) findViewById(R.id.idimgfotoperfil);
        btnabrigaleria=(Button)findViewById(R.id.idbtnfotoperfil);

        progressBar = findViewById(R.id.fe_pb_barra);
        btnregistrrar=(Button)findViewById(R.id.idbotonregistrardatos);
        btnfinish=(Button)findViewById(R.id.btnfinish);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String user_id = mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference().child("imagenes");
        referenceprofesor = FirebaseDatabase.getInstance().getReference("Paciente").child(user_id);

        if (currentUser != null) {
            // final String user_uID = mAuth.getCurrentUser().getUid();
            //final String correo = mAuth.getCurrentUser().getEmail();

            referenceprofesor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //    String name = dataSnapshot.child("nombre").getValue().toString();
                    String urlimageperfil = dataSnapshot.child("fotoPerfil").getValue().toString();
                    String nombreprofe = dataSnapshot.child("nombres").getValue().toString();
                    String apellidoprofe = dataSnapshot.child("apellidos").getValue().toString();
                    String numeroprofe = dataSnapshot.child("celular").getValue().toString();
                    etnombreprofesor.setText(nombreprofe);
                    etapellidoprofeseor.setText(apellidoprofe);
                    etnumeroprofesor.setText(numeroprofe);
                    try {
                        if (urlimageperfil.equals("default_image")){
                            //imgfoto.setImageDrawable(getDrawable(R.drawable.default_profile_image));
                            imgfoto.setImageResource(R.drawable.default_profile_image);
                        }else{
                            Picasso.get().load(urlimageperfil).fit().centerCrop().into(imgfoto);
                        }
                    }catch (Exception ex){
                        Toast.makeText(EditarPerfilPacienteActivity.this, "erro 3", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("error  ","error 4");
                }
            });
        }

        btnabrigaleria.setOnClickListener(v -> abrirGaleria());
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                uriImagen = Objects.requireNonNull(result.getData()).getData();
                imgfoto.setImageURI(uriImagen);
                uploadFile();
            }
        });

        btnregistrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre=etnombreprofesor.getText().toString();
                String apellido=etapellidoprofeseor.getText().toString();
                String numero=etnumeroprofesor.getText().toString();
                modificardatos(nombre,apellido,numero);
            }
        });
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void abrirGaleria() {
        if (ContextCompat.checkSelfPermission(EditarPerfilPacienteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(EditarPerfilPacienteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activityResultLauncher.launch(galeria);
    }
    private void modificardatos(String nombre, String apellido, String numero) {
        if (TextUtils.isEmpty(nombre)){
            etnumeroprofesor.setError("campo requerido");
        }
        if (TextUtils.isEmpty(apellido)){
            etapellidoprofeseor.setError("campo requerido");
        }
        if (TextUtils.isEmpty(numero)){
            etnumeroprofesor.setError("campo requerido");
        }
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Actualizando ");
            progressDialog.setMessage("Espere un momento...");
            progressDialog.show();

            referenceprofesor.child("nombres").setValue(nombre);
            referenceprofesor.child("apellidos").setValue(apellido);
            referenceprofesor.child("celular").setValue(numero).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //  Toast.makeText(EditarPerfil.this, "Perfil Modificado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        String mensaje="Actualizado Datos";
                        //      dialogomensaje(mensaje);
                        Toast.makeText(EditarPerfilPacienteActivity.this, "ACtualizado", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditarPerfilPacienteActivity.this, "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void uploadFile() {
        if (uriImagen != null) {

            StorageReference fileRef = storageRef.child(uriImagen.getLastPathSegment());

            fileRef.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0), 500);
                Toast.makeText(EditarPerfilPacienteActivity.this, "Se ha subido la imagen", Toast.LENGTH_SHORT).show();

                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    referenceprofesor.child("fotoPerfil").setValue(uri.toString());
                    Toast.makeText(EditarPerfilPacienteActivity.this, "Foto Subida", Toast.LENGTH_SHORT).show();
                    //  progressDialog.dismiss();

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(EditarPerfilPacienteActivity.this, "Fallo", Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();
                    }
                });

            }).addOnFailureListener(e -> Toast.makeText(EditarPerfilPacienteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(EditarPerfilPacienteActivity.this, "No se seleccionó una imagen", Toast.LENGTH_SHORT).show();
            //  progressDialog.dismiss();
        }
    }

}
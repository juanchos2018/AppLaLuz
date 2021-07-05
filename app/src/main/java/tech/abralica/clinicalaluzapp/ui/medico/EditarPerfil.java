package tech.abralica.clinicalaluzapp.ui.medico;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.ui.actiity.ReservarCitaActivity;
import tech.abralica.clinicalaluzapp.ui.admin.RegistrarEspecialidadActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditarPerfil extends AppCompatActivity {


    EditText etnombreprofesor,etapellidoprofeseor,etnumeroprofesor;
    Button btnregistrrar,btnabrigaleria;
    ImageView imgfoto;
    private String path;//almacena la ruta de la imagen
    private final static int GALLERY_PICK_CODE = 1;
    Bitmap thumb_Bitmap = null;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    private DatabaseReference referenceprofesor;
    private FirebaseAuth mAuth;
    StorageReference storageRef;
    private StorageReference storageReference;
    private StorageReference thumb_image_ref;
    private static final int COD_FOTO = 20;
    private final int MIS_PERMISOS = 100;
    private StorageReference mStorageRef;
    File fileImagen;
    Bitmap bitmap;
    public FirebaseUser currentUser;
    Uri uri;
    private final int frames = 9;
    private int currentAnimationFrame = 0;
    private final int PICKER=1;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_ARCHIVO=15;
    String  profile_download_url,profile_thumb_download_url;
    private LottieAnimationView animationView;
    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    String correoprofe;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uriImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        etnombreprofesor=(EditText)findViewById(R.id.idnombresprofesor1);
        etapellidoprofeseor=(EditText)findViewById(R.id.idapellidoprofesor1);
        etnumeroprofesor=(EditText)findViewById(R.id.idtelefonoprofesor1);
        imgfoto=(ImageView) findViewById(R.id.idimgfotoperfil);

        btnabrigaleria=(Button)findViewById(R.id.idbtnfotoperfil);
        btnregistrrar=(Button)findViewById(R.id.idbotonregistrardatos);
        progressBar = findViewById(R.id.fe_pb_barra);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        correoprofe=mAuth.getCurrentUser().getEmail();
        referenceprofesor = FirebaseDatabase.getInstance().getReference().child("Medico").child(user_id);


        ///mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("imagenes");
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
                        Toast.makeText(EditarPerfil.this, "erro 3", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("error  ","error 4");
                }
            });
        }
        //  if(solicitaPermisosVersionesSuperiores()){
        //      btnabrigaleria.setEnabled(true);
        //  }else{
        //      btnabrigaleria.setEnabled(false);
        //  }
        if (solicitaPermisosVersionesSuperiores()){

        }
        // btnabrigaleria.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {

        //         Intent intent=new Intent(Intent.ACTION_PICK,
        //         MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //         intent.setType("image/");
        //         startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);// 10
        //     }
        // });

        btnabrigaleria.setOnClickListener(v -> abrirGaleria());

        btnregistrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre=etnombreprofesor.getText().toString();
                String apellido=etapellidoprofeseor.getText().toString();
                String numero=etnumeroprofesor.getText().toString();
                modificardatos(nombre,apellido,numero);
            }
        });


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                uriImagen = Objects.requireNonNull(result.getData()).getData();
                imgfoto.setImageURI(uriImagen);
                uploadFile();
            }
        });


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
                        Toast.makeText(EditarPerfil.this, "ACtualizado", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditarPerfil.this, "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void abrirGaleria() {
        if (ContextCompat.checkSelfPermission(EditarPerfil.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(EditarPerfil.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activityResultLauncher.launch(galeria);
    }


    private void uploadFile() {
        if (uriImagen != null) {

         //  progressDialog=new ProgressDialog(EditarPerfil.this);
         //  progressDialog.setTitle("Agregando imagen");
         //  progressDialog.setMessage("cargando...");
         //  progressDialog.show();
         //  progressDialog.setCancelable(false);

            StorageReference fileRef = storageRef.child(uriImagen.getLastPathSegment());

            fileRef.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0), 500);
                Toast.makeText(EditarPerfil.this, "Se ha subido la imagen", Toast.LENGTH_SHORT).show();

                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    referenceprofesor.child("fotoPerfil").setValue(uri.toString());
                    Toast.makeText(EditarPerfil.this, "Foto Subida", Toast.LENGTH_SHORT).show();
                  //  progressDialog.dismiss();

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(EditarPerfil.this, "Fallo", Toast.LENGTH_SHORT).show();
                      //  progressDialog.dismiss();
                    }
                });

            }).addOnFailureListener(e -> Toast.makeText(EditarPerfil.this, e.getMessage(), Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(EditarPerfil.this, "No se seleccionó una imagen", Toast.LENGTH_SHORT).show();
          //  progressDialog.dismiss();
        }
    }




    private void DialogoOk(String mensaje){

        builder1 = new AlertDialog.Builder(EditarPerfil.this);
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(EditarPerfil.this).inflate(R.layout.dialogo_ok, null);
        animationView = v.findViewById(R.id.animation_viewcheck);
        resetAnimationView();
        animationView.playAnimation();
        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText(mensaje);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }

    private void resetAnimationView() {
        currentAnimationFrame = 0;
        animationView.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return null;
                    }
                }
        );
    }

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getBaseContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&getBaseContext().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getBaseContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(getApplicationContext(),"Permisos concedidos",Toast.LENGTH_SHORT);
                btnabrigaleria.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getBaseContext());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getBaseContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(),"Los permisos no fueron concedidos",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

}
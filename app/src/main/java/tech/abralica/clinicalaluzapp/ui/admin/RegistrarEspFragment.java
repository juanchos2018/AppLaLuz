package tech.abralica.clinicalaluzapp.ui.admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.StartActivity;
import tech.abralica.clinicalaluzapp.models.Especialidad;
import tech.abralica.clinicalaluzapp.models.Medico;
import tech.abralica.clinicalaluzapp.ui.actiity.MedicosActivity;
import tech.abralica.clinicalaluzapp.ui.paciente.ReservarEspFragment;

import static android.app.Activity.RESULT_OK;

public class RegistrarEspFragment extends Fragment {

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


    RecyclerView recyclerView;

    EditText filtro;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        espRef = FirebaseFirestore.getInstance().collection("especialidades");
        storageRef = FirebaseStorage.getInstance().getReference().child("imagenes");
        progressBar = new ProgressBar(getContext());
        referenceespecialidad= FirebaseDatabase.getInstance().getReference("Especialidades");

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                uriImagen = Objects.requireNonNull(result.getData()).getData();
                ivImagen.setImageURI(uriImagen);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reg_especialidad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FloatingActionButton fab = requireView().findViewById(R.id.fab2);

        recyclerView = view.findViewById(R.id.recylerespecalidad);
        recyclerView.setHasFixedSize(true);
        init();
        referenceespecialidad= FirebaseDatabase.getInstance().getReference("Especialidades");

        filtro=(EditText)view.findViewById(R.id.ttxfiltrarespe);


        filtro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPeopleProfile(filtro.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

      /*  ivImagen = requireView().findViewById(R.id.fe_iv_imagen);
        bElegir = requireView().findViewById(R.id.fe_mb_elegir);
        bRegistrar = requireView().findViewById(R.id.fe_mb_registrar);
        tilNombre = requireView().findViewById(R.id.fe_til_nombre);
        tilDesc = requireView().findViewById(R.id.fe_til_desc);
        progressBar = requireView().findViewById(R.id.fe_pb_barra);
        tenombre =requireView().findViewById(R.id.fe_tiet_nombre);
        tedescripcion=requireView().findViewById(R.id.fe_tiet_desc);
        bElegir.setOnClickListener(v -> abrirGaleria());
        bRegistrar.setOnClickListener(v -> uploadFile()); */

    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AggregarESepcialidad();
        }
    });
    }

    private void searchPeopleProfile(final String searchString) {
        final Query searchQuery = referenceespecialidad.orderByChild("nombre")
                .startAt(searchString).endAt(searchString + "\uf8ff");
        //final Query searchQuery = peoplesDatabaseReference.orderByChild("search_name").equalTo(searchString);

        FirebaseRecyclerOptions<Especialidad> recyclerOptions = new FirebaseRecyclerOptions.Builder<Especialidad>()
                .setQuery(searchQuery, Especialidad.class)
                .build();
        final Context context = getContext();
        FirebaseRecyclerAdapter<Especialidad, Items> adapter = new FirebaseRecyclerAdapter<Especialidad, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items holder, final int position, @NonNull final Especialidad model) {

                holder.especialidadNombre.setText(model.getNombre());
                holder.especialidadDesc.setText(model.getDescripcion());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String idespecialidad=model.getIdespecialidad();
                        final String nombrespecialidad=model.getNombre();
                        Intent intent = new Intent(getContext(), HorasCitasActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("nombrespecialidad",nombrespecialidad);
                        bundle.putString("idespecialidad",idespecialidad);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
            }
            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.especialidad_card, parent, false);
                return new Items(vista);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }


    private void AggregarESepcialidad() {
        startActivity(new Intent(getContext(),RegistrarEspecialidadActivity.class));
    }

    private void abrirGaleria() {
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activityResultLauncher.launch(galeria);
    }

    private void uploadFile() {
        if (uriImagen != null) {
            StorageReference fileRef = storageRef.child(getRandomHexString() + "." + getFileExtension(uriImagen));

            fileRef.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0), 500);
                Toast.makeText(getContext(), "Se ha subido la imagen", Toast.LENGTH_SHORT).show();

                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    urlDownloadImage = uri.toString();
                String  nombre=tenombre.getText().toString();
                String descri= tedescripcion.getText().toString();

                    registrarEspecialidad(nombre,descri);

                }).addOnFailureListener(e -> Toast.makeText(getContext(), "No se pudo obtener el enlace", Toast.LENGTH_SHORT).show());

            }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(getContext(), "No se seleccionó una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarEspecialidad(String nonbre,String decripo) {


        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Agregando Cliente");
        progressDialog.setMessage("cargando...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String key =referenceespecialidad.push().getKey();
        Especialidad o =new Especialidad(key,nonbre,decripo,urlDownloadImage);

        referenceespecialidad.child(key).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

     //   Especialidad especialidad = new Especialidad();
     //   especialidad.setNombre(Objects.requireNonNull(tilNombre.getEditText()).getText().toString());
      //  especialidad.setDescripcion(Objects.requireNonNull(tilDesc.getEditText()).getText().toString());
     //   //especialidad.setUrlImagen(urlDownloadImage);

      //  espRef.add(especialidad).addOnSuccessListener(e -> Toast.makeText(getContext(), "Se registró la " + "especialidad", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getContext(), "No se pudo registrar la especialidad: " + e.getMessage(), Toast.LENGTH_SHORT).show());


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Especialidad> recyclerOptions = new FirebaseRecyclerOptions.Builder<Especialidad>()
                .setQuery(referenceespecialidad, Especialidad.class).build();
        FirebaseRecyclerAdapter<Especialidad, Items> adapter =new FirebaseRecyclerAdapter<Especialidad, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull Especialidad tutores) {
                final String key = getRef(i).getKey();
                referenceespecialidad.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String nombre=dataSnapshot.child("nombre").getValue().toString();
                            final String descripcion=dataSnapshot.child("descripcion").getValue().toString();
                            final String idespecialidad=dataSnapshot.child("idespecialidad").getValue().toString();
                            String rutafoto =   dataSnapshot.child("urlImagen").getValue().toString();

                            items.especialidadNombre.setText(nombre);
                            items.especialidadDesc.setText(descripcion);
                            Picasso.get().load(rutafoto).fit().centerCrop().into(items.especialidadImagen);
                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), HorasCitasActivity.class);
                                    Bundle bundle= new Bundle();
                                    bundle.putString("idespecialidad",idespecialidad);
                                    bundle.putString("nombrespecialidad",nombre);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.especialidad_card, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    public  static class Items extends RecyclerView.ViewHolder{
        ImageView especialidadImagen;
        TextView especialidadNombre;
        TextView especialidadDesc;

        public Items(@NonNull View itemView) {
            super(itemView);
            especialidadImagen = itemView.findViewById(R.id.especialidad_imagen);
            especialidadNombre = itemView.findViewById(R.id.especialidad_nombre);
            especialidadDesc = itemView.findViewById(R.id.especialidad_desc);

        }
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
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

package tech.abralica.clinicalaluzapp.ui.actiity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.ClsComentario;
import tech.abralica.clinicalaluzapp.models.Especialidad;
import tech.abralica.clinicalaluzapp.ui.admin.HorasCitasActivity;
import tech.abralica.clinicalaluzapp.ui.admin.RegistrarEspFragment;

public class DetalleMedicoActivity extends AppCompatActivity {

    private DatabaseReference referenceUsuario,referencecmentario,referecepaciente;

    String idusuario;

    TextView tvnombre,tvcorreo,tvcelular;
    Button btncita,btnpublicar;
    ImageView imfperfil;
    String idmeidico,idespecialidad;

    public FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    String nombremedico,nombreespecialidad,idpaciente;
    String idmedico,nombrepaciente;
    EditText txtcomentario;
    Button btnfinish;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_medico);

        tvnombre=(TextView)findViewById(R.id.nombremedico);
        tvcorreo=(TextView)findViewById(R.id.tvcorreo);
        tvcelular=(TextView)findViewById(R.id.tvtelrfono);
        imfperfil =(ImageView)findViewById(R.id.imgperfil);
        btncita=(Button)findViewById(R.id.btnnuevacita);

        idmeidico=getIntent().getStringExtra("idusuario");
        idespecialidad=getIntent().getStringExtra("idespecialidad");
        txtcomentario =(EditText)findViewById(R.id.txtcomentario);
        btnpublicar=(Button)findViewById(R.id.btnpublicar);

        btnfinish=(Button)findViewById(R.id.btnfinish);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        idpaciente =    mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recylercomentario);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetalleMedicoActivity.this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setHasFixedSize(true);

        referenceUsuario = FirebaseDatabase.getInstance().getReference("Medico").child(idmeidico);
        referencecmentario=FirebaseDatabase.getInstance().getReference("Comentarios").child(idmeidico);

        referenceUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String nombre = dataSnapshot.child("nombres").getValue().toString();
                        String apellido = dataSnapshot.child("apellidos").getValue().toString();
                        String correo = dataSnapshot.child("email").getValue().toString();
                        String celular = dataSnapshot.child("celular").getValue().toString();
                        String foto = dataSnapshot.child("fotoPerfil").getValue().toString();
                        tvnombre.setText(nombre+" "+apellido);
                        tvcorreo.setText(correo);
                        tvcelular.setText(celular);
                        if (foto.equals("default_image")){
                            imfperfil.setImageResource(R.drawable.imgdoctorico);
                        }else{
                            Picasso.get().load(foto).fit().centerCrop().into(imfperfil);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        referecepaciente =FirebaseDatabase.getInstance().getReference("Paciente").child(idpaciente);
        referecepaciente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String nombre =dataSnapshot.child("nombres").getValue().toString();
                    String apellidos =dataSnapshot.child("apellidos").getValue().toString();
                    nombrepaciente =nombre+" "+apellidos;
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        btncita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NuevaCita();
            }
        });
        btnpublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comentario=txtcomentario.getText().toString();
                PublicarComentario(comentario);
            }
        });

        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void PublicarComentario(String comentario) {
        if (TextUtils.isEmpty(comentario)){
            Toast.makeText(this, "Campo requerido", Toast.LENGTH_SHORT).show();
        }else{
            SendComentario(comentario);
        }
    }

    private void SendComentario(String comentario) {
        String key=referencecmentario.push().getKey();
        ClsComentario com = new ClsComentario(key,nombrepaciente,comentario,"default_image");
        referencecmentario.child(key).setValue(com);
        txtcomentario.setText("");

    }

    private void NuevaCita() {
        Intent intent = new Intent(DetalleMedicoActivity.this,ReservarCitaActivity.class);
        String nombremedico=tvnombre.getText().toString();
        // idmeidico=getIntent().getStringExtra("idusuario");
        //        idespecialidad=getIntent().getStringExtra("idespecialidad");
        Bundle bundle = new Bundle();
        bundle.putString("idmeidico",idmeidico);
        bundle.putString("idespecialidad",idespecialidad);
        bundle.putString("nombremedico",nombremedico);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ClsComentario> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsComentario>()
                .setQuery(referencecmentario, ClsComentario.class).build();
        FirebaseRecyclerAdapter<ClsComentario, Items> adapter =new FirebaseRecyclerAdapter<ClsComentario, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsComentario tutores) {
                final String key = getRef(i).getKey();
                referencecmentario.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String nombre=dataSnapshot.child("nombre").getValue().toString();
                            final String mensaje=dataSnapshot.child("mensaje").getValue().toString();
                           // String rutafoto =   dataSnapshot.child("urlImagen").getValue().toString();
                            items.nombrepaciente.setText(nombre);
                            items.coemntario.setText(mensaje);
                           // Picasso.get().load(rutafoto).fit().centerCrop().into(items.especialidadImagen);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DetalleMedicoActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentarios, parent, false);
                return new Items(vista);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public  static class Items extends RecyclerView.ViewHolder{
        ImageView especialidadImagen;
        TextView nombrepaciente;
        TextView coemntario;

        public Items(@NonNull View itemView) {
            super(itemView);
          //  especialidadImagen = itemView.findViewById(R.id.especialidad_imagen);
            nombrepaciente = itemView.findViewById(R.id.id_tvnombrecliente);
            coemntario = itemView.findViewById(R.id.tvcomentario);
        }
    }

}
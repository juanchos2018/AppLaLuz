package tech.abralica.clinicalaluzapp.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import tech.abralica.clinicalaluzapp.ui.actiity.DetalleMedicoActivity;

public class DatosMedicoActivity extends AppCompatActivity {

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

    RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_medico);

        tvnombre=(TextView)findViewById(R.id.nombremedico);
        tvcorreo=(TextView)findViewById(R.id.tvcorreo);
        tvcelular=(TextView)findViewById(R.id.tvtelrfono);
        imfperfil =(ImageView)findViewById(R.id.imgperfil);
        btncita=(Button)findViewById(R.id.btnnuevacita);

        idmeidico=getIntent().getStringExtra("idusuario");
        idespecialidad=getIntent().getStringExtra("idespecialidad");
        txtcomentario =(EditText)findViewById(R.id.txtcomentario);
        btnpublicar=(Button)findViewById(R.id.btnpublicar);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        idpaciente =    mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recylercomentario);
        recyclerView.setLayoutManager(new LinearLayoutManager(DatosMedicoActivity.this, LinearLayoutManager.VERTICAL, false));

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
                        Toast.makeText(DatosMedicoActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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
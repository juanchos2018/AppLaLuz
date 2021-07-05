package tech.abralica.clinicalaluzapp.ui.medico;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.adapter.adapterCitas2;
import tech.abralica.clinicalaluzapp.adapter.adapterHistorial;
import tech.abralica.clinicalaluzapp.models.ClsCita;
import tech.abralica.clinicalaluzapp.models.ClsComentario;
import tech.abralica.clinicalaluzapp.models.Historial;
import tech.abralica.clinicalaluzapp.ui.actiity.DetalleMedicoActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicoHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicoHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private DatabaseReference referenceCitas;
    private DatabaseReference referenceCitas2,referenceHistorial,referencecmentario;
    private FirebaseAuth mAuth;
    String user_id;
    View vista;
    RecyclerView recyclerView;

    TextView tvcitspendientes,txtcitasterminadas;
    ArrayList<ClsCita> listaCitas = new ArrayList<>();

    public MedicoHomeFragment() {
        // Required empty public constructor
    }


    public static MedicoHomeFragment newInstance(String param1, String param2) {
        MedicoHomeFragment fragment = new MedicoHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_medico_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //  Toast.makeText(getContext(), user_id, Toast.LENGTH_SHORT).show();
        referenceCitas = FirebaseDatabase.getInstance().getReference("CitasReservadas");
        referenceHistorial=FirebaseDatabase.getInstance().getReference("Historial");
        referencecmentario=FirebaseDatabase.getInstance().getReference("Comentarios").child(user_id);

        tvcitspendientes=(TextView)vista.findViewById(R.id.txtcitaspendientes);
        txtcitasterminadas =(TextView)vista.findViewById(R.id.txtcitastermiandas);

        recyclerView = vista.findViewById(R.id.recylercomentarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setHasFixedSize(true);

        return vista;
    }


    @Override
    public void onStart() {
        super.onStart();
        Query q = referenceCitas;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                listaCitas.clear();
                int contador=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ClsCita cita = postSnapshot.getValue(ClsCita.class);
                    if (cita.getIdMedico().equals(user_id)) {
                        if (cita.getEstado().equals("Pendiente")){
                            contador++;
                        }
                        listaCitas.add(cita);
                    }
                }
                tvcitspendientes.setText(String.valueOf(contador));
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });

        Query query =referenceHistorial;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                int contador=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Historial medico = postSnapshot.getValue(Historial.class);
                    if (medico.getIdMedico().equals(user_id)) {
                       ;if (medico.getEstado().equals("Terminado")){
                            contador++;
                        }
                    }
                }
                txtcitasterminadas.setText(String.valueOf(contador));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

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
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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
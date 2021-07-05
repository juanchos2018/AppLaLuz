package tech.abralica.clinicalaluzapp.ui.paciente;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.adapter.adapterHistorial;
import tech.abralica.clinicalaluzapp.models.Historial;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialPacienteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialPacienteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RecyclerView recyclerView;
    private DatabaseReference referencehistorial;
    ArrayList<Historial> listaHistrial;
    String idpaciente;
    android.app.AlertDialog.Builder builder2;
    AlertDialog aler2;
    public FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    adapterHistorial mydadepter;
    public HistorialPacienteFragment() {
        // Required empty public constructor
    }


    View vista;

    // TODO: Rename and change types and number of parameters
    public static HistorialPacienteFragment newInstance(String param1, String param2) {
        HistorialPacienteFragment fragment = new HistorialPacienteFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_historial_paciente, container, false);

        listaHistrial=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        idpaciente =    mAuth.getCurrentUser().getUid();
        referencehistorial= FirebaseDatabase.getInstance().getReference("Historial");

        recyclerView = vista.findViewById(R.id.recyclerhistorialpaciente);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query =referencehistorial;
        listaHistrial.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Historial medico = postSnapshot.getValue(Historial.class);
                    if (medico.getIdPaciente().equals(idpaciente)) {
                        listaHistrial.add(medico);
                    }
                }
                mydadepter=new adapterHistorial(listaHistrial);
                recyclerView.setAdapter(mydadepter);
                mydadepter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            String dscricon =listaHistrial.get(recyclerView.getChildAdapterPosition(v)).getDescripcion();
                             DialogoDetalle(dscricon);
                        //   int cantidad=adapter.selecciondaos();
                        /// Toast.makeText(AlumnosSalon.this, String.valueOf(cantidad), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }


    private void DialogoDetalle(String desricion){
        builder2 = new AlertDialog.Builder(getActivity());
        Button btcerrrar,btnterminar;
        TextView tvestado,ttxdescricion;
       // EditText etedescripcion;
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_detallecita, null);

        builder2.setView(v);

        ttxdescricion=(TextView) v.findViewById(R.id.idetdescripcion2);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo3);
        tvestado=(TextView)v.findViewById(R.id.idestado3);
      //  btnterminar=(Button)v.findViewById(R.id.idbtnterminar);

        tvestado.setText("Detalle Cita..");
        ttxdescricion.setText(desricion);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aler2.dismiss();
            }
        });

        aler2  = builder2.create();
        aler2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aler2.show();
    }

}
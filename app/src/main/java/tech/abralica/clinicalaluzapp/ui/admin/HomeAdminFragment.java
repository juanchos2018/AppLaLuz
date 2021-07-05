package tech.abralica.clinicalaluzapp.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.ClsCita;
import tech.abralica.clinicalaluzapp.models.Especialidad;
import tech.abralica.clinicalaluzapp.models.Historial;
import tech.abralica.clinicalaluzapp.models.Medico;
import tech.abralica.clinicalaluzapp.models.Paciente;

public class HomeAdminFragment extends Fragment {


    View vista;

    private DatabaseReference referenceespecialidad,referencemedico,referecepacientes;


TextView txttotalmedicos,txtpacientes,txtespecaliades;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

       vista =inflater.inflate(R.layout.fragment_home_admin, container, false);

        referencemedico = FirebaseDatabase.getInstance().getReference("Medico");
        txttotalmedicos=(TextView)vista.findViewById(R.id.txttotalmedicos);
        referecepacientes =FirebaseDatabase.getInstance().getReference("Paciente");
        txtpacientes=(TextView)vista.findViewById(R.id.txtpacientes);
        txtespecaliades =(TextView)vista.findViewById(R.id.txtespecaliades);
        referenceespecialidad =FirebaseDatabase.getInstance().getReference("Especialidades");

        return vista;
    }


    @Override
    public void onStart() {
        super.onStart();
        Query q = referencemedico;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                int contador=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Medico cita = postSnapshot.getValue(Medico.class);
                      contador++;
                }
                txttotalmedicos.setText(String.valueOf(contador));
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });

        Query quqe = referecepacientes;
        quqe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                int contador=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Paciente cita = postSnapshot.getValue(Paciente.class);
                    contador++;
                }
                txtpacientes.setText(String.valueOf(contador));
                //  tvcitspendientes.setText(String.valueOf(contador));
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });


        Query query = referenceespecialidad;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                int contador=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Especialidad cita = postSnapshot.getValue(Especialidad.class);
                    contador++;
                }
                txtespecaliades.setText(String.valueOf(contador));
                //  tvcitspendientes.setText(String.valueOf(contador));
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
        //txtespecaliades
    }

}

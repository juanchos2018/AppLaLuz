package tech.abralica.clinicalaluzapp.ui.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.adapter.SpinAdapter2;
import tech.abralica.clinicalaluzapp.adapter.adapterMedicos;
import tech.abralica.clinicalaluzapp.models.Especialidad;
import tech.abralica.clinicalaluzapp.models.Historial;
import tech.abralica.clinicalaluzapp.models.Medico;
import tech.abralica.clinicalaluzapp.ui.actiity.DetalleMedicoActivity;
import tech.abralica.clinicalaluzapp.ui.actiity.MedicosActivity;

public class RegistrarMedicoFragment extends Fragment {

    View vista;

    private DatabaseReference referenceespecialidad,referencemedico,referenceespecialidamedico;

    ArrayList<Especialidad> listaESpecialidad;
    private SpinAdapter2 adapter2;
    ArrayAdapter<Especialidad> adapterEspecialidad;
    AutoCompleteTextView actv;
    Spinner id_spincargo;
   // especialidad
   private FirebaseAuth auth;
    TextInputEditText etonombre,etapellido,etcelular,etcorreo,ettipodcumento,etnumerodocuemnto;
    MaterialButton btnregistara;
    String idEspecaialidad;

    private ProgressDialog progressDialog;

    RecyclerView recyclerView;
    EditText txtfiltrarmedico;
    String idespcialidad;
    ArrayList<Medico> listaMedico;
    tech.abralica.clinicalaluzapp.adapter.adapterMedicos adapterMedicos;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        vista=inflater.inflate(R.layout.fragment_reg_medico, container, false);
        referenceespecialidad= FirebaseDatabase.getInstance().getReference("Medico");
        recyclerView =vista.findViewById(R.id.recylermedicos1);
        listaMedico = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        FloatingActionButton fab = vista.findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregrMedico();
            }
        });
        txtfiltrarmedico=(EditText)vista.findViewById(R.id.txtfiltrarmedico);
        txtfiltrarmedico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // searchPeopleProfile(etbuscarnombre.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString().toLowerCase());
            }
        });


        return vista;
    }

    private void AgregrMedico() {
         startActivity(new Intent(getContext(),RegistrarMedicoActivity.class));
    }


    private void filtrar(String texto) {
        ArrayList<Medico> filtradatos= new ArrayList<>();
        for(Medico item :listaMedico){
            if (item.getNombres().toLowerCase().contains(texto)){
                filtradatos.add(item);
            }
            adapterMedicos.filtrarMedicos(filtradatos);
        }
    }

    private void RegistrarMedico(String nombre, String apellido, String celuar, String correo, String numerodocumento) {
    if (TextUtils.isEmpty(nombre)){
        Toast.makeText(getContext(), "poner nobre", Toast.LENGTH_SHORT).show();
    }
    else if (TextUtils.isEmpty(apellido)){
        Toast.makeText(getContext(), "poner apellido", Toast.LENGTH_SHORT).show();
    }
    else if (TextUtils.isEmpty(correo)){
        Toast.makeText(getContext(), "pomr correo", Toast.LENGTH_SHORT).show();
    }
    else if (TextUtils.isEmpty(idEspecaialidad)){
        Toast.makeText(getContext(), "eligir especialidad", Toast.LENGTH_SHORT).show();
    }
    else {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Creando Cuenta");
        progressDialog.setMessage("Espera ....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        String password = "123456";
        auth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(getActivity(),
                        task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(),
                                        task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                referencemedico = FirebaseDatabase.getInstance().getReference("Medico").child(auth.getCurrentUser().getUid());
                                referencemedico.child("idusuario").setValue(auth.getCurrentUser().getUid());
                                referencemedico.child("dniusuario").setValue(numerodocumento);
                                referencemedico.child("nombres").setValue(nombre);
                                referencemedico.child("apellidos").setValue(apellido);
                                referencemedico.child("celular").setValue(celuar);
                                referencemedico.child("email").setValue(correo);
                                referencemedico.child("idespecialidad").setValue(idEspecaialidad);
                                referencemedico.child("tipousuario").setValue("Medico");
                                referencemedico.child("token").setValue("token");
                                //token
                                referencemedico.child("fotoPerfil").setValue("default_image").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Se registr corectamente", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });

              }

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query =referenceespecialidad;
        listaMedico.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Medico medico = postSnapshot.getValue(Medico.class);
                        listaMedico.add(medico);
                }
                adapterMedicos=new adapterMedicos(listaMedico);
                recyclerView.setAdapter(adapterMedicos);
                adapterMedicos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DatosMedicoActivity.class);
                        Bundle bundle = new Bundle();
                        String idusu=listaMedico.get(recyclerView.getChildPosition(v)).getIdusuario();
                        bundle.putString("idusuario",idusu);
                        bundle.putString("idespecialidad",idespcialidad);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }
}

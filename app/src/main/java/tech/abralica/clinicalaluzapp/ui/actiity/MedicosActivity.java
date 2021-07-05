package tech.abralica.clinicalaluzapp.ui.actiity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.adapter.adapterMedicos;
import tech.abralica.clinicalaluzapp.models.Medico;

public class MedicosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference referenceespecialidad;

    String idespcialidad;
    ArrayList<Medico> listaMedico;
    tech.abralica.clinicalaluzapp.adapter.adapterMedicos adapterMedicos;
    Button btnfinish;
    EditText tctfilto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos);
        listaMedico=new ArrayList<>();

        referenceespecialidad= FirebaseDatabase.getInstance().getReference("Medico");
        idespcialidad=getIntent().getStringExtra("idespecialidad");
        recyclerView = findViewById(R.id.recyclermedico);
        tctfilto=(EditText)findViewById(R.id.txtfiltrarmedico1);
        btnfinish=(Button)findViewById(R.id.btnfinish);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tctfilto.addTextChangedListener(new TextWatcher() {
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
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();

        Query query =referenceespecialidad;
        listaMedico.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Medico medico = postSnapshot.getValue(Medico.class);
                    if (medico.getIdespecialidad().equals(idespcialidad)) {
                        listaMedico.add(medico);
                    }
                }
                adapterMedicos=new adapterMedicos(listaMedico);
                recyclerView.setAdapter(adapterMedicos);
                adapterMedicos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MedicosActivity.this, DetalleMedicoActivity.class);
                        Bundle bundle = new Bundle();
                        String idusu=listaMedico.get(recyclerView.getChildPosition(v)).getIdusuario();
                        bundle.putString("idusuario",idusu);
                        bundle.putString("idespecialidad",idespcialidad);
                        intent.putExtras(bundle);
                        startActivity(intent);
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
}
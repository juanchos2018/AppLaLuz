package tech.abralica.clinicalaluzapp.ui.paciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;

import tech.abralica.clinicalaluzapp.R;

public class NuevaCitaActivity extends AppCompatActivity {


    private DatabaseReference referenceehoras;
    StorageReference storageRef;
    CollectionReference espRef;
    private ProgressDialog progressDialog;
    TextInputEditText tenombre,tedescripcion;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    String user_id;
    String idespecialidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita);


        recyclerView = findViewById(R.id.recyclerhorarios2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        idespecialidad=getIntent().getStringExtra("idespecialidad");
        referenceehoras= FirebaseDatabase.getInstance().getReference("HorarioAtencion").child(idespecialidad);


    }
}
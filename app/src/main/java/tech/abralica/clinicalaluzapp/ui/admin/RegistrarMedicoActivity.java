package tech.abralica.clinicalaluzapp.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import tech.abralica.clinicalaluzapp.models.Especialidad;

public class RegistrarMedicoActivity extends AppCompatActivity {


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_medico);

        referenceespecialidad = FirebaseDatabase.getInstance().getReference("Especialidades");
        listaESpecialidad = new ArrayList<>();
        referencemedico= FirebaseDatabase.getInstance().getReference("Medico");
        id_spincargo=(Spinner)findViewById(R.id.id_spincargo);
        auth = FirebaseAuth.getInstance();
        etonombre=findViewById(R.id.nombres_edit_text);
        etapellido=findViewById(R.id.apellidos_edit_text);
        etcelular=findViewById(R.id.celular_edit_text);
        etcorreo=findViewById(R.id.email_edit_text);
        etnumerodocuemnto=findViewById(R.id.doc_identidad_edit_text);

        btnregistara=findViewById(R.id.signup_next_button1);
        btnregistara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre =etonombre.getText().toString();
                String apellido =etapellido.getText().toString();
                String celuar =etcelular.getText().toString();
                String correo =etcorreo.getText().toString();
                String numerodocumento =etnumerodocuemnto.getText().toString();
                RegistrarMedico(nombre,apellido,celuar,correo,numerodocumento);
            }
        });
    }


    private void RegistrarMedico(String nombre, String apellido, String celuar, String correo, String numerodocumento) {
        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(RegistrarMedicoActivity.this, "poner nobre", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(apellido)){
            Toast.makeText(RegistrarMedicoActivity.this, "poner apellido", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(correo)){
            Toast.makeText(RegistrarMedicoActivity.this, "pomr correo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(idEspecaialidad)){
            Toast.makeText(RegistrarMedicoActivity.this, "eligir especialidad", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(RegistrarMedicoActivity.this);
            progressDialog.setTitle("Creando Cuenta");
            progressDialog.setMessage("Espera We ....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            String password = "123456";
            auth.createUserWithEmailAndPassword(correo, password)
                    .addOnCompleteListener(RegistrarMedicoActivity.this,
                            task -> {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrarMedicoActivity.this,
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
                                            Toast.makeText(RegistrarMedicoActivity.this, "Se registr corectamente", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query q=referenceespecialidad;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaESpecialidad.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Especialidad artist = postSnapshot.getValue(Especialidad.class);
                    listaESpecialidad.add(artist);
                }
                adapter2 = new SpinAdapter2(RegistrarMedicoActivity.this, android.R.layout.select_dialog_item,listaESpecialidad);
                id_spincargo.setAdapter(adapter2);
                id_spincargo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Especialidad espe = adapter2.getItem(position);
                        idEspecaialidad=espe.getIdespecialidad();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
package tech.abralica.clinicalaluzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {


    private final String[] tipos = {"Paciente", "Médico", "Administrador"};

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText inputEmail, inputPassword;
    private MaterialAutoCompleteTextView mactvTipo;
    private FirebaseUser user;
    private Bundle bundle;
    private DocumentSnapshot paciente;
    private ProgressDialog progressDialog;
    String Nodo ;
    private DatabaseReference referenceUsuarios,referenceColegas,referenceColegas2;

    Button btnolvide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        inputEmail = findViewById(R.id.fl_et_email);
        inputPassword =findViewById(R.id.fl_et_contrasena);
        mactvTipo = findViewById(R.id.fl_mactv_como);
        btnolvide=(Button)findViewById(R.id.fl_mb_olvido);
        btnolvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IROlvide();
            }
        });
        fillACTextView();

        Button signupButton = findViewById(R.id.fl_mb_registrarse);
        Button startButton = findViewById(R.id.fl_b_login);
        startButton.setOnClickListener(view1 -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String tipoUsuario = mactvTipo.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Ingrese su email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(tipoUsuario)) {
                Toast.makeText(this, "Seleccione su tipo de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            Ingresar(email,password,tipoUsuario);

        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nuevo();
            }
        });
    }

    private void Nuevo() {
        startActivity(new Intent(LoginActivity.this,RegistroPacienteActivity.class));
    }

    private void IROlvide() {
        startActivity(new Intent(LoginActivity.this,RecuperarClaveActivity.class));
    }

    private void Ingresar(String email, String password, String tipoUsuario) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Un momento");
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        ///  progressDialog.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();
        int collectionName;
        switch (tipoUsuario) {

            case "Paciente": collectionName = R.string.db_collection_pacientes;
                Nodo="Paciente";
                break;
            case "Médico": collectionName = R.string.db_collection_medicos;
                Nodo="Medico";
                break;
            case "Administrador": collectionName = R.string.db_collection_administradores;
                Nodo="Administrador";
                break;
            default: collectionName = 0; break;
        }

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userUID = auth.getCurrentUser().getUid();
                    referenceUsuarios = FirebaseDatabase.getInstance().getReference(Nodo).child(userUID);
                    referenceUsuarios.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String tipo = dataSnapshot.child("tipousuario").getValue().toString();

                                Toast.makeText(LoginActivity.this,"vsita login "+ tipo, Toast.LENGTH_SHORT).show();
                                if (tipo.equals(Nodo)){

                                    SharedPreferences preferences= getSharedPreferences("mitokenUser", Context.MODE_PRIVATE);
                                    String token=preferences.getString("token","no existe");

                                    SharedPreferences preferencess=getSharedPreferences("usuario", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor=preferencess.edit();
                                    editor.putString("tipoUsuario",Nodo);
                                    editor.commit();

                                 //   SharedPreferences sharedPref = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                                  //  rol = sharedPref.getString("tipoUsuario", "tipoUsuario");


                                    referenceUsuarios.child("token").setValue(token);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("tipousu",Nodo);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                    progressDialog.dismiss();
                                    finish();

                                }else{
                                    Toast.makeText(LoginActivity.this, "tu usario no es el indicado", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                }
                            }else
                            {
                                Toast.makeText(LoginActivity.this, "tu usario no existe", Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, "msg "+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Verifique su Email", Toast.LENGTH_SHORT).show();

                   }


            }
        });

    }
    private void fillACTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.select_dialog_item, tipos);
        AutoCompleteTextView actv = findViewById(R.id.fl_mactv_como);
        actv.setThreshold(0);
        actv.setAdapter(adapter);
    }

}
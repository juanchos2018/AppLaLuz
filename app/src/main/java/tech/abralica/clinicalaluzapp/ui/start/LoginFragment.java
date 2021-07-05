package tech.abralica.clinicalaluzapp.ui.start;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import tech.abralica.clinicalaluzapp.MainActivity;
import tech.abralica.clinicalaluzapp.R;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

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

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillACTextView();

        NavController navController = Navigation.findNavController(view);

        inputEmail = requireView().findViewById(R.id.fl_et_email);
        inputPassword = requireView().findViewById(R.id.fl_et_contrasena);
        mactvTipo = requireView().findViewById(R.id.fl_mactv_como);

        Button signupButton = requireView().findViewById(R.id.fl_mb_registrarse);
        signupButton.setOnClickListener(view1 -> navController.navigate(R.id.personalDataFragment));

        Button rememberButton = requireView().findViewById(R.id.fl_mb_olvido);
        rememberButton.setOnClickListener(view1 -> navController.navigate(R.id.rememberFragment));

        Button startButton = requireView().findViewById(R.id.fl_b_login);
        startButton.setOnClickListener(view1 -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String tipoUsuario = mactvTipo.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity(), "Ingrese su email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(tipoUsuario)) {
                Toast.makeText(getActivity(), "Seleccione su tipo de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Un momento");
            progressDialog.setMessage("Cargando...");
            progressDialog.show();
          ///  progressDialog.setCanceledOnTouchOutside(false);
            auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                } else {
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

                    String userUID = auth.getCurrentUser().getUid();
                    referenceUsuarios = FirebaseDatabase.getInstance().getReference(Nodo).child(userUID);
                    referenceUsuarios.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        String tipo = dataSnapshot.child("tipousuario").getValue().toString();

                                        Toast.makeText(getContext(), tipo, Toast.LENGTH_SHORT).show();
                                        if (tipo.equals(Nodo)){
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("tipousu",Nodo);
                                            intent.putExtras(bundle);
                                            startActivity(intent);

                                            progressDialog.dismiss();

                                        }else{
                                            Toast.makeText(getContext(), "tu usarion no es el indicado", Toast.LENGTH_SHORT).show();

                                            progressDialog.dismiss();
                                                  }
                                    }else
                                    {
                                        Toast.makeText(getContext(), "tu usarion no es el indicado", Toast.LENGTH_SHORT).show();

                                        progressDialog.dismiss();
                                    }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(), "ERror en Dtos", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void fillACTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, tipos);
        AutoCompleteTextView actv = requireView().findViewById(R.id.fl_mactv_como);
        actv.setThreshold(0);
        actv.setAdapter(adapter);
    }
}
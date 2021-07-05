package tech.abralica.clinicalaluzapp.ui.start;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Paciente;

public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText inputEmail, inputPassword, inputConfirmPassword;

    private Paciente usuario;
    private String email;
    private DatabaseReference reference;
    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        inputEmail = requireView().findViewById(R.id.email_edit_text);
        inputPassword = getView().findViewById(R.id.password_edit_text);
        inputConfirmPassword = getView().findViewById(R.id.confirm_edit_text);

        Button loginButton = getView().findViewById(R.id.signup_cancel_button);
        loginButton.setOnClickListener(view1 ->
                navController.navigate(R.id.loginFragment));

        Button signupButton = getView().findViewById(R.id.register_register_button);
        signupButton.setOnClickListener(view1 -> {
            email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString();
            String confirm = inputConfirmPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity(), "Ingrese su email",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Ingrese su contraseña",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(confirm)) {
                Toast.makeText(getActivity(), "Confirme su contraseña",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(getActivity(), "Las contraseñas no coinciden",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(),
                            task -> {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(),
                                            task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    addToDb(navController,auth.getCurrentUser().getUid());
                                }
                            });
        });
    }

    private void addToDb(NavController navController,String user_id) {
        if (getArguments().containsKey("paciente")) {

            reference = FirebaseDatabase.getInstance().getReference("Paciente").child(user_id);
            reference.child("id_usuario").setValue(user_id);
            reference.child("dni_usuario").setValue(usuario.getDocIdentidad().getCodigo());
            reference.child("nombres").setValue(usuario.getNombres());
            reference.child("apellidos").setValue(usuario.getApellidos());
            reference.child("celular").setValue(usuario.getCelular());
            reference.child("email").setValue(email);
            reference.child("token").setValue("token");
            reference.child("tipousuario").setValue("Paciente");
            reference.child("fotoPerfil").setValue("defult_image").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    Toast.makeText(getContext(), "Se registr corectamente", Toast.LENGTH_SHORT).show();
                }
            });


           /* usuario = (Paciente) getArguments().getSerializable("paciente");
            Map<String, Object> userMap = new HashMap<>();
            Map<String, Object> docIdentidadMap = new HashMap<>();
            docIdentidadMap.put("tipo", usuario.getDocIdentidad().getTipo());
            docIdentidadMap.put("codigo", usuario.getDocIdentidad().getCodigo());
            userMap.put("docIdentidad", docIdentidadMap);
            userMap.put("nombres", usuario.getNombres());
            userMap.put("apellidos", usuario.getApellidos());
            userMap.put("celular", usuario.getCelular());
            userMap.put("pais", usuario.getPais());
            userMap.put("ciudad", usuario.getCiudad());
            userMap.put("fotoPerfil", "TODO");
            userMap.put("email", email);
            db.collection("pacientes")
                    .add(userMap)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Se registró correctamente!",
                                Toast.LENGTH_LONG).show();

                        navController.navigate(R.id.loginFragment);
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "No se pudo registrar al cliente. " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show()); */
        }
    }
}
package tech.abralica.clinicalaluzapp.ui.start;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.DocIdentidad;
import tech.abralica.clinicalaluzapp.models.Paciente;
import tech.abralica.clinicalaluzapp.models.Usuario;
import tech.abralica.clinicalaluzapp.models.enums.TipoDocEnum;

public class PersonalDataFragment extends Fragment {

    private final String[] tipos = {"DNI", "Carné de extranjería", "PTP", "Pasaporte"};

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText inputEmail, inputPassword, inputConfirmPassword;

    private Paciente usuario;
    private String email;
    private DatabaseReference reference;

    private ProgressDialog progressDialog;


    public PersonalDataFragment() {
        // Required empty public constructor
    }

    public static PersonalDataFragment newInstance() {
        return new PersonalDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_signup_persona, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillACTextView();
        TextInputLayout et_tipo = requireView().findViewById(R.id.tipo_doc_input_layout);
        EditText et_doc_identidad = requireView().findViewById(R.id.doc_identidad_edit_text);
        EditText et_nombres = requireView().findViewById(R.id.nombres_edit_text);
        EditText et_apellidos = requireView().findViewById(R.id.apellidos_edit_text);
        EditText et_celular = requireView().findViewById(R.id.celular_edit_text);


        inputEmail = requireView().findViewById(R.id.email_edit_text);
        inputPassword = getView().findViewById(R.id.password_edit_text);


        auth = FirebaseAuth.getInstance();
       // Button cancelButton = requireView().findViewById(R.id.signup_cancel_button);
       // cancelButton.setOnClickListener(view1 -> navController.navigate(R.id.loginFragment));

        Button nextButton = requireView().findViewById(R.id.signup_next_button);
        nextButton.setOnClickListener(view1 -> {

            Usuario usuario = new Usuario();
            email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString();
            TipoDocEnum tipoDocEnum;

            switch (Objects.requireNonNull(et_tipo.getEditText()).getText().toString()) {
                case "DNI":
                    tipoDocEnum = TipoDocEnum.DNI;
                    break;
                case "Carné de extranjería":
                    tipoDocEnum = TipoDocEnum.CarneExtranjeria;
                    break;
                case "PTP":
                    tipoDocEnum = TipoDocEnum.PTP;
                    break;
                case "Pasaporte":
                    tipoDocEnum = TipoDocEnum.Pasaporte;
                    break;
                default:
                    Toast.makeText(getActivity(), "Seleccione un tipo de documento de identidad", Toast.LENGTH_SHORT).show();
                    return;
            }

            String codDocIdentidad = et_doc_identidad.getText().toString();

            if (TextUtils.isEmpty(codDocIdentidad)) {
                Toast.makeText(getActivity(), "Ingrese su documento de identidad", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombres = et_nombres.getText().toString();

            if (TextUtils.isEmpty(nombres)) {
                Toast.makeText(getActivity(), "Ingrese sus nombres", Toast.LENGTH_SHORT).show();
                return;
            }

            String apellidos = et_apellidos.getText().toString();

            if (TextUtils.isEmpty(apellidos)) {
                Toast.makeText(getActivity(), "Ingrese sus apellidos", Toast.LENGTH_SHORT).show();
                return;
            }

            String celular = et_celular.getText().toString();

            if (TextUtils.isEmpty(celular)) {
                Toast.makeText(getActivity(), "Ingrese su celular", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog =new ProgressDialog(getContext());
            progressDialog.setTitle("Creando Cuenta");
            progressDialog.setMessage("Espera We ....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
       //     String nombre =
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(),
                            task -> {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(),
                                            task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    reference = FirebaseDatabase.getInstance().getReference("Paciente").child(auth.getCurrentUser().getUid());
                                    reference.child("idusuario").setValue(auth.getCurrentUser().getUid());
                                    reference.child("dniusuario").setValue(codDocIdentidad);
                                    reference.child("nombres").setValue(nombres);
                                    reference.child("apellidos").setValue(apellidos);
                                    reference.child("celular").setValue(celular);
                                    reference.child("email").setValue(email);
                                    reference.child("token").setValue("token");
                                    reference.child("tipousuario").setValue("paciente");
                                    reference.child("fotoPerfil").setValue("default_image").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Se registr corectamente", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            });
         //   navController.navigate(R.id.datosPacienteFragment, bundle);
        });
    }

    private void fillACTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, tipos);

        AutoCompleteTextView actv = getView().findViewById(R.id.tipo_doc_actv);
        actv.setThreshold(0);
        actv.setAdapter(adapter);
    }


}
package tech.abralica.clinicalaluzapp.ui.start;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Paciente;
import tech.abralica.clinicalaluzapp.models.Usuario;

public class DatosPacienteFragment extends Fragment {

    private final String[] esCountries = {"Perú", "Argentina", "Bolivia", "Chile", "Colombia", "Costa Rica", "Cuba", "Ecuador", "El Salvador", "España", "Guatemala", "Guinea Ecuatorial", "Honduras", "México", "Nicaragua", "Panamá", "Paraguay", "Puerto", "República Dominicana", "Uruguay", "Venezuela"};

    private final String[] peruCities = {"Abancay", "Arequipa", "Ayacucho", "Cajamarca", "Callao", "Cerro de Pasco", "Chachapoyas", "Chiclayo", "Chimbote", "Contamana", "Cusco", "Huacho", "Huancavelica", "Huancayo", "Huaraz", "Huánuco", "Ica", "Iquitos", "Juliaca", "Lima", "Moquegua", "Moyobamba", "Piura", "Pucallpa", "Puerto Maldonado", "Puno", "Tacna", "Trujillo", "Tumbes"};

    private Usuario usuario;
    private Paciente paciente;

    private Button nextButton, cancelButton;

    public DatosPacienteFragment() {
        // Required empty public constructor
    }

    public static DatosPacienteFragment newInstance() {
        return new DatosPacienteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_paciente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillACTVCountries();
        fillACTVCities();

        NavController navController = Navigation.findNavController(view);

        TextInputLayout tilPais = requireView().findViewById(R.id.country_input_layout);
        TextInputLayout tilCiudad = requireView().findViewById(R.id.city_input_layout);

        Button nextButton = requireView().findViewById(R.id.signup_next_button);
        nextButton.setOnClickListener(view1 -> {
            String pais = Objects.requireNonNull(tilPais.getEditText()).getText().toString();
            String ciudad = Objects.requireNonNull(tilCiudad.getEditText()).getText().toString();

            if (TextUtils.isEmpty(pais)) {
                Toast.makeText(getActivity(), "Seleccione un pais", Toast.LENGTH_SHORT).show();
                return;
            }

          //  if (TextUtils.isEmpty(ciudad)) {
          //      Toast.makeText(getActivity(), "Seleccione una ciudad", Toast.LENGTH_SHORT).show();
          //      return;
          //  }
            ciudad="tacna";
            paciente = new Paciente();

            Bundle bundleget = new Bundle();
            Usuario uus =new Usuario();
            uus= (Usuario) bundleget.getSerializable("usuario");
            String nombre = bundleget.getString("nombres");
            Toast.makeText(getContext(), uus.getNombres(), Toast.LENGTH_SHORT).show();


          //  String nombre =
           /// paciente.setDocIdentidad(usuario.getDocIdentidad());
         //   paciente.setNombres(usuario.getNombres());
         //   paciente.setApellidos(usuario.getApellidos());
        //    paciente.setCelular(usuario.getCelular());

          //  paciente.setPais(pais);
          //  paciente.setCiudad(ciudad);

            Bundle bundle = new Bundle();
            bundle.putSerializable("paciente", paciente);

           // navController.navigate(R.id.signupFragment, bundle);
        });

    }

    private void fillACTVCountries() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, esCountries);
        AutoCompleteTextView actv = requireView().findViewById(R.id.country_actv);
        actv.setThreshold(0);
        actv.setAdapter(adapter);
    }

    private void fillACTVCities() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, peruCities);
        AutoCompleteTextView actv = requireView().findViewById(R.id.city_actv);
        actv.setThreshold(0);
        actv.setAdapter(adapter);
    }
}
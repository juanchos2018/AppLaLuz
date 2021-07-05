package tech.abralica.clinicalaluzapp.ui.paciente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.ui.actiity.ReservarCitaActivity;
import tech.abralica.clinicalaluzapp.ui.admin.RegistrarEspFragment;

public class HomePacienteFragment extends Fragment {

    private LinearLayout bReservar, bCitas, bHistorial, bUbicanos;
    private TextView tvPaciente;


    private int currentAnimationFrame = 0;

    private LottieAnimationView animationView;
    View vista;
    android.app.AlertDialog.Builder builder2;
    AlertDialog aler2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_paciente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("usuario",
                Context.MODE_PRIVATE);

        String nombre = sharedPref.getString("nombres", "nombres");

        tvPaciente = requireView().findViewById(R.id.home_tv_paciente);
        tvPaciente.setText(String.format("Bienvenido %s!", nombre));

        bReservar = requireView().findViewById(R.id.fph_ll_reservar);
        bReservar.setOnClickListener(view1 ->
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReservarEspFragment()).commit());

        bCitas = requireView().findViewById(R.id.fph_ll_citas);
        bCitas.setOnClickListener(view1 ->
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GestionarCitasFragment()).commit());

        bHistorial=requireView().findViewById(R.id.fph_ll_historial);
        bHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistorialPacienteFragment()).commit();
            }
        });

        animationView = requireView().findViewById(R.id.animessage);
        resetAnimationView();
        animationView.playAnimation();
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrrMensje();
            }
        });

    }

    private void MostrrMensje() {

        builder2 = new AlertDialog.Builder(getActivity());
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_mascarilla, null);
        animationView = v.findViewById(R.id.animation_viewcheck4);
        resetAnimationView();
        animationView.playAnimation();
        builder2.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText("Recuarda Usar Mascarilla");
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aler2.dismiss();
            }
        });

        aler2  = builder2.create();
        aler2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        aler2.show();
    }

    private void resetAnimationView() {
        currentAnimationFrame = 0;
        animationView.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return null;
                    }
                }
        );
    }
}

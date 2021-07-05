package tech.abralica.clinicalaluzapp.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.UbicacionActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClinicaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClinicaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardView cartelefono,cardwahtapp,cardubicacion;
    TextView tvtelefono,tvwhatapp;


    public ClinicaFragment() {
        // Required empty public constructor
    }

    View vista;
    public static ClinicaFragment newInstance(String param1, String param2) {
        ClinicaFragment fragment = new ClinicaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_clinica, container, false);

        tvtelefono=(TextView)vista.findViewById(R.id.tvtelefono);
        cartelefono =(CardView)vista.findViewById(R.id.cartelefono);
        tvwhatapp=(TextView)vista.findViewById(R.id.tvwhatsapp) ;

        ///cardwatapp
        cardwahtapp=(CardView)vista.findViewById(R.id.cardwatapp);
        cardubicacion=(CardView)vista.findViewById(R.id.cardubicacion);
        cardwahtapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String wha =tvwhatapp.getText().toString();
                   WhataApps(wha);
            }
        });

        cartelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ntelefono =tvtelefono.getText().toString();
                Telefono(ntelefono);
            }
        });

            cardubicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irUbiacacion();
                }
            });
       return vista;

    }

    private void irUbiacacion() {
    startActivity(new Intent(getContext(), UbicacionActivity.class));
    }

    private void Telefono(String numero) {
        try {
            if (TextUtils.isEmpty(numero)){
                Toast.makeText(getContext(), "no existe numero de telefono", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+numero));
                startActivity(intent);
            }

        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void WhataApps(String telefono) {
        try {

            if (TextUtils.isEmpty(telefono)){
                Toast.makeText(getContext(), "no hay telefono", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent _intencion = new Intent("android.intent.action.MAIN");
                _intencion.setAction(Intent.ACTION_SEND);
                _intencion.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators("51"+telefono)+"@s.whatsapp.net");
                startActivity(_intencion);
            }

        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
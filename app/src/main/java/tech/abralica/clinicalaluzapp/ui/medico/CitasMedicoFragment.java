package tech.abralica.clinicalaluzapp.ui.medico;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import tech.abralica.clinicalaluzapp.EditarCitaActivity;
import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.adapter.adapterCitas;
import tech.abralica.clinicalaluzapp.adapter.adapterCitas2;
import tech.abralica.clinicalaluzapp.models.ClsCita;
import tech.abralica.clinicalaluzapp.models.Historial;
import tech.abralica.clinicalaluzapp.ui.actiity.ReservarCitaActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CitasMedicoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitasMedicoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FirebaseUser currentUser;
    LinearLayoutManager linearLayoutManager;
    RecyclerView friendList;
    String user_id;
    adapterCitas2 myAdapter;
    ArrayList<ClsCita> listaCitas = new ArrayList<>();
    android.app.AlertDialog.Builder builder;
    AlertDialog alert;
    private DatabaseReference referenceCitas;
    private DatabaseReference referenceCitas2,referenceHistorial,referencePaciente;
    // private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    android.app.AlertDialog.Builder builder2;
    AlertDialog aler2;
    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;
  EditText  etbuscarnombre;
    Historial objhistorial;
    public CitasMedicoFragment() {
        // Required empty public constructor
    }

    View vista;
    // TODO: Rename and change types and number of parameters
    public static CitasMedicoFragment newInstance(String param1, String param2) {
        CitasMedicoFragment fragment = new CitasMedicoFragment();
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
        // Inflate the layout for this fragment
        vista =inflater.inflate(R.layout.fragment_citas_medico, container, false);

        listaCitas = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
      //  Toast.makeText(getContext(), user_id, Toast.LENGTH_SHORT).show();
        referenceCitas = FirebaseDatabase.getInstance().getReference("CitasReservadas");
        referenceHistorial=FirebaseDatabase.getInstance().getReference("Historial");
        friendList = vista.findViewById(R.id.recyclercitasmedico);
        friendList.setLayoutManager(new LinearLayoutManager(this.getContext()));


        etbuscarnombre =(EditText)vista.findViewById(R.id.txtbusccarcita) ;

        etbuscarnombre.addTextChangedListener(new TextWatcher() {
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

        return vista;
    }


    private void filtrar(String texto) {
        ArrayList<ClsCita> filtradatos= new ArrayList<>();

        for(ClsCita item :listaCitas){
            if (item.getNombrepaciente().toLowerCase().contains(texto)){
                filtradatos.add(item);
            }
            myAdapter.filtrarPaciente(filtradatos);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        Query q = referenceCitas;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                listaCitas.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ClsCita cita = postSnapshot.getValue(ClsCita.class);
                    if (cita.getIdMedico().equals(user_id)) {
                        listaCitas.add(cita);
                    }
                }

                myAdapter = new adapterCitas2(listaCitas);
                friendList.setAdapter(myAdapter);
                myAdapter.setOnClickListener(v -> {
                    objhistorial = new Historial();
                    objhistorial.setIdPaciente(listaCitas.get(friendList.getChildAdapterPosition(v)).getIdPaciente());
                    objhistorial.setNombrepaciente(listaCitas.get(friendList.getChildAdapterPosition(v)).getNombrepaciente());
                    objhistorial.setFecha(listaCitas.get(friendList.getChildAdapterPosition(v)).getFecha());
                    objhistorial.setHora(listaCitas.get(friendList.getChildAdapterPosition(v)).getHora());
                    objhistorial.setIdMedico(listaCitas.get(friendList.getChildAdapterPosition(v)).getIdMedico());
                    objhistorial.setNombreMedico(listaCitas.get(friendList.getChildAdapterPosition(v)).getNombreMedico());
                    objhistorial.setIdEspecialida(listaCitas.get(friendList.getChildAdapterPosition(v)).getIdEspecialida());
                    objhistorial.setEspecialidad(listaCitas.get(friendList.getChildAdapterPosition(v)).getEspecialidad());

                    //   Toast.makeText(getContext(), listaCitas.get(friendList.getChildAdapterPosition(v)).getHora(), Toast.LENGTH_SHORT).show();
                   String estado=listaCitas.get(friendList.getChildAdapterPosition(v)).getEstado();
                   Dialogo(listaCitas.get(friendList.getChildAdapterPosition(v)).getKeycita(),estado);


                });
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    private void Dialogo(String key,String estado) {
        builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] items = new CharSequence[3];
        items[0] = "Confirmar";
        items[1] = "Terminar";
        items[2] = "Otros";
        // View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_datetime, null);
        // builder.setView(v);
        builder.setTitle("Acciones").setItems(items, (dialog, posicion) -> {
            if (posicion == 0) {
              //  Anular(key);
                if (estado.equals("Confirmado")){
                    DialogoWarning();
                    alert.dismiss();
                }else{
                    Confirmar(key);
                    alert.dismiss();
                }
            }
            if (posicion == 1) {
               DialogoTerminar(key);
             //   startActivity(intent);
            }
        });
        alert = builder.create();
        alert.show();
    }


    private void Confirmar(String key) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando..");
        progressDialog.setTitle("Cofirmando");
        progressDialog.show();
        progressDialog.setCancelable(false);
        referenceCitas2 = FirebaseDatabase.getInstance().getReference("CitasReservadas").child(key);
        referenceCitas2.child("estado").setValue("Confirmado")  .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                referencePaciente =FirebaseDatabase.getInstance().getReference("Paciente").child(objhistorial.getIdPaciente());
                referencePaciente.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    progressDialog.dismiss();
                                    String tokenpaciente =dataSnapshot.child("token").getValue().toString();
                                    Toast.makeText(getContext(), tokenpaciente, Toast.LENGTH_SHORT).show();
                                    Notificar(tokenpaciente,"Su Cita ha Sido Aceptada","por el "+objhistorial.getNombreMedico());
                                    DialogoOk("Se ha Confirmado la Cita");
                                }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });


            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }
    private  void  Notificar(String token,String titulo,String detalle){
        RequestQueue myrequest = Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();
        try {
            json.put("to", token);
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", titulo);
            notificacion.put("detalle", detalle);
            json.put("data", notificacion);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAg-e6R5o:APA91bE1Fk_sB1kdCZXu9B0qWsJB-QR_oXQ0rktSvFHcbN24lBbkVBc7ovEyX__zM0om_3JtxN4gaOfk-iQVBo88R7vLtc01IcjjJHHHtLQLR3v-ntPlEQWWtXXSgP-Yn_Au_QmGgLFm");
                    return header;
                }
            };
            //
            myrequest.add(request);
            //
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void DialogoOk(String mensaje){
        builder2 = new AlertDialog.Builder(getActivity());
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_ok, null);
        animationView = v.findViewById(R.id.animation_viewcheck);
        resetAnimationView();
        animationView.playAnimation();
        builder2.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText(mensaje);
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

    private void DialogoWarning(){
        builder2 = new AlertDialog.Builder(getActivity());
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_warning, null);
        animationView = v.findViewById(R.id.animation_viewcheck);
        resetAnimationView();
        animationView.playAnimation();
        builder2.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText("Esta cita ya esta confirmada ");
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

    private void DialogoTerminar(String key){
        builder2 = new AlertDialog.Builder(getActivity());
        Button btcerrrar,btnterminar;
        TextView tvestado;
        EditText etedescripcion;
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_terminar, null);

        builder2.setView(v);

        etedescripcion=(EditText)v.findViewById(R.id.idetdescripcion1);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        btnterminar=(Button)v.findViewById(R.id.idbtnterminar);

        tvestado.setText("Desea Terminar Cita..");
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aler2.dismiss();
            }
        });
        btnterminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desci =etedescripcion.getText().toString();
                if (TextUtils.isEmpty(desci)){
                    Toast.makeText(getActivity(), "poner descricion", Toast.LENGTH_SHORT).show();
                }else {
                    aler2.dismiss();
                    TerminarCita(key,desci);
                   // Toast.makeText(getActivity(), objhistorial.getEstado(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        aler2  = builder2.create();
        aler2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aler2.show();
    }

    private void TerminarCita(String key,String descrip) {
        String keyhistorial=referenceHistorial.push().getKey();
        objhistorial.setKeyhistorial(keyhistorial);
        objhistorial.setDescripcion(descrip);
        objhistorial.setEstado("Terminado");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        objhistorial.setFechafinalizacion(fecha);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando..");
        progressDialog.setTitle("Cofirmando");
        progressDialog.show();
        progressDialog.setCancelable(false);
        referenceHistorial.child(keyhistorial).setValue(objhistorial).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    referenceCitas= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(key);
                    referenceCitas.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if ( task.isSuccessful()){

                                referencePaciente =FirebaseDatabase.getInstance().getReference("Paciente").child(objhistorial.getIdPaciente());
                                referencePaciente.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            progressDialog.dismiss();
                                            String tokenpaciente =dataSnapshot.child("token").getValue().toString();
                                            Toast.makeText(getContext(), tokenpaciente, Toast.LENGTH_SHORT).show();
                                            Notificar(tokenpaciente,"Su Cita ha Sido finalizada","por el "+objhistorial.getNombreMedico());
                                            progressDialog.dismiss();
                                            DialogoOk("Se ha finalizado la cita");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                                    }
                                });
                             //   Notificar(token,titulo,detalle);
                               // Toast.makeText(getActivity(), "Eliminado ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
package tech.abralica.clinicalaluzapp.ui.paciente;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

///import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Citas;
import tech.abralica.clinicalaluzapp.models.Doctor;

public class ReservarMedicoFragment extends Fragment {

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    LinearLayoutManager linearLayoutManager;
    RecyclerView friendList;
    android.app.AlertDialog.Builder builder;
    AlertDialog alert;
    String fecha;
    String user_id;

    private FirebaseFirestore db;
    //private FirestoreRecyclerAdapter adapter;
    private DatabaseReference referenceCitas;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_reservar_medic, container, false);

        friendList = vista.findViewById(R.id.recycler_medicos);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String myInt = bundle.getString("key", "0");
            Toast.makeText(getContext(), myInt, Toast.LENGTH_SHORT).show();
        }

        mAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        referenceCitas = FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id);
        init();
        getFriendList();

        return vista;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getFriendList() {
        Query query = db.collection("medicos");
     /*   FirestoreRecyclerOptions<Doctor> response = new FirestoreRecyclerOptions.Builder<Doctor>().setQuery(query, Doctor.class).build();
        adapter = new FirestoreRecyclerAdapter<Doctor, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(@NotNull FriendsHolder holder, int position, @NotNull Doctor model) {
                holder.tvnombre.setText(model.getNombres());
                holder.btnserservar.setOnClickListener(v -> {
                    String docId = getSnapshots().getSnapshot(position).getId();
                    String nombremedico = model.getNombres();

                    Reservar(docId, nombremedico);
                });
            }

            @NotNull
            @Override
            public FriendsHolder onCreateViewHolder(@NotNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_medicos, group, false);
                return new FriendsHolder(view);
            }

            @Override
            public void onError(@NotNull FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        friendList.setAdapter(adapter);
        adapter.startListening(); */
    }

    //Dialogo
    private void Reservar(String idmedico, String nombremedico) {
        builder = new AlertDialog.Builder(getActivity());

        Button btcerrrar, btnreservar;
        ImageButton btnfecha, btnhora;
        EditText fechaCita, horacita;

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_datetime, null);
        builder.setView(v);
        btcerrrar = v.findViewById(R.id.btncerrar);
        btnreservar = v.findViewById(R.id.iniciar_button);
        btnfecha = v.findViewById(R.id.ib_obtener_fecha);
        fechaCita = v.findViewById(R.id.etFecha);
        btnhora = v.findViewById(R.id.ib_obtener_hora);
        horacita = v.findViewById(R.id.etHora);
        btnfecha.setOnClickListener(v1 -> {
            //calendariopo

            DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + dayOfMonth : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + mesActual : String.valueOf(mesActual);
                fecha = diaFormateado + BARRA + mesFormateado + BARRA + year;

                //btn aceptar
                fechaCita.setText(fecha);

            }, anio, mes, dia);
            recogerFecha.show();
        });
        btnhora.setOnClickListener(v12 -> {
            TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String horaFormateada = (hourOfDay < 10) ? CERO + hourOfDay : String.valueOf(hourOfDay);
                    String minutoFormateado = (minute < 10) ? CERO + minute : String.valueOf(minute);
                    String AM_PM;
                    if (hourOfDay < 12) {
                        AM_PM = "a.m.";
                    } else {
                        AM_PM = "p.m.";
                    }
                    horacita.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                    // etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                }
            }, hora, minuto, false);
            recogerHora.show();
        });
        btcerrrar.setOnClickListener(v13 -> alert.dismiss());
        btnreservar.setOnClickListener(v14 -> {
            String fe = fechaCita.getText().toString();
            String hora = horacita.getText().toString();
            if (TextUtils.isEmpty(fe)) {
                Toast.makeText(getContext(), "Poner Fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            ReservarCita(fe, hora, idmedico, nombremedico);
        });

        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    private void ReservarCita(String fecha, String hora, String idmedico, String nombremedico) {
        ///reservar cita que manda aal bd Firebae
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Agregajdo Cita");
        progressDialog.setMessage("cargando...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String key = referenceCitas.push().getKey();

        // para gaurar el nodo Citas
        // sadfsdfsdfsd1212sdss
        Citas objecita = new Citas(key, user_id, "user", fecha, hora, idmedico, nombremedico, "especilidad", "Nuevo");

        //  referenceCitas= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id);
        referenceCitas.child(key).setValue(objecita).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public static class FriendsHolder extends RecyclerView.ViewHolder {
        TextView tvnombre;
        Button btnserservar;

        public FriendsHolder(View itemView) {
            super(itemView);
            tvnombre = itemView.findViewById(R.id.tvnombredoctor);
           // btnserservar = itemView.findViewById(R.id.btnrsservar);
        }
    }
}
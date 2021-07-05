package tech.abralica.clinicalaluzapp.ui.actiity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.ClsCita;
import tech.abralica.clinicalaluzapp.models.Horario;
import tech.abralica.clinicalaluzapp.ui.admin.HorasCitasActivity;

public class ReservarCitaActivity extends AppCompatActivity {

    private DatabaseReference referenceehoras,referecemedico,referenceespecialidad,refereceCita,referecepaciente;
    StorageReference storageRef;
    CollectionReference espRef;
    private ProgressDialog progressDialog;
    TextInputEditText tenombre,tedescripcion;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;


    String idespecialidad,idmedico,nombrepaciente;
    TextView tvfecha,tvhora;
    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String CERO = "0";
    private static final String BARRA = "/";
   // bundle.putString("idmeidico",idmeidico);
    //        bundle.putString("idespecialidad",idespecialidad);
    ImageButton btnfecha;
    Button btnreservar;
    Button btnfinish;
    String tokenMedico;
    String nombremedico,nombreespecialidad,idpaciente;
    public FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    android.app.AlertDialog.Builder builder2;
    AlertDialog aler2;
    private final int frames = 9;
    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_cita);

        recyclerView = findViewById(R.id.recyclerhorarios2);
        btnfecha =(ImageButton)findViewById(R.id.ib_obtener_fecha2);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        idpaciente =    mAuth.getCurrentUser().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        idmedico=getIntent().getStringExtra("idmeidico");
        idespecialidad=getIntent().getStringExtra("idespecialidad");
        nombremedico=getIntent().getStringExtra("nombremedico");
        btnfinish=(Button)findViewById(R.id.btnfinish);

        referenceehoras= FirebaseDatabase.getInstance().getReference("HorarioAtencion").child(idespecialidad);
        referecemedico=FirebaseDatabase.getInstance().getReference("Medico").child(idmedico);
        referecemedico.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                            tokenMedico=dataSnapshot.child("token").getValue().toString();
                    }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
        referenceespecialidad=FirebaseDatabase.getInstance().getReference("Especialidades").child(idespecialidad);
        referenceespecialidad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                            nombreespecialidad=dataSnapshot.child("nombre").getValue().toString();
                    }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
        referecepaciente =FirebaseDatabase.getInstance().getReference("Paciente").child(idpaciente);
        referecepaciente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                            String nombre =dataSnapshot.child("nombres").getValue().toString();
                            String apellidos =dataSnapshot.child("apellidos").getValue().toString();
                            nombrepaciente =nombre+" "+apellidos;
                    }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
        tvfecha=(TextView)findViewById(R.id.fechaseleccionada);
        tvhora=(TextView)findViewById(R.id.horaselecionada);
        btnreservar =(Button)findViewById(R.id.btnreservar);

        btnfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fecha();
            }
        });
        btnreservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hora =tvhora.getText().toString();
                String fecha  =tvfecha.getText().toString();
                ReservarCita(hora,fecha);
            }
        });
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void ReservarCita(String hora, String fecha) {
        refereceCita=FirebaseDatabase.getInstance().getReference("CitasReservadas");
        String keycita =refereceCita.push().getKey();
    if (TextUtils.isEmpty(hora)){
        Toast.makeText(this, "poner hora", Toast.LENGTH_SHORT).show();
    }else if (TextUtils.isEmpty(fecha)){
        Toast.makeText(this, "poer fecha", Toast.LENGTH_SHORT).show();
    }
    else if (TextUtils.isEmpty(nombreespecialidad)){
        Toast.makeText(this, "falta un campo", Toast.LENGTH_SHORT).show();
    }else{
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subiendo");
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        ClsCita cita = new ClsCita(keycita,idpaciente,nombrepaciente,fecha,hora,idmedico,nombremedico,idespecialidad,nombreespecialidad,"Pendiente");
        refereceCita.child(keycita).setValue(cita).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                    JSONObject json = new JSONObject();
                    try {

                        json.put("to", tokenMedico);
                        JSONObject notificacion = new JSONObject();
                        notificacion.put("titulo", "Nueva Cita para Usted");
                        notificacion.put("detalle", "Persona " +nombrepaciente);
                        json.put("data", notificacion);
                        String URL = "https://fcm.googleapis.com/fcm/send";
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> header = new HashMap<>();
                                header.put("content-type", "application/json");
                                header.put("authorization", "key=AAAAfvozJiI:APA91bE-VcaWipii9HXHDgRBGlorwIzmihVdqd9MqxveeHl75KUStT-zw41e6OlQ2jhOwzTa5NxJQ1e_TYLZrsYsCbtdAXLQ3nxeBspun0NZWWXh9X2Z_2htzmwkAk3cRqz3QcQPT1fs");
                                return header;
                            }
                        };
                        //
                        myrequest.add(request);
                        //
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    DialogoOk();
                    //  Toast.makeText(ReservarCitaActivity.this, "Cita Registraeda", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {            @Override
        public void onFailure(@NonNull @NotNull Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ReservarCitaActivity.this, "Error conecion", Toast.LENGTH_SHORT).show();
         }
        });
    }

    }
    private void DialogoOk(){

        builder2 = new AlertDialog.Builder(ReservarCitaActivity.this);
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(ReservarCitaActivity.this).inflate(R.layout.dialogo_ok, null);
        animationView = v.findViewById(R.id.animation_viewcheck);
        resetAnimationView();
        animationView.playAnimation();
        builder2.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText("Se ha reservado Cita");
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


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Horario> recyclerOptions = new FirebaseRecyclerOptions.Builder<Horario>()
                .setQuery(referenceehoras, Horario.class).build();
        FirebaseRecyclerAdapter<Horario, Items> adapter =new FirebaseRecyclerAdapter<Horario, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull Horario tutores) {
                final String key = getRef(i).getKey();
                referenceehoras.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String horainicio=dataSnapshot.child("horaInicio").getValue().toString();
                            final String horafin=dataSnapshot.child("horaFin").getValue().toString();
                            final String idespecialidad=dataSnapshot.child("idespecialidad").getValue().toString();
                            final String especialidad=dataSnapshot.child("especialidad").getValue().toString();

                            items.horainicio.setText(horainicio);
                            items.horafin.setText(horafin);
                            items.tvespecialidad.setText(especialidad);
                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //
                                    tvhora.setText(horainicio+" - " +horafin);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ReservarCitaActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private  void Fecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(ReservarCitaActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                String   fecha  =diaFormateado + BARRA + mesFormateado + BARRA + year;
                tvfecha.setText(fecha);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }
    public  static class Items extends RecyclerView.ViewHolder{
        TextView horainicio,horafin,tvespecialidad;
        //  TextView especialidadDesc;
        public Items(@NonNull View itemView) {
            super(itemView);
            horainicio = itemView.findViewById(R.id.tvhorainicio);
            horafin = itemView.findViewById(R.id.tvhorafin);
            tvespecialidad=itemView.findViewById(R.id.tvespecialidad);
        }
    }
}
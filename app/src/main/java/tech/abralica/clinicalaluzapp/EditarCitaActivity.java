package tech.abralica.clinicalaluzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.common.base.MoreObjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tech.abralica.clinicalaluzapp.models.Horario;
import tech.abralica.clinicalaluzapp.ui.actiity.ReservarCitaActivity;

public class EditarCitaActivity extends AppCompatActivity implements View.OnClickListener  {


    String key;
    String user_id,isespecialidad,idmedico;

    private DatabaseReference referenceCitas,referenceehoras,referecemedico;
    private DatabaseReference referenceCitas2;
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    private static final String CERO = "0";
    private static final String BARRA = "/";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String DOS_PUNTOS = ":";
    private ProgressDialog progressDialog;
    EditText et_fecha,et_hora;
    TextView etHora;
    Button btnUpdate;
    TextView namemedic;
    ImageButton ibhora,ibfecha;

    RecyclerView recyclerView;
    android.app.AlertDialog.Builder builder2;
    AlertDialog aler2;
    String tokenMedico,namepaciente;

    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cita);

        key=getIntent().getStringExtra("key");
        user_id=getIntent().getStringExtra("user_id");
       // referenceCitas2= FirebaseDatabase.getInstance().getReference("CitasReservadas");
        init();

        //                    bundle.putString("idmedic",idmedic);namepaciente
        idmedico=getIntent().getStringExtra("idmedic");

        namepaciente=getIntent().getStringExtra("namepaciente");
        //   bundle.putString("idEspecialida", idespecia);
        isespecialidad =getIntent().getStringExtra("idEspecialida");
        recyclerView = findViewById(R.id.recyclerhorarios3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        etHora =(TextView)findViewById(R.id.horaselecionada);
        referenceCitas= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(key);
        referenceCitas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    et_fecha.setText(snapshot.child("fecha").getValue().toString());
                    etHora.setText(snapshot.child("hora").getValue().toString());
                    namemedic.setText(snapshot.child("nombreMedico").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        referenceehoras= FirebaseDatabase.getInstance().getReference("HorarioAtencion").child(isespecialidad);

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
    }

    private  void init(){
        et_fecha=(EditText)findViewById(R.id.etFecha1);
        et_hora=(EditText)findViewById(R.id.etHora1);
        btnUpdate=(Button)findViewById(R.id.btnupdate);
        namemedic=(TextView)findViewById(R.id.tbnombremedico);
        ibfecha   =(ImageButton)findViewById(R.id.ib_obtener_fecha1);
       // ibhora=(ImageButton)findViewById(R.id.ib_obtener_hora1);
       // ibhora.setOnClickListener(EditarCitaActivity.this);
        ibfecha.setOnClickListener(EditarCitaActivity.this);
        btnUpdate.setOnClickListener(EditarCitaActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnUpdate.getId()){
            String fe=et_fecha.getText().toString();
            String hor= etHora.getText().toString();
            UpdateCita(fe,hor);
        }
        else if (v.getId()==ibfecha.getId()){
            Dialofecha();
        }
        else{
            Toast.makeText(this, "No click :V", Toast.LENGTH_SHORT).show();
        }
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

                            items.horainicio.setText(horainicio);
                            items.horafin.setText(horafin);

                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //
                                    etHora.setText(horainicio+" - " +horafin);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(EditarCitaActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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
    private void Dialofecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(EditarCitaActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                et_fecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }
    private void DialogoHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(EditarCitaActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                et_hora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        }, hora, minuto, false);
        recogerHora.show();
    }

    private  void UpdateCita(String fecha,String hora){
        if (TextUtils.isEmpty(fecha)){
            Toast.makeText(this, "poner fecha", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(hora)){
            Toast.makeText(this, "poner Hora", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog=new ProgressDialog(EditarCitaActivity.this);
            progressDialog.setTitle("Editar Cita");
            progressDialog.setMessage("cargando...");
            progressDialog.show();
            progressDialog.setCancelable(false);
           // UpdateCita(fe,hor);
            referenceCitas2= FirebaseDatabase.getInstance().getReference("CitasReservadas");
            referenceCitas2.child(key).child("fecha").setValue(fecha);
            referenceCitas2.child(key).child("hora").setValue(hora).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(EditarCitaActivity.this, "Modificado", Toast.LENGTH_SHORT).show();

                        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                        JSONObject json = new JSONObject();
                        try {

                            json.put("to", tokenMedico);
                            JSONObject notificacion = new JSONObject();
                            notificacion.put("titulo", "Se ha Modicado la Cita");
                            notificacion.put("detalle", "Paciente " +namepaciente);
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
                        DialogoOk("SE ha modificado");
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditarCitaActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void DialogoOk(String mensaje){
        builder2 = new AlertDialog.Builder(EditarCitaActivity.this);
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(EditarCitaActivity.this).inflate(R.layout.dialogo_ok, null);
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
                finish();
            }
        });
        aler2  = builder2.create();
        aler2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aler2.show();
    }


    private void DialogoWarning(){
        builder2 = new AlertDialog.Builder(EditarCitaActivity.this);
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(EditarCitaActivity.this).inflate(R.layout.dialogo_warning, null);
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
    public  static class Items extends RecyclerView.ViewHolder{
        TextView horainicio,horafin;
        //  TextView especialidadDesc;
        public Items(@NonNull View itemView) {
            super(itemView);
            horainicio = itemView.findViewById(R.id.tvhorainicio);
            horafin = itemView.findViewById(R.id.tvhorafin);
        }
    }

}
package tech.abralica.clinicalaluzapp.ui.admin;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Especialidad;
import tech.abralica.clinicalaluzapp.models.Horario;

public class HorasCitasActivity extends AppCompatActivity {


    private DatabaseReference referenceehoras;
    StorageReference storageRef;
    CollectionReference espRef;
    private ProgressDialog progressDialog;
    TextInputEditText tenombre,tedescripcion;


    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView friendList;

    android.app.AlertDialog.Builder builder;
    AlertDialog alert;
    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    private DatabaseReference reference;
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String DOS_PUNTOS = ":";

    String fecha;

    String user_id;
    String idespecialidad,nombreespecialidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horas_citas);


        recyclerView = findViewById(R.id.recyclerhorarios);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        idespecialidad=getIntent().getStringExtra("idespecialidad");
        nombreespecialidad=getIntent().getStringExtra("nombrespecialidad");


        referenceehoras= FirebaseDatabase.getInstance().getReference("HorarioAtencion").child(idespecialidad);

        FloatingActionButton fab = findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo("","");
            }
        });

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

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(HorasCitasActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void Dialogo(String idmedico,String nombremedico){
        builder = new AlertDialog.Builder(HorasCitasActivity.this);
        Button btcerrrar,btnreservar;
        ImageButton btnfecha,btnhora;
        EditText hora1,hora2;
        TextView tvfalta;
        View v = LayoutInflater.from(HorasCitasActivity.this).inflate(R.layout.dialogo_addhoras, null);
        builder.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.btncerrar);
        btnreservar=(Button)v.findViewById(R.id.iniciar_button);
        btnfecha=(ImageButton)v.findViewById(R.id.ib_obtener_fecha);
        hora1=(EditText)v.findViewById(R.id.etHora1);
        btnhora=(ImageButton)v.findViewById(R.id.ib_obtener_hora);
        hora2=(EditText)v.findViewById(R.id.etHora2);

        btnfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                //calendariopo

                TimePickerDialog recogerHora = new TimePickerDialog(HorasCitasActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        hora1.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);

                    }
                }, hora, minuto, false);
                recogerHora.show();
            }
        });
        btnhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog recogerHora = new TimePickerDialog(HorasCitasActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        hora2.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);

                    }
                }, hora, minuto, false);
                recogerHora.show();
            }
        });
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        btnreservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horaini =hora1.getText().toString();
                String horafin =hora2.getText().toString();
                if (TextUtils.isEmpty(horaini)){
                    Toast.makeText(HorasCitasActivity.this, "Poner Fecha", Toast.LENGTH_SHORT).show();
                    return;
                }
                alert.dismiss();
                ReservarCita(horaini,horafin);
            }
        });

        alert  = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }


    private  void ReservarCita(String horaini,String horafin)
    {

        ///reservar cita que manda aal bd Firebae
        progressDialog=new ProgressDialog(HorasCitasActivity.this);
        progressDialog.setTitle("Agregado horario");
        progressDialog.setMessage("cargando...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String key = referenceehoras.push().getKey();

        // para gaurar el nodo Citas
        // sadfsdfsdfsd1212sdss
        Horario objecita =new Horario(key,idespecialidad,horaini,horafin,nombreespecialidad  );
    //HorarioAtencion
      //  reference=FirebaseDatabase.getInstance().getReference("HorarioAtencion");
        //  referenceCitas= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id);
        referenceehoras.child(key).setValue(objecita).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(HorasCitasActivity.this, "Agregado", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HorasCitasActivity.this, "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
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
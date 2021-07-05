package tech.abralica.clinicalaluzapp.ui.paciente;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import tech.abralica.clinicalaluzapp.EditarCitaActivity;
import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.adapter.adapterCitas;
import tech.abralica.clinicalaluzapp.models.Citas;
import tech.abralica.clinicalaluzapp.models.ClsCita;

public class GestionarCitasFragment extends Fragment {

    public FirebaseUser currentUser;
    LinearLayoutManager linearLayoutManager;
    RecyclerView friendList;
    String user_id;
    adapterCitas myAdapter;
    ArrayList<ClsCita> listaCitas = new ArrayList<>();
    android.app.AlertDialog.Builder builder;
    AlertDialog alert;
    private DatabaseReference referenceCitas;
    private DatabaseReference referenceCitas2;
   // private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    android.app.AlertDialog.Builder builder2;
    AlertDialog aler2;

    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;
    public GestionarCitasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_gestionar_citas, container, false);
        listaCitas = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
       // Toast.makeText(getContext(), user_id, Toast.LENGTH_SHORT).show();
        referenceCitas = FirebaseDatabase.getInstance().getReference("CitasReservadas");

        friendList = vista.findViewById(R.id.recyclercitas);
        friendList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return vista;
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
                    if (cita.getIdPaciente().equals(user_id)) {
                        listaCitas.add(cita);
                    }
                }
                int count = listaCitas.size();
                Log.e("count", String.valueOf(count));
                //ArrayList<Citas> listaCitas=new ArrayList<>();
                myAdapter = new adapterCitas(listaCitas);
                friendList.setAdapter(myAdapter);
                myAdapter.setOnClickListener(v -> {
                    //   Toast.makeText(getContext(), listaCitas.get(friendList.getChildAdapterPosition(v)).getHora(), Toast.LENGTH_SHORT).show();
                    String idMedico=listaCitas.get(friendList.getChildAdapterPosition(v)).getIdMedico();
                    String estado=listaCitas.get(friendList.getChildAdapterPosition(v)).getEstado();
                    String namepaciene =listaCitas.get(friendList.getChildAdapterPosition(v)).getNombrepaciente();
                    Dialogo(listaCitas.get(friendList.getChildAdapterPosition(v)).getKeycita(),listaCitas.get(friendList.getChildPosition(v)).getIdEspecialida(),estado,idMedico,namepaciene);
                });
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    private void Dialogo(String key,String idespecia,String estado,String idmedic,String namepaciente) {
        builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] items = new CharSequence[3];
        items[0] = "Anular";
        items[1] = "Cambiar Fecha";
        items[2] = "Otros";
        // View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_datetime, null);
        // builder.setView(v);
        builder.setTitle("Acciones").setItems(items, (dialog, posicion) -> {
            if (posicion == 0) {
                if (estado.equals("Confirmado")){
                    DialogoWarning();
                    alert.dismiss();
                }else{
                    Anular(key);
                    alert.dismiss();
                }
            }
            if (posicion == 1) {
                if (estado.equals("Confirmado")){
                    DialogoWarning();
                    alert.dismiss();
                }else{
                    Intent intent = new Intent(getContext(), EditarCitaActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", key);
                    bundle.putString("user_id", user_id);
                    bundle.putString("idEspecialida", idespecia);
                    bundle.putString("idmedic",idmedic);
                    bundle.putString("namepaciente",namepaciente);
                    //idEspecialida
                    intent.putExtras(bundle);
                    startActivity(intent);
                    alert.dismiss();
                }
                // Toast.makeText(getActivity(), "tdoavai no implementado", Toast.LENGTH_SHORT).show();
            }
        });
        alert = builder.create();
        alert.show();
    }

    private void Anular(String key) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando..");
        progressDialog.setTitle("Eliinando");
        progressDialog.show();
        progressDialog.setCancelable(false);
        referenceCitas2 = FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id).child(key);
        referenceCitas2.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Eliminado ", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
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
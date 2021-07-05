package tech.abralica.clinicalaluzapp.ui.paciente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.ui.medico.EditarPerfil;

public class PerfilPacienteFragment extends Fragment {


    View vista;

    CardView careditar;
    ImageView img2;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;

    public FirebaseUser currentUser;
    String urlimageperfil;
    TextView tvnombreprofe,tvapellidoprofe,tvnumeroprofe;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

      vista = inflater.inflate(R.layout.fragment_perfil_paciente, container, false);

        tvnombreprofe=(TextView)vista.findViewById(R.id.tvonombrápaciente);
        tvapellidoprofe=(TextView)vista.findViewById(R.id.tvapellidopaciente);
        tvnumeroprofe=(TextView)vista.findViewById(R.id.tvtelefonopaciente);
        img2=(ImageView)vista.findViewById(R.id.imgpaciente);
        careditar=(CardView)vista.findViewById(R.id.carperfilpaciente);
        careditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activieditarperdil();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            final String  user_uID = mAuth.getCurrentUser().getUid();
            final String  correo = mAuth.getCurrentUser().getEmail();

            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Paciente").child(user_uID);
            userDatabaseReference.keepSynced(true);
            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String nombreprofe = dataSnapshot.child("nombres").getValue().toString();
                    String apellidoprofe = dataSnapshot.child("apellidos").getValue().toString();
                    String numeroprofe = dataSnapshot.child("email").getValue().toString();

                    String fotoperfil = dataSnapshot.child("fotoPerfil").getValue().toString();
                    tvnombreprofe.setText(nombreprofe);
                    tvapellidoprofe.setText(apellidoprofe);
                    tvnumeroprofe.setText(numeroprofe);

                    try {

                        if (fotoperfil.equals("default_image")){
                            img2.setImageResource(R.drawable.default_profile_image);
                        }else{
                            Picasso.get().load(fotoperfil).fit().centerCrop().into(img2);
                        }
                    }catch (Exception ex){
                        Toast.makeText(getActivity(),"Ocurrio un error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("error  ", "error 7");
                }
            });
        }

       return vista;
    }

    private void activieditarperdil() {
        startActivity(new Intent(getContext(), EditarPerfilPacienteActivity.class));
    }

}

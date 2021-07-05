package tech.abralica.clinicalaluzapp.ui.medico;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import tech.abralica.clinicalaluzapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilMedicoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilMedicoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardView careditar;
    ImageView img2;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;

    public FirebaseUser currentUser;
    String urlimageperfil;


    TextView tvnombreprofe,tvapellidoprofe,tvnumeroprofe;


    public PerfilMedicoFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PerfilMedicoFragment newInstance(String param1, String param2) {
        PerfilMedicoFragment fragment = new PerfilMedicoFragment();
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

    View vista;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_perfil_medico, container, false);
        tvnombreprofe=(TextView)vista.findViewById(R.id.idtvnombreprofesor);
        tvapellidoprofe=(TextView)vista.findViewById(R.id.idtvapellidoprofesor);
        tvnumeroprofe=(TextView)vista.findViewById(R.id.idtvnumeroprofesor);
        img2=(ImageView)vista.findViewById(R.id.img4);
        careditar=(CardView)vista.findViewById(R.id.careditarperfil);
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

            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Medico").child(user_uID);
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
        startActivity(new Intent(getContext(),EditarPerfil.class));
    }

}
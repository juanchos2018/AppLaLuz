package tech.abralica.clinicalaluzapp.ui.paciente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Especialidad;
import tech.abralica.clinicalaluzapp.ui.actiity.MedicosActivity;
import tech.abralica.clinicalaluzapp.ui.admin.HorasCitasActivity;
import tech.abralica.clinicalaluzapp.ui.admin.RegistrarEspFragment;

public class ReservarEspFragment extends Fragment {

    RecyclerView recyclerView;
    private DatabaseReference referenceespecialidad;


    private FirebaseFirestore db;
    EditText txtfiltro;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservar_esp, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        init();
        referenceespecialidad= FirebaseDatabase.getInstance().getReference("Especialidades");
        txtfiltro=(EditText)view.findViewById(R.id.txtbuscarespecialidad);

        txtfiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPeopleProfile(txtfiltro.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }
    private void searchPeopleProfile(final String searchString) {
        final Query searchQuery = referenceespecialidad.orderByChild("nombre")
                .startAt(searchString).endAt(searchString + "\uf8ff");
        FirebaseRecyclerOptions<Especialidad> recyclerOptions = new FirebaseRecyclerOptions.Builder<Especialidad>()
                .setQuery(searchQuery, Especialidad.class)
                .build();
        final Context context = getContext();
        FirebaseRecyclerAdapter<Especialidad, Items> adapter = new FirebaseRecyclerAdapter<Especialidad, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items holder, final int position, @NonNull final Especialidad model) {

                holder.especialidadNombre.setText(model.getNombre());
                holder.especialidadDesc.setText(model.getDescripcion());

                final String idespecialidad=model.getIdespecialidad();
                String rutafoto =   model.getUrlImagen();

                Picasso.get().load(rutafoto).fit().centerCrop().into(holder.especialidadImagen);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), MedicosActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("idespecialidad",idespecialidad);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
            }
            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.especialidad_card, parent, false);
                return new Items(vista);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Especialidad> recyclerOptions = new FirebaseRecyclerOptions.Builder<Especialidad>()
                .setQuery(referenceespecialidad, Especialidad.class).build();
        FirebaseRecyclerAdapter<Especialidad,Items> adapter =new FirebaseRecyclerAdapter<Especialidad, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull Especialidad tutores) {
                final String key = getRef(i).getKey();
                referenceespecialidad.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String nombre=dataSnapshot.child("nombre").getValue().toString();
                            final String descripcion=dataSnapshot.child("descripcion").getValue().toString();
                            final String idespecialidad=dataSnapshot.child("idespecialidad").getValue().toString();
                             String rutafoto =   dataSnapshot.child("urlImagen").getValue().toString();
                            items.especialidadNombre.setText(nombre);
                            items.especialidadDesc.setText(descripcion);
                            Picasso.get().load(rutafoto).fit().centerCrop().into(items.especialidadImagen);
                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), MedicosActivity.class);
                                    Bundle bundle= new Bundle();
                                    bundle.putString("idespecialidad",idespecialidad);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.especialidad_card, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public  static class Items extends RecyclerView.ViewHolder{
        ImageView especialidadImagen;
        TextView especialidadNombre;
        TextView especialidadDesc;

        public Items(@NonNull View itemView) {
            super(itemView);
            especialidadImagen = itemView.findViewById(R.id.especialidad_imagen);
            especialidadNombre = itemView.findViewById(R.id.especialidad_nombre);
            especialidadDesc = itemView.findViewById(R.id.especialidad_desc);

        }
    }


}
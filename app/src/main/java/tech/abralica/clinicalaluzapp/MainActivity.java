package tech.abralica.clinicalaluzapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import tech.abralica.clinicalaluzapp.ui.admin.HomeAdminFragment;
import tech.abralica.clinicalaluzapp.ui.admin.RegistrarEspFragment;
import tech.abralica.clinicalaluzapp.ui.admin.RegistrarMedicoFragment;
import tech.abralica.clinicalaluzapp.ui.medico.CitasMedicoFragment;
import tech.abralica.clinicalaluzapp.ui.medico.MedicoHomeFragment;
import tech.abralica.clinicalaluzapp.ui.paciente.GestionarCitasFragment;
import tech.abralica.clinicalaluzapp.ui.paciente.HistorialMedicoFragment;
import tech.abralica.clinicalaluzapp.ui.paciente.HistorialPacienteFragment;
import tech.abralica.clinicalaluzapp.ui.paciente.HomePacienteFragment;
import tech.abralica.clinicalaluzapp.ui.paciente.PerfilPacienteFragment;
import tech.abralica.clinicalaluzapp.ui.paciente.ReservarEspFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // String rol = "Administrador";
    String rol = "Paciente";
    // String rol = "Médico";
    private DrawerLayout drawer;

    private DatabaseReference userDatabaseReference;
    TextView txtnombre,tvtorrreo;
    ImageView  imgusario;

    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public static void navigateTo(Fragment fragment, AppCompatActivity activity) {
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SharedPreferences sharedPref = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        // rol = sharedPref.getString("tipoUsuario", "tipoUsuario");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rol =getIntent().getStringExtra("tipousu");

        if (rol==null || rol ==""){
             SharedPreferences sharedPref = getSharedPreferences("usuario", Context.MODE_PRIVATE);
             rol = sharedPref.getString("tipoUsuario", "tipoUsuario");

        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        txtnombre = navHeader.findViewById(R.id.txtnombreusuario);

        tvtorrreo=navHeader.findViewById(R.id.txtcorreousuario);

        imgusario=(ImageView)navHeader.findViewById(R.id.imgusuario);

        if (currentUser != null){
            final String  user_uID = mAuth.getCurrentUser().getUid();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference(rol).child(user_uID);
            String correo=mAuth.getCurrentUser().getEmail();
            tvtorrreo.setText(correo);
            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.exists()){
                            String nombre = dataSnapshot.child("nombres").getValue().toString();
                            String urlimageperfil = dataSnapshot.child("fotoPerfil").getValue().toString();
                            txtnombre.setText(nombre);
                            if (urlimageperfil.equals("default_image")){
                                imgusario.setImageResource(R.drawable.imgdoctorico);
                            }else{
                                Picasso.get().load(urlimageperfil).fit().centerCrop().into(imgusario);
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        switch (rol) {
            case "Administrador":
                AdministradorOptions.inflateSubmenu(this, navigationView);
                break;
            case "Paciente":
                PacienteOptions.inflateSubmenu(this, navigationView);
                break;
            case "Medico":
                MedicodoOptions.inflateSubmenu(this, navigationView);
                break;

        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            switch (rol) {
                case "Administrador":
                    AdministradorOptions.replaceDefaultFragment(this, navigationView);
                    break;
                case "Paciente":
                    PacienteOptions.replaceDefaultFragment(this, navigationView);
                    break;
                case "Medico":
                    MedicodoOptions.replaceDefaultFragment(this, navigationView);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        //checking logging, if not login redirect to Login ACTIVITY
        if (currentUser == null ){
            mAuth.signOut();
            logOutUser(); // Return to Login activity

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.action_salir){

            if (currentUser != null){
                // userDatabaseReference.child("active_now").setValue(ServerValue.TIMESTAMP);
            }
            mAuth.signOut();
            logOutUser();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logOutUser() {

        Intent loginIntent =  new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (rol) {
            case "Administrador":
                AdministradorOptions.replaceSelectedFragment(this, itemId);
                break;
            case "Paciente":
                PacienteOptions.replaceSelectedFragment(this, itemId);
                break;
            case "Medico":
                //MedicodoOptions
                MedicodoOptions.replaceSelectedFragment(this, itemId);
                break;


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class AdministradorOptions {

        private static final int defaultHome = R.id.nav_home_admin;

        public static void replaceDefaultFragment(AppCompatActivity activity, NavigationView navigationView) {
            MainActivity.navigateTo(new HomeAdminFragment(), activity);
            navigationView.setCheckedItem(defaultHome);
        }

        public static void replaceSelectedFragment(AppCompatActivity activity, int itemId) {
            if (itemId == R.id.nav_home_admin)
                MainActivity.navigateTo(new HomeAdminFragment(), activity);
            else if (itemId == R.id.nav_reg_medico)
                MainActivity.navigateTo(new RegistrarMedicoFragment(), activity);
            else if (itemId == R.id.nav_reg_especialidad)
                MainActivity.navigateTo(new RegistrarEspFragment(), activity);
        }

        public static void inflateSubmenu(AppCompatActivity activity, NavigationView navigationView) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.dw_i_opciones);

            activity.getMenuInflater().inflate(R.menu.admin_menu, item.getSubMenu());
        }
    }

    public static class MedicodoOptions {

        private static final int defaultHome = R.id.nav_inicio_medico;

        public static void replaceDefaultFragment(AppCompatActivity activity, NavigationView navigationView) {
            MainActivity.navigateTo(new MedicoHomeFragment(), activity);
            navigationView.setCheckedItem(defaultHome);
        }

        public static void replaceSelectedFragment(AppCompatActivity activity, int itemId) {
            if (itemId == R.id.nav_inicio_medico)
                MainActivity.navigateTo(new MedicoHomeFragment(), activity);
            else if (itemId == R.id.nav_citas)
                MainActivity.navigateTo(new CitasMedicoFragment(), activity);
            else if (itemId == R.id.nav_historial)
                MainActivity.navigateTo(new tech.abralica.clinicalaluzapp.ui.medico.HistorialMedicoFragment(), activity);

            else if (itemId == R.id.nav_perfilmedico)
                MainActivity.navigateTo(new tech.abralica.clinicalaluzapp.ui.medico.PerfilMedicoFragment(), activity);
            else if (itemId == R.id.nav_clinica)
                MainActivity.navigateTo(new tech.abralica.clinicalaluzapp.ui.ClinicaFragment(), activity);

        }

        public static void inflateSubmenu(AppCompatActivity activity, NavigationView navigationView) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.dw_i_opciones);
            activity.getMenuInflater().inflate(R.menu.medico_menu, item.getSubMenu());
        }

    }
    public static class PacienteOptions {

        private static final int defaultHome = R.id.nav_home_paciente;

        public static void replaceDefaultFragment(AppCompatActivity activity, NavigationView navigationView) {
            MainActivity.navigateTo(new HomePacienteFragment(), activity);
            navigationView.setCheckedItem(defaultHome);
        }

        public static void replaceSelectedFragment(AppCompatActivity activity, int itemId) {
            if (itemId == R.id.nav_home_paciente)
                MainActivity.navigateTo(new HomePacienteFragment(), activity);
            else if (itemId == R.id.nav_reservar_cita)
                MainActivity.navigateTo(new ReservarEspFragment(), activity);
            else if (itemId == R.id.nav_gestionar_citas)
                MainActivity.navigateTo(new GestionarCitasFragment(), activity);
            else if (itemId == R.id.nav_historial_medico)
                MainActivity.navigateTo(new HistorialPacienteFragment(), activity);
            else if (itemId == R.id.nav_perfil_paciente)
                MainActivity.navigateTo(new PerfilPacienteFragment(), activity);

            else if (itemId == R.id.nav_clinica)
                MainActivity.navigateTo(new tech.abralica.clinicalaluzapp.ui.ClinicaFragment(), activity);


        }

        public static void inflateSubmenu(AppCompatActivity activity, NavigationView navigationView) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.dw_i_opciones);

            activity.getMenuInflater().inflate(R.menu.paciente_menu, item.getSubMenu());
        }
    }
}
package tech.abralica.clinicalaluzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

public class UbicacionActivity extends AppCompatActivity  implements OnMapReadyCallback, LocationListener {



    private TextView tvLatitud, tvLongitud, tvAltura, tvPrecision;
    Button btnenviar;
    private GoogleMap mMap;
    double lat, lon;
    double lat_conductir, lon_conductor;
    String key_envio;
    DatabaseReference reference;
    Location location;
    LocationManager lm;
    private LocationManager locManager;
    private Location loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            Log.v("Localizacion", location.getLatitude() + " y " + location.getLongitude());
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat_conductir = -18.0226834;
        lon_conductor=-70.2612516;

        LatLng aquitoy = new LatLng(lat_conductir, lon_conductor);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(aquitoy)
                .zoom(12)//esto es el zoom
                .bearing(30)//esto es la inclonacion
                .build();

        //ENVENTO CLICK PARA EL
        mMap.addMarker(new MarkerOptions().position(aquitoy).title("Clinica la Luz"));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }
}
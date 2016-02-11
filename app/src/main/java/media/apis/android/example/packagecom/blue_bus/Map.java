package media.apis.android.example.packagecom.blue_bus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.GpsStatus.Listener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    float zoomLevel = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onSearch(View view){
        EditText location_SA = (EditText)findViewById(R.id.SearchAddress);
        String location = location_SA.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals(" ")){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e){
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng HeadOffice = new LatLng(51.525408,-0.140841);
        //mMap.addMarker(new MarkerOptions().position(HeadOffice).title("Atos UK & Ireland Head Office"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HeadOffice, zoomLevel));

        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);
        if (locationManager != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);


                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(HeadOffice).title("Atos UK & Ireland Head Office"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HeadOffice, zoomLevel));
            }
        }

    }

    //public void showCurrentLocation(Location location){
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions();
       // markerOptions.position(latLng);
       // markerOptions.title("My Location");
       // mMap.addMarker(markerOptions);//adding marker to the map
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

    //}




}

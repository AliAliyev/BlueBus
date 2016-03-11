package media.apis.android.example.packagecom.blue_bus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

//<<<<<<< HEAD
//=======
//>>>>>>> origin/master

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    float zoomLevel = 12;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



    }

    public void onSearch(View view) {
        EditText location_SA = (EditText) findViewById(R.id.SearchAddress);
        String location = location_SA.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals(" ")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .position(latLng)
                    .title("marker")
                    .snippet("Please select the nearest office and click its name to save")
                    .flat(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//<<<<<<< HEAD
        LatLng HeadOffice = new LatLng(51.525408, -0.140841);
        //mMap.addMarker(new MarkerOptions().position(HeadOffice).title("Atos UK & Ireland Head Office"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HeadOffice, zoomLevel));

        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);
        if (locationManager != null) {
            // if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            //       || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
//=======
        LatLng Andover = new LatLng(51.217607, -0.140841);
        mMap.addMarker(new MarkerOptions().position(Andover).title("Andover - Kingsgate House"));

        LatLng Birmingham = new LatLng(52.467790, -1.723174);
        mMap.addMarker(new MarkerOptions().position(Birmingham).title("Birmingham - Business Park"));
//>>>>>>> origin/master

        LatLng Bristol = new LatLng(51.548890, -2.552829);
        mMap.addMarker(new MarkerOptions().position(Bristol).title("Bristol - Beta Building"));

//<<<<<<< HEAD
                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(HeadOffice).title("Atos UK & Ireland Head Office"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HeadOffice, zoomLevel));
            //}
        }
//=======
        LatLng Cambridge = new LatLng(52.244776, -0.108724);
        mMap.addMarker(new MarkerOptions().position(Cambridge).title("Cambridge - Discovery House"));
//>>>>>>> origin/master

        LatLng Crewe = new LatLng(53.088869, -2.435694);
        mMap.addMarker(new MarkerOptions().position(Crewe).title("Crewe - Rail House"));

        LatLng Darlington = new LatLng(54.546358, -1.572281);
        mMap.addMarker(new MarkerOptions().position(Darlington).title("Darlington - Equinox House"));

        LatLng Dublin = new LatLng(53.334786, -6.253470);
        mMap.addMarker(new MarkerOptions().position(Dublin).title("Dublin - Ireland"));

        LatLng Dundee = new LatLng(56.478894, -2.958642);
        mMap.addMarker(new MarkerOptions().position(Dundee).title(" Dundee - Maryfield House"));

        LatLng Guildford = new LatLng(51.245592, -0.593889);
        mMap.addMarker(new MarkerOptions().position(Guildford).title("Guildford - Deacon Field"));

        LatLng Leeds = new LatLng(53.795883, -1.558285);
        mMap.addMarker(new MarkerOptions().position(Leeds).title("Leeds - Wellington Place"));

        LatLng Linwood = new LatLng(55.844053, -4.476901);
        mMap.addMarker(new MarkerOptions().position(Linwood).title("Linwood - St James Business Park"));

        LatLng Liverpool = new LatLng(53.410934, -2.994645);
        mMap.addMarker(new MarkerOptions().position(Liverpool).title("Liverpool - Old Hall Street"));

        LatLng Livingston = new LatLng(55.888862, -3.557722);
        mMap.addMarker(new MarkerOptions().position(Livingston).title("Livingston - Appleton Place"));

        LatLng London = new LatLng(51.525415, -0.140774);
        mMap.addMarker(new MarkerOptions().position(London).title("London, Triton Square"));

        LatLng Nottingham = new LatLng(52.919606, -1.208643);
        mMap.addMarker(new MarkerOptions().position(Nottingham).title("Nottingham - Building 10.1"));

        LatLng Runcorn = new LatLng(53.349474, -2.657411);
        mMap.addMarker(new MarkerOptions().position(Runcorn).title("Runcorn - Daresbury Court"));

        LatLng Westbury = new LatLng(51.280158, -2.178767);
        mMap.addMarker(new MarkerOptions().position(Westbury).title("Westbury - Heywood House"));

        LatLng Winnersh = new LatLng(51.438568, -0.889851);
        mMap.addMarker(new MarkerOptions().position(Winnersh).title("Winnersh - 1020 Eskdale Road"));

        LatLng Wolverhampton = new LatLng(52.637395, -2.118566);
        mMap.addMarker(new MarkerOptions().position(Wolverhampton).title("Wolverhampton - Trinity Court"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(London, zoomLevel));



        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker arg0) {
                String addressTitle = arg0.getTitle();
                for (int i=0; i<COUNTRIES.length; i++) {
                    if (addressTitle.equals(COUNTRIES[i])) {
                        //decide whether it is the start point or terminal from previous page
                        Bundle extras = getIntent().getExtras();
                        Boolean type = (Boolean)extras.get("startPoint or terminal");
                        Boolean searchPage = (Boolean)extras.get("searchPage or addPage");
                        if (searchPage=true) {
                            //return to Search Page
                            Intent intent = new Intent(Map.this, Search.class);
                            //save(transfer) the option to Search Page
                            intent.putExtra("selectedAddress", addressTitle);
                            intent.putExtra("t", type);
                            startActivity(intent);
                            break;
                        } else {
                            //return to Add Page
                            Intent intent = new Intent(Map.this, Add.class);
                            //save(transfer) the option to Search Page
                            intent.putExtra("selectedAddress", addressTitle);
                            intent.putExtra("t", type);
                            startActivity(intent);
                            break;
                        }
                    }
                }
            }

        });


    }
    private static final String[] COUNTRIES = new String[] {
            "Andover - Kingsgate House", "Birmingham - Business Park", "Bristol - Beta Building",
            "Cambridge - Discovery House", "Crewe - Rail House", "Darlington - Equinox House",
            "Dublin - Ireland","Dundee - Maryfield House", "Guildford - Deacon Field",
            "Leeds - Wellington Place", "Linwood - St James Business Park", "Liverpool - Old Hall Street",
            "Livingston - Appleton Parkway", "London, Triton Square", "Nottingham - Building 10.1",
            "Runcorn - Daresbury Court", "Westbury - Heywood House", "Winnersh – 1020 Eskdale Road",
            "Wolverhampton - Trinity Court"
    };


}


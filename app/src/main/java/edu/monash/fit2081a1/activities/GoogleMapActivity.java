package edu.monash.fit2081a1.activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.monash.fit2081a1.R;
import edu.monash.fit2081a1.databinding.ActivityGoogleMapBinding;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;
    SupportMapFragment mapFragment;
    private String countryToFocus;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        geocoder = new Geocoder(this, Locale.getDefault());

        // set class variable using bundle data
        countryToFocus = getIntent().getExtras().getString("categoryLocation", "Malaysia");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * Change map display type, feel free to explore other available map type:
         * MAP_TYPE_NORMAL: Basic map.
         * MAP_TYPE_SATELLITE: Satellite imagery.
         * MAP_TYPE_HYBRID: Satellite imagery with roads and labels.
         * MAP_TYPE_TERRAIN: Topographic data.
         * MAP_TYPE_NONE: No base map tiles
         */
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // place latitude-longitude values in the order specified
        LatLng malaysia = new LatLng(3.8599933274207, 102.53683911732293);

        // adds a marker to the specified latitude-longitude
        mMap.addMarker(new MarkerOptions().position(malaysia).title("Malaysia"));

        // use moveCamera method to move current map viewing angle to Malaysia
        mMap.moveCamera(CameraUpdateFactory.newLatLng(malaysia));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(10f));

        findCountryMoveCamera();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //save current location
                String msg;
                String selectedCountry;


                List<Address> addresses = new ArrayList<>();
                try {
                    //The results of getFromLocation are a best guess and are not guaranteed to be meaningful or correct.
                    // It may be useful to call this method from a thread separate from your primary UI thread.
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); //last param means only return the first address object
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses.size() == 0) {
                    msg = "Category address not found";
                }
                else {
                    android.location.Address address = addresses.get(0);
                    selectedCountry = address.getCountryName();
                    msg = "The selected country is " + selectedCountry;
                }

                Toast.makeText(GoogleMapActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findCountryMoveCamera() {
        // initialise Geocode to search location using String
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // getFromLocationName method works for API 33 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            /**
             * countryToFocus: String value, any string we want to search
             * maxResults: how many results to return if search was successful
             * successCallback method: if results are found, this method will be executed
             *                          runs in a background thread
             */
            geocoder.getFromLocationName(countryToFocus, 1, addresses -> {
                // if there are results, this condition would ret urn true

                if (!addresses.isEmpty()) {
                    // run on UI thread as the user interface will update once set map location
                    runOnUiThread(() -> {                        // define new LatLng variable using the first address from list of addresses
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );

                        // repositions the camera according to newAddressLocation
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));

                        // just for reference add a new Marker
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(newAddressLocation)
                                        .title(countryToFocus)
                        );

                        // set zoom level to 8.5f or any number between range of 2.0 to 21.0
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10f));
                    });
                } else {
                    runOnUiThread(() -> {
                        LatLng malaysia = new LatLng(3.8599933274207, 102.53683911732293);

                        // adds a marker to the specified latitude-longitude
                        mMap.addMarker(new MarkerOptions().position(malaysia).title("Malaysia"));

                        // use moveCamera method to move current map viewing angle to Malaysia
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(malaysia));

                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10f));
                        Toast.makeText(this, "Category address not found", Toast.LENGTH_SHORT).show();
                    });
                }

            });
        }

    }

}
package com.GOF.cairn;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.GOF.cairn.ui.favourites.FavFragment;
import com.GOF.cairn.ui.map.MapMFragment;
import com.GOF.cairn.ui.preferences.PreferenceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                                                                                                                    //from Mapbox account

        //createMapFragment(savedInstanceState);                                                                  //creates and sets mapFragment


        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);                                         //logical assigning of nav bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new MapMFragment()).commit();           //selects mapFragment on startup
        bottomNav.setSelectedItemId( R.id.navigation_Map);                                                      //sets the middle item as selected on startup (map)
    }


//    private void createMapFragment(Bundle savedInstanceState) {                                                 //creates map fragment
//        if(savedInstanceState == null)
//        {
//            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this, null);          //build the map, sets options of the Map Fragment
//            options.camera(new CameraPosition.Builder().zoom(9).build());                                       //sets Zoom level on map
//
//            mapFragment = SupportMapFragment.newInstance(options);                                              //create the map fragment
//
//            transaction.add(R.id.frag_cont, mapFragment, "com.mapbox.map");                                 //Add fragment to the container
//            transaction.commit();
//        }
//        else
//        {
//            mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentByTag("com.mapbox.map");  //if already created retrieve existing one
//        }
//
//        if(mapFragment != null)
//        {
//            mapFragment.getMapAsync(new OnMapReadyCallback() {                                                  //gets map data from MAPBOX
//                @Override
//                public void onMapReady(@NonNull MapboxMap mapboxMap)
//                {
//                    MainActivity.this.mapboxMap = mapboxMap;                                                    //assigns map to local mapbox variable
//
//                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {                        //visual appearance of the map
//                        @Override
//                        public void onStyleLoaded(@NonNull Style style) {
//                            enableLocationComponent(style);
//                        }
//                    });
//                }
//            });
//        }//if
//
//
//    }
//
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =                                 //for selecting a nav bar item
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.navigation_Pref:
                            selectedFragment  = new PreferenceFragment();
                            break;
                        case R.id.navigation_Map:
                            selectedFragment = new MapMFragment();
                            break;
                        case R.id.navigation_Fav:
                            selectedFragment  = new FavFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,selectedFragment).commit(); //launches fragment
                    return true;
                }

            };
//    @SuppressWarnings( {"MissingPermission"})
//    private void enableLocationComponent(@NonNull Style loadedMapStyle) {                                       //gets permissions and listens for location changes on mapFragment
//
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {                                   // Check if permissions are enabled and if not request
//
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();                             // Get an instance of the LocationComponent.
//            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());  // Activate the LocationComponent
//
//            locationComponent.setLocationComponentEnabled(true);                                                // Enable the LocationComponent so that it's actually visible on the map
//            locationComponent.setCameraMode(CameraMode.TRACKING);                                               // Set the LocationComponent's camera mode
//            locationComponent.setRenderMode(RenderMode.NORMAL);                                                 // Set the LocationComponent's render mode
//        } else {
//            permissionsManager = new PermissionsManager(this);                                           //if no locations granted get locations
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            mapboxMap.getStyle(new Style.OnStyleLoaded() {
//                @Override
//                public void onStyleLoaded(@NonNull Style style) {
//                    enableLocationComponent(style);                                                             //runs at end permission obtaining process - if statement will choose different
//                }
//            });
//        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show(); //tune the user they being a tonsil
//            finish();
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
//    }

}
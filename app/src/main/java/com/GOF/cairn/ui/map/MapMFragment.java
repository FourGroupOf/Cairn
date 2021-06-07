package com.GOF.cairn.ui.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.IntentHelper;
import com.GOF.cairn.R;
import com.GOF.cairn.SavedPOI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class MapMFragment extends Fragment implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener
{


    private MapView mapView;
    public MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private Button btnNavigate;
    private FloatingActionButton lbtnAddLocation, lbtnSearch;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private Point clickedPoint; // for saving lat and long od user defined POI
    public String Units;
    public IntentHelper helpIt = new IntentHelper();
    private static final String TAG = "MapFragment";

    //region Search Variables
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    //endregion



    public View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview. - CALL BEFORE YOU DO ANYTHING WITH A MAP. VERY IMPORTANT.
        Mapbox.getInstance(getContext().getApplicationContext(), getString(R.string.mapbox_access_token));  //gets stuff from mapbox

        View view = inflater.inflate(R.layout.fragment_map_m, container, false);

        btnNavigate = view.findViewById(R.id.btn_start_navigation);
        mapView = view.findViewById(R.id.mv_map_view);

        mapView.onCreate(savedInstanceState);                   //mapview is navigation
        mapView.getMapAsync(this);                      //gets maps

        lbtnAddLocation = view.findViewById(R.id.btnAddLocation);
        lbtnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedPoint == null){
                    Toast.makeText(getActivity(), "Please select a location to add to favourites", Toast.LENGTH_LONG).show();
                    return;
                }
//                AddLandDialog nd = new AddLandDialog(getActivity());
//                nd.show();
//                openDialog();

                AuthHold.getInstance().lati = clickedPoint.latitude();
                AuthHold.getInstance().longi = clickedPoint.longitude();
                helpIt.openIntentAddL(getActivity());

            }
        });

        lbtnSearch = view.findViewById(R.id.btnSearch);
        lbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchActivity();
            }
        });

        return view;
    }

//    public void openDialog()
//    {
//        AddLandDialog ld = new AddLandDialog();
//        ld.show(getActivity().getSupportFragmentManager(), "New Landmark");
//    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {   // Check if permissions are enabled and if not request
            locationComponent = mapboxMap.getLocationComponent();               // Get an instance of the LocationComponent
            locationComponent.activateLocationComponent(LocationComponentActivationOptions
                    .builder(getContext(), loadedMapStyle).build());            // Activate the LocationComponent
            locationComponent.setLocationComponentEnabled(true);                // Enable the LocationComponent so that it's actually visible on the map
            locationComponent.setCameraMode(CameraMode.TRACKING);               // Set the LocationComponent's camera mode
            locationComponent.setRenderMode(RenderMode.NORMAL);                 // Set the LocationComponent's render mode
        } else
        {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }
    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {                 //configures the symbol layer for the map

        loadedMapStyle.addImage("destination-icon-id", BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }
    public void getRoute(Point origin, Point destination) {     //draws the routes on the map
        Units = AuthHold.getInstance().loggedInUser.metric? "metric":"imperial";
        NavigationRoute.builder(getContext())
                .accessToken(getString(R.string.mapbox_access_token))
                .origin(origin)
                .destination(destination)
                .voiceUnits(Units)            //We run a check on the user settings here, metric/imperial
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);


                        if (navigationMapRoute != null) {// Draw the route on the map
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    //Method called whenever the map is clicked - This bit here - We can make a public method, for creating a route
    //call it from another class (find by tag) and have it route from say, a saved destination. Actually, I think getRoute() covers that.
    //we just need to pass in our two points
    @Override
    public boolean onMapClick(@NonNull LatLng point)
    {
        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());       //LatLng point are the co-ords of the clicked location
        clickedPoint = destinationPoint;
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(), locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");   //gonna be honest, dont really know what this does. I do think feature is a location obj
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        getRoute(originPoint, destinationPoint);
        btnNavigate.setEnabled(true);
        btnNavigate.setVisibility(View.VISIBLE);
        handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
        return true;
    }

    //region PERMISSIONS
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onPermissionResult(boolean granted) {   //on the result of a permission request, this is run
        if (granted)
        {
            //if perms are granted, enable the location component
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }
    //endregion

    //region MAPBOX OVERRIDES
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    //endregion

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MapMFragment.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);         //does the permissions and sets up user location on the map
                addDestinationIconSymbolLayer(style);   //configures the layer for map markers
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_location_on_24, null);
                Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                style.addImage(symbolIconId, mBitmap);
                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);// Set up a new symbol layer for displaying the searched location's feature coordinates
                setupLayer(style);

                mapboxMap.addOnMapClickListener(MapMFragment.this); //adds the on click listener for the map
                btnNavigate.setOnClickListener(btn_start_navigation_clicked);//sets the onclick listener for the button
            }
        });
    }
    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }
    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
    }

    private void showSearchActivity() {

        //enableLocationComponent(mapboxMap.getStyle());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .geocodingTypes("poi")
                        .proximity(originPoint)
                        .build(PlaceOptions.MODE_CARDS))
                .build(getActivity());
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    //displays searched location
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs("destination-source-id");
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    LatLng test =  new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude());

                    Point destinationPoint = Point.fromLngLat(test.getLongitude(), test.getLatitude());
                    Point  originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                            locationComponent.getLastKnownLocation().getLatitude());

                    getRoute(originPoint , destinationPoint);
                    btnNavigate.setEnabled(true);
//                    btnNavigate.setBackgroundResource(R.color.ButtonBackground);
                    btnNavigate.setVisibility(View.VISIBLE);
                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);

                }
            }
        }
    }

    private View.OnClickListener btn_start_navigation_clicked = new View.OnClickListener() {        //opens the navigation UI here.button click method here
        @Override
        public void onClick(View v)
        {
            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(false)
                    .build();

            NavigationLauncher.startNavigation(getActivity(), options); // Call this method with Context from within an Activity
        }
    };


    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (!features.isEmpty()) {

            String output = "";
            Feature feature = features.get(0);

            StringBuilder stringBuilder = new StringBuilder();

            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    stringBuilder.append(String.format("%s - %s", entry.getKey(), entry.getValue()));
                    stringBuilder.append(System.getProperty("line.separator"));

                    switch (entry.getKey()) {
                        case "type":
                            output += "Type: " + formatString(entry.getValue() + "\n");
                            break;
                        case "class":
                            output += "Category: " + formatString(entry.getValue() + "\n");
                            break;
                        // case "category_en":
                        //     output += "Category: " + formatString(entry.getValue() + "\n");
                        //     break;
                        case "name":
                            output +=  "Name: " + formatString(entry.getValue() + "\n");
                            break;
                        //case "name_en":
                        //   output += "Name: " + formatString(entry.getValue() + "\n");
                        //    break;
                        // case "maki":=
                        //    output += entry.getKey() + " " + formatString(entry.getValue() + "\n");
                        //    break;
                        default:
                            // code block
                    }
                }

                Log.i("Info", stringBuilder.toString());

                if (output.length() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(output)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

        } else {
            Snackbar.make(getView(), "No location data.", Snackbar.LENGTH_LONG).show();
        }
        return true;
    }
    public String formatString(String entry) {
        String formatted = "";

        formatted = entry.replace("\"", "");
        formatted = formatted.replace("_", " ");
        formatted = formatted.substring(0, 1).toUpperCase() + formatted.substring(1);

        return formatted;
    }




}
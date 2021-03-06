package com.messirvedevs.wufker;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class googleMap extends Fragment
        implements OnMapReadyCallback, AdapterView.OnItemClickListener,
        SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static final String LOGTAG = "googleMapFragment";

    // Almacena si se otorgaron o no los permisos de ubicaci??n.
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // La ubicaci??n geogr??fica donde el dispositivo est?? actualmente situado.
    // Esto significa la ??ltima ubicaci??n conocida entregada por el Fused Location Provider.
    private Location lastKnownLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    // Una ubicaci??n y un zoom por defecto para utilizar en caso que no se concedan los permisos.
    private final LatLng defaultLocation = new LatLng(-29.978488, -71.341683);
    private static final int DEFAULT_ZOOM = 14;

    private GoogleMap map;

    // Usadas para la listview y para posicionar el marcador en el mapa
    private final int MAX_ENTRIES = 10;
    private List<String> placesNames = new ArrayList();
    private List<String> placesVicinity = new ArrayList();
    private List<LatLng> placesLatLngs = new ArrayList();
    private ArrayAdapter adapter;



    private Marker marker;
    MenuItem searchItem;

    ArrayList<String> KEYWORDS = new ArrayList<String>(
            Arrays.asList("veterinaria", "veterinario", "consultorio", "veterinary",
                    "clinica", "cl??nica", "clinic", "hospital", "doctor", "medico", "medical",
                    "mascota", "mascotas", "pet", "tienda", "shop", "centro", "asistencial"));

    public googleMap() {
        // Required empty public constructor
    }

    public static googleMap newInstance(String param1, String param2) {
        googleMap fragment = new googleMap();
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageButton mapButton =  (ImageButton) getView().findViewById(R.id.getPlacesButton);

        // Construye el ListView para mostrar los lugares cercanos
        adapter = new ArrayAdapter(getContext(), R.layout.places_item, placesNames);
        ListView lv = view.findViewById(R.id.listview_places);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (map != null) {
                    map.clear();
                }

                updateLocation();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.map_search, menu);
        searchItem = menu.findItem(R.id.map_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) { return true; }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) { return true; }

    @Override
    public boolean onQueryTextSubmit(String query) { return true; }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    /*
    Cuando se hace click en un item de la lista de places,
    se muestra el mapa con el lugar respectivo marcado.
*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = placesNames.get(position);
        String vicinity = placesVicinity.get(position);
        LatLng latLng = placesLatLngs.get(position);

        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.clearFocus();
        }

        map.clear(); // Clears all the existing markers;
        if (marker != null && marker.getTitle().equals(name)) {
            marker.setTitle("");
            marker.remove();
        } else {
            MarkerOptions markerOptions = new MarkerOptions(); // Creating a marker
            markerOptions.position(latLng); // Setting the position for the marker
            markerOptions.title(name);
            markerOptions.snippet(vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            marker = map.addMarker(markerOptions); // Placing a marker on the touched position

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        updateLocation();
    }

    private void updateLocation() {
        getLocationPermission(); // Solicita el permiso al usuario.
        updateLocationUI();// Active la capa de Mi Ubicaci??n y el control relacionado en el mapa
        getDeviceLocation(); // Obtiene la ubicaci??n actual del dispositivo y establece la posici??n del mapa.
    }

    /*
       Solicita al usuario por los permisos para utilizar la ubicaci??n del dispositivo.
       El resultado de la solicitud es manejado por el callback onRequestPermissionsResult().
     */
    private void getLocationPermission() {
        int permission_status = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission_status == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /*
       Maneja el resultado de la solicitud por permisos de ubicaci??n.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // Si la solicitud es cancelada, el array resultante es vac??o.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }

        updateLocationUI();
    }

    /*
       Actualiza la configuraci??n de la interfaz del mapa en funci??n de si el usuario ha otorgado
       permisos de ubicaci??n.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /*
       Obtiene la ubicaci??n actual del dispositivo, y posiciona la camara del mapa.
     */
    private void getDeviceLocation() {
        // Obtiene la mejor y m??s reciente ubicaci??n del dispositivo, el cual puede ser null
        // en casos raros donde la ubicaci??n no est?? disponible.
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Establece la posici??n de la camara del mapa a la ubicaci??n actual del dispositivo
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                getPlaces(); // Obtiene las veterinarias cercanas a la ubicaci??n del usuario.
                            }
                        } else {
                            Log.d(LOGTAG, "Current location is null. Using defaults.");
                            Log.e(LOGTAG, "Exception: %s", task.getException());
                            map.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getPlaces() {
        if (map == null) {
            return;
        }

        try {
            if (lastKnownLocation != null) {
                StringBuilder sbValue = new StringBuilder(sbMethod());
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(sbValue.toString());
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public StringBuilder sbMethod() {
        // Ubicaci??n Actual
        double mLatitude = lastKnownLocation.getLatitude();
        double mLongitude = lastKnownLocation.getLongitude();

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=1000");
        sb.append("&types=" + "veterinary_care|pet_store");
        sb.append("&sensor=true");
        sb.append("&key=" + getResources().getString(R.string.google_maps_key));

        Log.d("Map", "api: " + sb.toString());

        return sb;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection(); // Creating an http connection to communicate with url
            urlConnection.connect(); // Connecting to url
            iStream = urlConnection.getInputStream();  // Reading data from url

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Download URL", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();
            try {
                jObject = new JSONObject(jsonData[0]);
                places = placeJson.parse(jObject);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            Log.d("Map", "list size: " + list.size());

            placesNames.clear();
            placesVicinity.clear();
            placesLatLngs.clear();
            adapter.notifyDataSetChanged();

            if (list.size() == 0) {
                Toast.makeText(getContext(), "No se encontraron veterinarias cercanas", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> hmPlace = list.get(i); // Getting a place from the places list
                String name = hmPlace.get("place_name"); // Getting name

                for (String key : KEYWORDS) {
                    if (name.toLowerCase().contains(key)) {
                        Log.d(LOGTAG, name);
                        double lat = Double.parseDouble(hmPlace.get("lat")); // Getting latitude of the place
                        double lng = Double.parseDouble(hmPlace.get("lng")); // Getting longitude of the place
                        LatLng latLng = new LatLng(lat, lng);
                        String vicinity = hmPlace.get("vicinity"); // Getting vicinity

                        placesNames.add(name);
                        placesVicinity.add(vicinity);
                        placesLatLngs.add(latLng);
                        break;
                    }
                }
            }

            int entries = Math.min(placesNames.size(), MAX_ENTRIES);
            if (entries == 0) {
                Toast.makeText(getContext(), "No se encontraron veterinarias cercanas", Toast.LENGTH_SHORT).show();
                return;
            }

            placesNames = placesNames.subList(0, entries);
            placesVicinity = placesVicinity.subList(0, entries);
            placesLatLngs = placesLatLngs.subList(0, entries);

            adapter.notifyDataSetChanged(); // Actualiza la lista con los lugares respectivos.
        }
    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {
        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }
}
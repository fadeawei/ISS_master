package com.hfad.pokemonapi;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView latitude;
    private TextView longitude;
    private MapView mapView;
    private TextView latitudeText;
    private TextView longitudeText;

    MapView map = null;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GeoPoint geo = new GeoPoint(latitude, longitude);
        geo.

                Marker startMarker = new Marker(map);
        startMarker.setPosition();
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        wireWidgets();
        getCoordinates();

        public void onResume(){
            super.onResume();
            //this will refresh the osmdroid configuration on resuming.
            //if you make changes to the configuration, use
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
            map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
        }

        public void onPause(){
            super.onPause();
            //this will refresh the osmdroid configuration on resuming.
            //if you make changes to the configuration, use
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            //Configuration.getInstance().save(this, prefs);
            map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
        }


    }

    private void getCoordinates() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.open-notify.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ISService service = retrofit.create(ISService.class);

        Call<ISStatus> ISSResponseCall = service.GetLocation();

        ISSResponseCall.enqueue(new Callback<ISStatus>() {
            @Override
            public void onResponse(Call<ISStatus> call, Response<ISStatus> response) {
                ISS iss_position = response.body().getIss_position();
                Log.d("ENQUEUE", "onResponse: " + iss_position.toString());
            }

            @Override
            public void onFailure(Call<ISStatus> call, Throwable t) {
                Log.d("ENQUEUE", "onFailre " + t.getMessage());
            }
        });

            latitudeText.setText("Latitude: " + latitude);
            longitudeText.setText("Longitude: " + longitude);

        }

    }


    private void wireWidgets() {
        latitude = findViewById(R.id.textView_maps_latitude);
        longitude = findViewById(R.id.textView_maps_longitude);
        mapView = findViewById(R.id.mapView_map);
    }
}

package com.hfad.pokemonapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       wireWidgets();
       getCoordinates();
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

    }

    private void wireWidgets() {
        latitude = findViewById(R.id.textView_maps_latitude);
        longitude = findViewById(R.id.textView_maps_longitude);
        mapView = findViewById(R.id.mapView_map);
    }
}

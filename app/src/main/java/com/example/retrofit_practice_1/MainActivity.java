package com.example.retrofit_practice_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit_practice_1.adapter.CustomAdapter;
import com.example.retrofit_practice_1.model.MovieDatabase;
import com.example.retrofit_practice_1.model.RetroPhoto;
import com.example.retrofit_practice_1.network.GetDataService;
import com.example.retrofit_practice_1.network.GetMovieService;
import com.example.retrofit_practice_1.network.RetrofitClientInstance;
import com.example.retrofit_practice_1.network.RetrofitMovieInstance;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Call<List<RetroPhoto>> call;
    private Call<MovieDatabase> movieCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        final TextView baconIdView = findViewById(R.id.baconIdView);

        /* Create handler for the Retrofit interface */
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        call = service.getAllPhotos();
        call.enqueue(new Callback<List<RetroPhoto>>() {
            @Override
            public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                /* generate the list here */
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        GetMovieService movieService = RetrofitMovieInstance.getRetrofitInstance().create(GetMovieService.class);
        movieCall = movieService.getSmallDataSet();
        movieCall.enqueue(new Callback<MovieDatabase>() {
            @Override
            public void onResponse(Call<MovieDatabase> call, Response<MovieDatabase> response) {
                HashMap<String, Integer> actors = response.body().getActors();
                Integer baconId = actors.get("Kevin Bacon");

                baconIdView.setText("Kevin Bacon's id is "+baconId);

            }

            @Override
            public void onFailure(Call<MovieDatabase> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong with the movie call", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<RetroPhoto> photoList) {
        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(this, photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        call.cancel();
        movieCall.cancel();
    }
}

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
import com.example.retrofit_practice_1.network.GetMovieService;
import com.example.retrofit_practice_1.network.RetrofitMovieInstance;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Call<MovieDatabase> movieCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        final TextView filmCareerTitleView = findViewById(R.id.filmCareerTitleView);

        GetMovieService movieService = RetrofitMovieInstance.getRetrofitInstance().create(GetMovieService.class);
        movieCall = movieService.getSmallDataSet();
        movieCall.enqueue(new Callback<MovieDatabase>() {
            @Override
            public void onResponse(Call<MovieDatabase> call, Response<MovieDatabase> response) {
                progressBar.setVisibility(View.INVISIBLE);
                HashMap<String, Integer> actors = response.body().getActors();
                Integer baconId = actors.get("Kevin Bacon");

                Toast.makeText(MainActivity.this, "Kevin Bacon's id is "+baconId, Toast.LENGTH_SHORT).show();

                filmCareerTitleView.setText("Kevin Bacon's career consists of these films:");

                List<String> filmCareer = response.body().getMoviesByActorId(baconId);

                generateDataList(filmCareer);

            }

            @Override
            public void onFailure(Call<MovieDatabase> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Something went wrong with the movie call", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<String> filmCareer) {
        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(this, filmCareer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieCall.cancel();
    }
}

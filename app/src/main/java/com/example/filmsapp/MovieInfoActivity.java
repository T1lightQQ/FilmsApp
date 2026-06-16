package com.example.filmsapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmsapp.api.ApiConfig;
import com.example.filmsapp.api.OmdbApi;
import com.example.filmsapp.api.RetrofitClient;
import com.example.filmsapp.db.DBHelper;
import com.example.filmsapp.models.Movie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieInfoActivity extends AppCompatActivity
{
    private TextView tvMovieInfo;
    private Button btnFavorite;
    private Button btnBack;

    private DBHelper dbHelper;
    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        tvMovieInfo = findViewById(R.id.tvMovieInfo);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DBHelper(this);

        String searchText = getIntent().getStringExtra("search_text");

        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v ->
        {
            if(currentMovie != null)
            {
                dbHelper.addFavorite(
                        currentMovie.getImdbID(),
                        currentMovie.getTitle()
                );

                Toast.makeText(
                        this,
                        "Добавлено в избранное",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        loadMovie(searchText);
    }

    private void loadMovie(String text)
    {
        OmdbApi api = RetrofitClient.getClient().create(OmdbApi.class);

        Call<Movie> call;

        boolean isId = text.startsWith("tt");

        if(isId)
        {
            call = api.getMovieById(
                    ApiConfig.API_KEY,
                    text,
                    "full",
                    "json"
            );
        }
        else
        {
            call = api.getMovieByTitle(
                    ApiConfig.API_KEY,
                    text,
                    "full",
                    "json"
            );
        }

        call.enqueue(new Callback<Movie>()
        {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response)
            {
                if(!response.isSuccessful() || response.body() == null)
                {
                    showError();
                    return;
                }

                Movie movie = response.body();

                if(movie.getResponse() == null ||
                        movie.getResponse().equals("False"))
                {
                    showError();
                    return;
                }

                currentMovie = movie;

                String info =
                        "Название: " + movie.getTitle() + "\n\n" +
                                "Год: " + movie.getYear() + "\n\n" +
                                "Жанр: " + movie.getGenre() + "\n\n" +
                                "Режиссёр: " + movie.getDirector() + "\n\n" +
                                "Актёры: " + movie.getActors() + "\n\n" +
                                "Описание: " + movie.getPlot();

                tvMovieInfo.setText(info);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t)
            {
                Toast.makeText(
                        MovieInfoActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void showError()
    {
        Toast.makeText(
                this,
                "Фильм не найден",
                Toast.LENGTH_SHORT
        ).show();
    }
}
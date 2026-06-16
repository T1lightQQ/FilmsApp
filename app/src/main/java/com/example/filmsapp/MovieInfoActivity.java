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

        if(text.startsWith("tt"))
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

                Movie m = response.body();

                if(m.getResponse() == null || m.getResponse().equals("False"))
                {
                    showError();
                    return;
                }

                currentMovie = m;

                String info =
                        "Название: " + m.getTitle() + "\n\n" +
                                "Год: " + m.getYear() + "\n" +
                                "Возрастное ограничение: " + m.getRated() + "\n" +
                                "Дата выхода: " + m.getReleased() + "\n" +
                                "Длительность: " + m.getRuntime() + "\n\n" +
                                "Жанр: " + m.getGenre() + "\n\n" +
                                "Режиссёр: " + m.getDirector() + "\n\n" +
                                "Сценарий: " + m.getWriter() + "\n\n" +
                                "Актёры: " + m.getActors() + "\n\n" +
                                "Сюжет: " + m.getPlot() + "\n\n" +
                                "Язык: " + m.getLanguage() + "\n" +
                                "Страна: " + m.getCountry() + "\n\n" +
                                "Награды: " + m.getAwards() + "\n\n" +
                                "Metascore: " + m.getMetascore() + "\n" +
                                "IMDb рейтинг: " + m.getImdbRating() + "\n" +
                                "IMDb голоса: " + m.getImdbVotes() + "\n\n" +
                                "IMDb ID: " + m.getImdbID() + "\n" +
                                "Тип: " + m.getType() + "\n\n" +
                                "DVD: " + m.getDvd() + "\n" +
                                "BoxOffice: " + m.getBoxOffice() + "\n" +
                                "Production: " + m.getProduction() + "\n" +
                                "Website: " + m.getWebsite();

                tvMovieInfo.setText(info);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t)
            {
                Toast.makeText(
                        MovieInfoActivity.this,
                        "Ошибка: " + t.getMessage(),
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
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

    private DBHelper dbHelper;

    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_info);

        tvMovieInfo =
                findViewById(
                        R.id.tvMovieInfo
                );

        btnFavorite =
                findViewById(
                        R.id.btnFavorite
                );

        dbHelper =
                new DBHelper(this);

        String searchText =
                getIntent()
                        .getStringExtra(
                                "search_text"
                        );

        loadMovie(searchText);

        btnFavorite.setOnClickListener(v ->
        {
            if(currentMovie == null)
            {
                return;
            }

            dbHelper.addFavorite(
                    currentMovie.getImdbID()
            );

            Toast.makeText(
                    this,
                    "Добавлено в избранное",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void loadMovie(String text)
    {
        OmdbApi api =
                RetrofitClient
                        .getClient()
                        .create(
                                OmdbApi.class
                        );

        Call<Movie> call;

        if(text.startsWith("tt"))
        {
            call =
                    api.getMovieById(
                            ApiConfig.API_KEY,
                            text
                    );
        }
        else
        {
            call =
                    api.getMovieByTitle(
                            ApiConfig.API_KEY,
                            text
                    );
        }

        call.enqueue(new Callback<Movie>()
        {
            @Override
            public void onResponse(
                    Call<Movie> call,
                    Response<Movie> response
            )
            {
                if(response.body() == null)
                {
                    Toast.makeText(
                            MovieInfoActivity.this,
                            "Фильм не найден",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                Movie movie =
                        response.body();

                currentMovie = movie;

                String info =
                        "Название: " + movie.getTitle() + "\n\n" +
                                "Год: " + movie.getYear() + "\n\n" +
                                "Возрастной рейтинг: " + movie.getRated() + "\n\n" +
                                "Дата выхода: " + movie.getReleased() + "\n\n" +
                                "Продолжительность: " + movie.getRuntime() + "\n\n" +
                                "Жанр: " + movie.getGenre() + "\n\n" +
                                "Режиссёр: " + movie.getDirector() + "\n\n" +
                                "Сценарий: " + movie.getWriter() + "\n\n" +
                                "Актёры: " + movie.getActors() + "\n\n" +
                                "Описание: " + movie.getPlot() + "\n\n" +
                                "Язык: " + movie.getLanguage() + "\n\n" +
                                "Страна: " + movie.getCountry() + "\n\n" +
                                "Награды: " + movie.getAwards() + "\n\n" +
                                "Metascore: " + movie.getMetascore() + "\n\n" +
                                "IMDb рейтинг: " + movie.getImdbRating() + "\n\n" +
                                "IMDb голосов: " + movie.getImdbVotes() + "\n\n" +
                                "IMDb ID: " + movie.getImdbID() + "\n\n" +
                                "Тип: " + movie.getType() + "\n\n" +
                                "DVD: " + movie.getDvd() + "\n\n" +
                                "Сборы: " + movie.getBoxOffice() + "\n\n" +
                                "Production: " + movie.getProduction() + "\n\n" +
                                "Website: " + movie.getWebsite();

                tvMovieInfo.setText(info);
            }

            @Override
            public void onFailure(
                    Call<Movie> call,
                    Throwable t
            )
            {
                Toast.makeText(
                        MovieInfoActivity.this,
                        "Ошибка: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
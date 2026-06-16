package com.example.filmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private Button btnSearch;
    private Button btnFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        btnFavorites = findViewById(R.id.btnFavorites);

        btnSearch.setOnClickListener(v ->
        {
            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SearchActivity.class
                    );

            startActivity(intent);
        });

        btnFavorites.setOnClickListener(v ->
        {
            Intent intent =
                    new Intent(
                            MainActivity.this,
                            FavoritesActivity.class
                    );

            startActivity(intent);
        });
    }
}
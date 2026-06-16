package com.example.filmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmsapp.db.DBHelper;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity
{
    private ListView listFavorites;
    private Button btnBack;

    private DBHelper dbHelper;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listFavorites = findViewById(R.id.listFavorites);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DBHelper(this);

        titles = dbHelper.getFavorites();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        titles
                );

        listFavorites.setAdapter(adapter);


        listFavorites.setOnItemClickListener((parent, view, position, id) ->
        {
            String item = titles.get(position);

            String imdbId = item.split("\n")[1];

            Intent intent =
                    new Intent(
                            FavoritesActivity.this,
                            MovieInfoActivity.class
                    );

            intent.putExtra("search_text", imdbId);

            startActivity(intent);
        });


        btnBack.setOnClickListener(v -> finish());
    }
}
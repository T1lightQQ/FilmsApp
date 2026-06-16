package com.example.filmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmsapp.db.DBHelper;
import com.example.filmsapp.models.FavoriteItem;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity
{
    private ListView listFavorites;
    private Button btnBack;

    private DBHelper dbHelper;

    private ArrayList<FavoriteItem> list;
    private ArrayList<String> displayList;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listFavorites = findViewById(R.id.listFavorites);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DBHelper(this);

        list = dbHelper.getFavorites();


        displayList = new ArrayList<>();

        for(FavoriteItem item : list)
        {
            displayList.add(item.title + "\n" + item.imdbId);
        }

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listFavorites.setAdapter(adapter);


        listFavorites.setOnItemClickListener((parent, view, position, id) ->
        {
            FavoriteItem item = list.get(position);

            Intent intent = new Intent(
                    FavoritesActivity.this,
                    MovieInfoActivity.class
            );

            intent.putExtra("search_text", item.imdbId);

            startActivity(intent);
        });


        listFavorites.setOnItemLongClickListener((parent, view, position, id) ->
        {
            FavoriteItem item = list.get(position);

            dbHelper.deleteFavorite(item.imdbId);

            list.remove(position);
            displayList.remove(position);
            adapter.notifyDataSetChanged();

            Toast.makeText(
                    this,
                    "Удалено из избранного",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        });


        btnBack.setOnClickListener(v -> finish());
    }
}
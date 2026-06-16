package com.example.filmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity
{
    private EditText etSearch;
    private Button btnFind;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        btnFind = findViewById(R.id.btnFind);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
        {
            finish();
        });

        btnFind.setOnClickListener(v ->
        {
            String text = etSearch.getText().toString().trim();

            if(text.isEmpty())
            {
                Toast.makeText(
                        this,
                        "Введите название или IMDb ID",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            SearchActivity.this,
                            MovieInfoActivity.class
                    );

            intent.putExtra("search_text", text);

            startActivity(intent);
        });
    }
}
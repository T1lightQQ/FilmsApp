package com.example.filmsapp.models;

public class FavoriteItem
{
    public String imdbId;
    public String title;

    public FavoriteItem(String imdbId, String title)
    {
        this.imdbId = imdbId;
        this.title = title;
    }
}
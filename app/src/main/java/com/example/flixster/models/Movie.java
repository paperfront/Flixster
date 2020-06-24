package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {

    public static final int POSTER_WIDTH = 120;
    public static final int POSTER_HEIGHT = 180;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w342";

    private double popularity;
    private String posterPath;
    private String title;
    private String overview;
    private String backdropPath;
    private double rating;
    private int id;

    // Constructor for Movie objects
    public Movie(JSONObject jsonObject) throws JSONException {

        popularity = jsonObject.getDouble("popularity");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
    }


    // Loads movies from a JSON array
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }


    // Getters

    public String getTitle() {
        return title;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return BASE_URL + posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return BASE_URL + backdropPath;
    }

    public double getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }
}

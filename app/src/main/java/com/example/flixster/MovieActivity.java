package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.FeaturedAdapter;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMovieBinding;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieActivity extends AppCompatActivity {

    public static final String TAG = "MovieActivity";

    ImageView ivBanner;
    TextView tvTitle;
    TextView tvOverview;
    TextView tvPopularity;
    TextView tvRating;

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieBinding binding = ActivityMovieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        movies = new ArrayList<>();
        ivBanner = binding.ivBanner;
        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        tvPopularity = binding.tvPopularity;
        tvRating = binding.tvRating;

        Movie movie = (Movie) getIntent().getSerializableExtra(MainActivity.KEY_MOVIE);
        tvRating.setText("Average Rating: " + Double.toString(movie.getRating()) + "/10");
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvPopularity.setText("Popularity Score: " + Double.toString(movie.getPopularity()) + "%");

        String imageUrl;
        RoundedCornersTransformation transformation;
        ivBanner.setBackgroundResource(R.drawable.flicks_backdrop_placeholder);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageUrl = movie.getBackdropPath();
            transformation = new RoundedCornersTransformation(5, 0);
            ivBanner.setBackgroundResource(R.drawable.flicks_backdrop_placeholder);
        } else {
            imageUrl = movie.getPosterPath();
            ivBanner.setBackgroundResource(R.drawable.flicks_movie_placeholder);
            transformation = new RoundedCornersTransformation(30, 0);
        }
        Glide.with(this).load(imageUrl).transform(transformation).override(Target.SIZE_ORIGINAL).into(ivBanner);

        //Create on click listener
        FeaturedAdapter.OnClickListener onClickListener = new FeaturedAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Log.d(TAG, "Clicked at position: " + position);
                Intent i = new Intent(MovieActivity.this, MovieActivity.class);
                i.putExtra(MainActivity.KEY_MOVIE, movies.get(position));
                startActivity(i);
            }
        };

        // Create adapter
        final FeaturedAdapter movieAdapter = new FeaturedAdapter(this, movies, onClickListener);
        // Set the adapter on the recycler view
        RecyclerView rvMovies = findViewById(R.id.rvFeatured);
        rvMovies.setAdapter(movieAdapter);
        // Set a layout manager
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        performQuery(movieAdapter, movie);

    }

    private void performQuery(final FeaturedAdapter adapter, Movie movie) {
        String relatedQuery = "https://api.themoviedb.org/3/movie/%s/recommendations?api_key=%s&language=en-US&page=1";
        relatedQuery = String.format(relatedQuery, Integer.toString(movie.getId()), getString(R.string.movies_api_key));
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(relatedQuery, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "API Request Succeeded");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "API Request failed: " + response);
            }
        });
    }
}

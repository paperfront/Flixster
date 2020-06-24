package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;
    private FeaturedAdapter.OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(int position);
    }

    public FeaturedAdapter(Context context, List<Movie> movies, FeaturedAdapter.OnClickListener onClickListener) {
        this.context = context;
        this.movies = movies;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_featured, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivFeatured;

        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFeatured = itemView.findViewById(R.id.ivFeatured);
            relativeLayout = itemView.findViewById(R.id.relativeFeatured);
        }

        public void bind(Movie movie) {

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });

            String imageUrl;
            RoundedCornersTransformation transformation;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                transformation = new RoundedCornersTransformation(5, 0);
                ivFeatured.setBackgroundResource(R.drawable.flicks_backdrop_placeholder);
            } else {
                imageUrl = movie.getPosterPath();
                ivFeatured.setBackgroundResource(R.drawable.flicks_movie_placeholder);
                transformation = new RoundedCornersTransformation(30, 0);
            }
            Glide.with(context).load(imageUrl).transform(transformation).override(Target.SIZE_ORIGINAL).into(ivFeatured);
        }
    }
}

package com.mirzaadil.task.view.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mirzaadil.task.R;
import com.mirzaadil.task.model.Movie;

import java.util.List;

/**
 * Created by mirzaadil on 8/24/17.
 */

public class MovieAdapterMain extends RecyclerView.Adapter<MovieAdapterMain.MovieViewHolder> {
    private List<Movie> movies;
    private int rowLayout;
    private Context mcontext;

    public MovieAdapterMain(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.mcontext = context;


    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieAdapterMain.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.movieTitle.setText(movies.get(position).getTitle());
        String imgUri = movies.get(position).getPosterPath();
        final Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + imgUri);
        holder.posterImage.setImageURI(uri);



    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        SimpleDraweeView posterImage;


        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.tv_movie_title);
            posterImage = (SimpleDraweeView) v.findViewById(R.id.img_poster);

        }
    }


}

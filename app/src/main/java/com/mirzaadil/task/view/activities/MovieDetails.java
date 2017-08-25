package com.mirzaadil.task.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mirzaadil.task.R;
import com.mirzaadil.task.model.Movie;

import java.net.URI;

public class MovieDetails extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarMovieDetail= null;
    private Toolbar toolbarMovieDetails;
    private SimpleDraweeView posterImage;
    private TextView textViewTitle;
    private TextView textViewRealeaseDate;
    private TextView textViewRating;
    private  TextView textViewDescrition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //title = ;
        initView();
        setSupportActionBar(toolbarMovieDetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Movie Details");

        final Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/"+getIntent().getStringExtra("poster_path"));
        posterImage.setImageURI(uri);
        textViewTitle.setText(getIntent().getStringExtra("title"));
        textViewRealeaseDate.setText(getIntent().getStringExtra("release_date"));
        textViewRating.setText(getIntent().getStringExtra("vote_avg"));
        textViewDescrition.setText(getIntent().getStringExtra("description"));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if(id == android.R.id.home){
            Intent i= new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
private void initView(){
    collapsingToolbarMovieDetail = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
    toolbarMovieDetails = (Toolbar) findViewById(R.id.toolbar_);
    textViewTitle = (TextView) findViewById(R.id.title);
    textViewRealeaseDate = (TextView) findViewById(R.id.realease_date);
    textViewRating = (TextView) findViewById(R.id.rating);
    textViewDescrition = (TextView) findViewById(R.id.description);
    posterImage = (SimpleDraweeView) findViewById(R.id.poster);
}


}






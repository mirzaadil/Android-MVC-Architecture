package com.mirzaadil.task.view.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mirzaadil.task.R;
import com.mirzaadil.task.controller.interfaces.ClickListener;
import com.mirzaadil.task.controller.rest.ApiClient;
import com.mirzaadil.task.controller.rest.ApiInterface;
import com.mirzaadil.task.controller.utils.ApplicationConstants;
import com.mirzaadil.task.controller.utils.GridSpacingItemDecoration;
import com.mirzaadil.task.controller.utils.RecyclerTouchListener;
import com.mirzaadil.task.model.Movie;
import com.mirzaadil.task.model.MoviesResponse;
import com.mirzaadil.task.view.adapters.MovieAdapterMain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private LottieAnimationView animationViewList;
    private RecyclerView recyclerViewSearchMovies;
    private RelativeLayout rlManageAnimation;
    private MaterialSearchView searchView;
    private Toolbar searchToolbar;
    private List<Movie> movies;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseCrash.log("Search Activity");
        animationViewList = (LottieAnimationView) findViewById(R.id.animation_view_list);
        rlManageAnimation = (RelativeLayout) findViewById(R.id.rl_manage_anim);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(searchToolbar);
        getSupportActionBar().setTitle("Search Movies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                apiCall(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {

                } else {

                    apiCall(newText);
                }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                rlManageAnimation.setVisibility(View.VISIBLE);
                animationViewList.playAnimation();

            }

            @Override
            public void onSearchViewClosed() {
                animationViewList.cancelAnimation();
                rlManageAnimation.setVisibility(View.INVISIBLE);
            }
        });

        recyclerViewSearchMovies = (RecyclerView) findViewById(R.id.search_movies_recycler_view);
        recyclerViewSearchMovies.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewSearchMovies.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerViewSearchMovies.setItemAnimator(new DefaultItemAnimator());

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void apiCall(String query) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.SearchMovie(ApplicationConstants.API_KEY, query);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                animationViewList.cancelAnimation();
                rlManageAnimation.setVisibility(View.INVISIBLE);
                int statusCode = response.code();
                movies = response.body().getResults();
                recyclerViewSearchMovies.setAdapter(new MovieAdapterMain(movies, R.layout.list_item_movie_main, getApplicationContext()));
                recyclerViewSearchMovies.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewSearchMovies, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Movie movie = movies.get(position);
                        Intent intent = new Intent(SearchActivity.this, MovieDetails.class);
                        intent.putExtra("title", movie.getTitle());
                        intent.putExtra("release_date", movie.getReleaseDate());
                        String value = String.valueOf(movie.getVoteAverage());
                        intent.putExtra("vote_avg", value);
                        intent.putExtra("description", movie.getOverview());
                        intent.putExtra("poster_path", movie.getPosterPath());

                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }


            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Search Activty", t.toString());
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setmFirebaseAnalytics() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Movies Search");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}
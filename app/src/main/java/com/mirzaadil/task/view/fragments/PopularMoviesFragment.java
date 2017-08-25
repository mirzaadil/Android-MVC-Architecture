package com.mirzaadil.task.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.mirzaadil.task.R;
import com.mirzaadil.task.controller.interfaces.ClickListener;
import com.mirzaadil.task.controller.rest.ApiClient;
import com.mirzaadil.task.controller.rest.ApiInterface;
import com.mirzaadil.task.controller.utils.ApplicationConstants;
import com.mirzaadil.task.controller.utils.ApplicationUtils;
import com.mirzaadil.task.controller.utils.GridSpacingItemDecoration;
import com.mirzaadil.task.controller.utils.RecyclerTouchListener;
import com.mirzaadil.task.model.Movie;
import com.mirzaadil.task.model.MoviesResponse;
import com.mirzaadil.task.view.activities.MovieDetails;
import com.mirzaadil.task.view.adapters.MovieAdapterMain;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by mirzaadil on 8/24/17.
 */

public class PopularMoviesFragment extends Fragment {
    private Context mcontext;
    private RecyclerView recyclerViewPopularMovies;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseCrash.log("Popular Activity");
        mcontext = getActivity();

        initView(view);
        if (ApplicationUtils.isNetworkAvailable()) {

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("AWOK Task");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Please wait for loading...");
            mProgressDialog.setIcon(R.mipmap.ic_launcher);
            mProgressDialog.show();
            callAPI();
        } else {
            ApplicationUtils.showToast(ApplicationConstants.NETWORK_MESSAGE);
        }

        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void initView(View view) {
        recyclerViewPopularMovies = (RecyclerView) view.findViewById(R.id.popular_movies_recycler_view);
        recyclerViewPopularMovies.setLayoutManager(new GridLayoutManager(mcontext, 2));
        recyclerViewPopularMovies.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerViewPopularMovies.setItemAnimator(new DefaultItemAnimator());
    }

    private void callAPI() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getPopularMovies(ApplicationConstants.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                mProgressDialog.dismiss();
                int statusCode = response.code();

                final List<Movie> movies = response.body().getResults();
                recyclerViewPopularMovies.setAdapter(new MovieAdapterMain(movies, R.layout.list_item_movie_main, mcontext));
                recyclerViewPopularMovies.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewPopularMovies, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Movie movie = movies.get(position);
                        Intent intent = new Intent(getActivity(), MovieDetails.class);
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
                Log.e(TAG, t.toString());
                mProgressDialog.dismiss();

            }
        });
    }

    private void setmFirebaseAnalytics() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Popular Movies");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Popular");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}

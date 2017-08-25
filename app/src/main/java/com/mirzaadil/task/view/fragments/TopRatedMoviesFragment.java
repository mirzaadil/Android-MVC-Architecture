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

public class TopRatedMoviesFragment extends Fragment {
    private Context mcontext;
    private RecyclerView recyclerViewTopRatedMovies;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_rated_movies, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseCrash.log("Top Rated Moves Activity");
        mcontext = getActivity();
        initView(view);
        if (ApplicationUtils.isNetworkAvailable()) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("AWOK Task");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Please wait for loading...");
            mProgressDialog.setIcon(R.mipmap.ic_launcher);
            mProgressDialog.show();
            callApi();
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
        recyclerViewTopRatedMovies = (RecyclerView) view.findViewById(R.id.top_rated_movies_recycler_view);
        recyclerViewTopRatedMovies.setLayoutManager(new GridLayoutManager(mcontext, 2));
        recyclerViewTopRatedMovies.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerViewTopRatedMovies.setItemAnimator(new DefaultItemAnimator());
    }


    private void callApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(ApplicationConstants.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                mProgressDialog.dismiss();
                final List<Movie> movies = response.body().getResults();
                recyclerViewTopRatedMovies.setAdapter(new MovieAdapterMain(movies, R.layout.list_item_movie_main, mcontext));
                recyclerViewTopRatedMovies.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewTopRatedMovies, new ClickListener() {
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

    private void firebaseAnalytics() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Top Rated");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Top Rated Move");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}

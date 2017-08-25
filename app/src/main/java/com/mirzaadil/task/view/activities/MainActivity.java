package com.mirzaadil.task.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.mirzaadil.task.R;
import com.mirzaadil.task.view.fragments.PopularMoviesFragment;
import com.mirzaadil.task.view.fragments.TopRatedMoviesFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayoutMain;
    private NavigationView navigationView;
    private Toolbar mtoolbar;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseCrash.log("Main Activity");
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new TopRatedMoviesFragment());
        fragmentTransaction.commit();
        initNavigationDrawer();


    }

    public void initNavigationDrawer() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Create a new fragment and specify the fragment to show based on nav item clicked
                Fragment mfragment = null;
                Class fragmentClass = null;
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.top_movies:

                        TopRatedMoviesFragment topRatedMoviesFragment = new TopRatedMoviesFragment();
                        FragmentTransaction ftTopRatedMovies = getSupportFragmentManager().beginTransaction();
                        ftTopRatedMovies.replace(R.id.flContent, topRatedMoviesFragment);
                        ftTopRatedMovies.commit();
                        drawerLayoutMain.closeDrawers();
                        break;
                    case R.id.poular_movies:
                        PopularMoviesFragment popularMoviesFragment = new PopularMoviesFragment();
                        FragmentTransaction ftpopular = getSupportFragmentManager().beginTransaction();
                        ftpopular.replace(R.id.flContent, popularMoviesFragment);
                        ftpopular.commit();
                        drawerLayoutMain.closeDrawers();
                        break;
                    case R.id.search_movies:
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        drawerLayoutMain.closeDrawers();
                        break;
                }
                try {
                    mfragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                menuItem.setChecked(true);
                // Set action bar title
                setTitle(menuItem.getTitle());
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_header = (TextView) header.findViewById(R.id.txt_header);
        tv_header.setText("AWOK.com");
        tv_header.setTextSize(25);
        drawerLayoutMain = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutMain, mtoolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayoutMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void firebaseAnalyticslog() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "DashBoard");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}

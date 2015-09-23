package com.zyj.dribbbleclient.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zyj.dribbbleclient.app.R;
import com.zyj.dribbbleclient.app.api.DribbbleService;
import com.zyj.dribbbleclient.app.db.DbShots;
import com.zyj.dribbbleclient.app.model.Shot;
import com.zyj.dribbbleclient.app.model.Shots;
import com.zyj.dribbbleclient.app.ui.adapter.ShotAdapter;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import se.emilsjolander.sprinkles.Query;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static final String DRIBBBLE_END_POINT = "http://api.dribbble.com/";

    private DribbbleService service;

    private String mShotType;

    private ShotAdapter mShotsAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private Callback<Shots> mShotListCallback = new Callback<Shots>() {
        @Override
        public void onResponse(Response<Shots> response) {
            Shots shots = response.body();
            if (shots != null) {
                mSwipeRefreshLayout.setRefreshing(false);
                mShotsAdapter.setShots(shots.shots);
                mShotsAdapter.notifyDataSetChanged();
                // Save into db
                String type = null;
                if (response.raw().request().urlString().contains(DribbbleService.POPULAR)) {
                    type = DribbbleService.POPULAR;
                } else if (response.raw().request().urlString().contains(DribbbleService.DEBUTS)) {
                    type = DribbbleService.DEBUTS;
                } else if (response.raw().request().urlString().contains(DribbbleService.EVERYONE)) {
                    type = DribbbleService.EVERYONE;
                }
                DbShots dbShots = getDbShots();
                if (dbShots == null) {
                    dbShots = new DbShots();
                }
                dbShots.type = type;
                dbShots.body = shots.toJson();
                dbShots.save();
            }
        }

        @Override
        public void onFailure(Throwable error) {
            Log.e("ShotList", error.getMessage());
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        onSectionAttached(0);
        getShotList(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup swipe refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShotListFromServer(1);
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(mDrawerToggle);

        // NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup listview
        ListView listView = (ListView) findViewById(android.R.id.list);
        mShotsAdapter = new ShotAdapter(this);
        listView.setAdapter(mShotsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view.findViewById(R.id.shot);
                String transitionName = getString(R.string.transition_name);
                Shot shot = (Shot) mShotsAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("shot", shot);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, imageView, transitionName);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            }
        });
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DRIBBBLE_END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DribbbleService.class);
    }

    private void getShotList(int position) {
        // Get shot list
        switch (position) {
            case 0:
                mShotType = DribbbleService.POPULAR;
                break;
            case 1:
                mShotType = DribbbleService.DEBUTS;
                break;
            case 2:
                mShotType = DribbbleService.EVERYONE;
                break;
        }
        mSwipeRefreshLayout.setRefreshing(true);

        // Load cached data
        DbShots dbShots = getDbShots();
        if (dbShots != null) {
            Gson gson = new Gson();
            Shots shots = gson.fromJson(dbShots.body, Shots.class);
            mShotsAdapter.setShots(shots.shots);
            mShotsAdapter.notifyDataSetChanged();
        }

        // Get shots from server
        getShotListFromServer(1);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int position = 0;
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_1:
                position = 0;
                break;
            case R.id.navigation_item_2:
                position = 1;
                break;
            case R.id.navigation_item_3:
                position = 2;
                break;
        }
        getShotList(position);
        onSectionAttached(position);
        restoreActionBar();
        return true;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private void getShotListFromServer(int page) {
        service.shotList(mShotType, page).enqueue(mShotListCallback);
    }

    private DbShots getDbShots() {
        return Query.one(DbShots.class,
                "select * from shots where type=?", mShotType).get();
    }
}

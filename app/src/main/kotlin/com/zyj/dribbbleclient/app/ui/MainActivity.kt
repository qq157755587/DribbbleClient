package com.zyj.dribbbleclient.app.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import com.zyj.dribbbleclient.app.R
import com.zyj.dribbbleclient.app.api.Restful
import com.zyj.dribbbleclient.app.db.DataBase
import com.zyj.dribbbleclient.app.db.DbShots
import com.zyj.dribbbleclient.app.model.Shot
import com.zyj.dribbbleclient.app.ui.adapter.ShotAdapter
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit


public class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mShotsAdapter: ShotAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        getShotList()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle!!.syncState()
    }

    private fun initView() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar.setDisplayShowTitleEnabled(true)

        // Setup swipe refresh layout
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light)
        mSwipeRefreshLayout!!.setOnRefreshListener { getShotListFromServer() }

        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawerLayout!!.setDrawerListener(mDrawerToggle)

        // NavigationView
        val navigationView = findViewById(R.id.navigation_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        // Setup listview
        val listView = findViewById(android.R.id.list) as ListView
        mShotsAdapter = ShotAdapter(this)
        listView.adapter = mShotsAdapter
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val imageView = view.findViewById(R.id.shot) as ImageView
                val transitionName = getString(R.string.transition_name)
                val shot = mShotsAdapter!!.getItem(position) as Shot
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("shot", shot)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity, imageView, transitionName)
                ActivityCompat.startActivity(this@MainActivity, intent, options.toBundle())
            }
        }
    }

    private fun getShotList() {
        // Get shot list
        mSwipeRefreshLayout!!.isRefreshing = true

        // Load cached data
        val dbShots = DataBase.getShots()
        if (dbShots != null) {
            val gson = Gson()
            val shots = gson.fromJson(dbShots.body, Array<out Shot>::class.java)
            mShotsAdapter!!.setShots(shots)
            mShotsAdapter!!.notifyDataSetChanged()
        }

        // Get shots from server
        getShotListFromServer()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        // todo
        mDrawerLayout!!.closeDrawer(Gravity.START)
        return true
    }

    private fun getShotListFromServer() {
        Restful.getService().shotList().enqueue(object: Callback<Array<out Shot>> {
            override fun onResponse(response: Response<Array<out Shot>>, retrofit: Retrofit) {
                val shots = response.body()
                if (shots != null) {
                    mSwipeRefreshLayout!!.isRefreshing = false
                    mShotsAdapter!!.setShots(shots)
                    mShotsAdapter!!.notifyDataSetChanged()
                    // Save into db
                    var dbShots = DataBase.getShots()
                    if (dbShots == null) {
                        dbShots = DbShots()
                    }
                    dbShots.body = Gson().toJson(shots)
                    dbShots.save()
                }
            }

            override fun onFailure(t: Throwable) {
                mSwipeRefreshLayout!!.isRefreshing = false
                Toast.makeText(MainActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
    }
}

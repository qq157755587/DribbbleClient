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
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import com.google.gson.Gson
import com.zyj.dribbbleclient.app.R
import com.zyj.dribbbleclient.app.api.DribbbleService
import com.zyj.dribbbleclient.app.api.Restful
import com.zyj.dribbbleclient.app.db.DataBase
import com.zyj.dribbbleclient.app.db.DbShots
import com.zyj.dribbbleclient.app.model.Shot
import com.zyj.dribbbleclient.app.model.Shots
import com.zyj.dribbbleclient.app.ui.adapter.ShotAdapter
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit


public class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    /**
     * Used to store the last screen title. For use in [.restoreActionBar].
     */
    private var mShotType: String = DribbbleService.POPULAR
    private var mShotsAdapter: ShotAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        switchType(0)
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
        mSwipeRefreshLayout!!.setOnRefreshListener { getShotListFromServer(1) }

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
        val dbShots = DataBase.getShots(mShotType)
        if (dbShots != null) {
            val gson = Gson()
            val shots = gson.fromJson(dbShots.body, Shots::class.java)
            mShotsAdapter!!.setShots(shots.shots)
            mShotsAdapter!!.notifyDataSetChanged()
        }

        // Get shots from server
        getShotListFromServer(1)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var position = 0
        when (menuItem.itemId) {
            R.id.navigation_item_1 -> position = 0
            R.id.navigation_item_2 -> position = 1
            R.id.navigation_item_3 -> position = 2
        }
        switchType(position)
        getShotList()
        mDrawerLayout!!.closeDrawer(Gravity.START)
        return true
    }

    private fun switchType(position: Int) {
        when (position) {
            0 -> {
                supportActionBar.title = getString(R.string.title_section1)
                mShotType = DribbbleService.POPULAR
            }
            1 -> {
                supportActionBar.title = getString(R.string.title_section2)
                mShotType = DribbbleService.DEBUTS
            }
            2 -> {
                supportActionBar.title = getString(R.string.title_section3)
                mShotType = DribbbleService.EVERYONE
            }
        }
    }

    private fun getShotListFromServer(page: Int) {
        Restful.getService().shotList(mShotType, page).enqueue(object: Callback<Shots> {
            override fun onResponse(response: Response<Shots>, retrofit: Retrofit) {
                val shots = response.body()
                if (shots != null) {
                    mSwipeRefreshLayout!!.isRefreshing = false
                    mShotsAdapter!!.setShots(shots.shots)
                    mShotsAdapter!!.notifyDataSetChanged()
                    // Save into db
                    var type: String? = null
                    if (response.raw().request().urlString().contains(DribbbleService.POPULAR)) {
                        type = DribbbleService.POPULAR
                    } else if (response.raw().request().urlString().contains(DribbbleService.DEBUTS)) {
                        type = DribbbleService.DEBUTS
                    } else if (response.raw().request().urlString().contains(DribbbleService.EVERYONE)) {
                        type = DribbbleService.EVERYONE
                    }
                    var dbShots = DataBase.getShots(mShotType)
                    if (dbShots == null) {
                        dbShots = DbShots()
                    }
                    dbShots.type = type
                    dbShots.body = shots.toJson()
                    dbShots.save()
                }
            }

            override fun onFailure(t: Throwable) {
                mSwipeRefreshLayout!!.isRefreshing = false
            }
        })
    }
}

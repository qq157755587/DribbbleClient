package com.zyj.dribbbleclient.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zyj.dribbbleclient.app.R;
import com.zyj.dribbbleclient.app.api.DribbbleService;
import com.zyj.dribbbleclient.app.model.Shots;
import com.zyj.dribbbleclient.app.ui.MainActivity;
import com.zyj.dribbbleclient.app.ui.adapter.ShotAdapter;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String DRIBBBLE_END_POINT = "http://api.dribbble.com";

    private DribbbleService service;

    private String mShotType;

    private ShotAdapter mShotsAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Callback<Shots> mShotListCallback = new Callback<Shots>() {
        @Override
        public void success(Shots shots, Response response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mShotsAdapter.setShots(shots.shots);
            mShotsAdapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("ShotList", error.getMessage());
        }
    };

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DRIBBBLE_END_POINT)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLog(new AndroidLog("Dribbble RESTful"))
                .build();
        service = restAdapter.create(DribbbleService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        init(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void init(View rootView) {
        // Setup swipe refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Setup listview
        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        mShotsAdapter = new ShotAdapter(getActivity());
        listView.setAdapter(mShotsAdapter);

        // Get shot list
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        switch (sectionNumber) {
            case 1:
                mShotType = DribbbleService.POPULAR;
                break;
            case 2:
                mShotType = DribbbleService.DEBUTS;
                break;
            case 3:
                mShotType = DribbbleService.EVERYONE;
                break;
        }
        mSwipeRefreshLayout.setRefreshing(true);
        getShotList(1);
    }

    private void getShotList(int page) {
        service.shotList(mShotType, page, mShotListCallback);
    }

    @Override
    public void onRefresh() {
        getShotList(1);
    }
}

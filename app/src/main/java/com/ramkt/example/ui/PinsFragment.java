/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.example.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ramkt.example.R;
import com.ramkt.example.response.Pins;
import com.ramkt.example.utils.Logger;

import java.util.List;

/**
 * PinsFragment class to show the staggered pin UI
 * and can be reused anywhere in the app
 */
public class PinsFragment extends Fragment implements PinsAdapter.IPinAdapterListener {
    private static final String TAG = PinsFragment.class.getSimpleName();
    private ProgressBar mProgressBar;
    private RelativeLayout mErrorLayout;
    private Button mButtonRetry;
    private RecyclerView mRecyclerView;
    private PinsAdapter mAdapter;
    private List<Pins> mPinList;
    private int mLoadingItemIndex;
    private FrameLayout mLoaderLayout;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i(TAG, "On Create view");
        View view = inflater.inflate(R.layout.fragment_pins, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rcv_search);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_Bar);
        mErrorLayout = (RelativeLayout) view.findViewById(R.id.rl_error);
        mButtonRetry = (Button) view.findViewById(R.id.btn_retry);
        mLoaderLayout = (FrameLayout) view.findViewById(R.id.fl_loader);
        showProgressBar();
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((HomeActivity) getActivity()).getPins();
            }
        });
        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.cardview_dark_background),
                getResources().getColor(R.color.cardview_light_background),
                getResources().getColor(R.color.cardview_dark_background),
                getResources().getColor(R.color.cardview_light_background));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.i(TAG, "On Start");
        mButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPins();
            }
        });
    }

    /**
     * Method called click on retry after initial download fails
     */
    public void getPins() {
        showProgressBar();
        ((HomeActivity) getActivity()).getPins();
    }

    /**
     * Method to show the progress bar when user type on search query
     */
    public void showProgressBar() {
        mLoaderLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Method called by Activity to set the data to the adapter
     *
     * @param pins
     */
    public void setPinList(List<Pins> pins) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeLayout.setRefreshing(false);
        if (pins.isEmpty()) {
            mErrorLayout.setVisibility(View.VISIBLE);
        } else {
            mLoaderLayout.setVisibility(View.GONE);
            Logger.i(TAG, "set Pin list in recycler view." + mPinList);
            if (mPinList == null) {
                mPinList = pins;
                RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new PinsAdapter(getActivity(), mPinList, mRecyclerView, this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Logger.i(TAG, "set Pin list in recycler view , size : " + mPinList.size());
                mPinList = pins;
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Method called by Activity if error occurs
     */
    public void error() {
        if (mPinList == null) {
            mLoaderLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Call back method listens to user click on pins
     */
    @Override
    public void onItemClick(Pins pin) {
        ((HomeActivity) getActivity()).showDetailsFragment(pin);
    }
}

package com.pcs.hackathonandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.adapters.FriendRecyclerViewAdapter;
import com.pcs.hackathonandroid.beans.DummyContent;
import com.pcs.hackathonandroid.beans.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FriendFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private FriendRecyclerViewAdapter adapter;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        ButterKnife.bind(this, view);

        // Set the adapter
        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new FriendRecyclerViewAdapter(DummyContent.ITEMS, mListener);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));

        // SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(() -> loadRecyclerViewData());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            // Fetching data from server
            loadRecyclerViewData();
        });

        return view;
    }

    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);
        adapter = new FriendRecyclerViewAdapter(DummyContent.ITEMS, mListener);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(User user);
    }
}

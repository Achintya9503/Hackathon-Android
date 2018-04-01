package com.pcs.hackathonandroid.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.adapters.AddFriendAdapter;
import com.pcs.hackathonandroid.adapters.FriendRecyclerViewAdapter;
import com.pcs.hackathonandroid.beans.User;
import com.pcs.hackathonandroid.interfaces.Api;
import com.pcs.hackathonandroid.rest.RestClient;
import com.pcs.hackathonandroid.util.SharedPrefUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    @BindView(R.id.add)
    FloatingActionButton fab;

    private ProgressDialog mProgressDialog;

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
        mSwipeRefreshLayout.post(() -> loadRecyclerViewData());

        fab.setOnClickListener(view1 -> exploreFriends());

        return view;
    }

    private void exploreFriends() {
        RestClient.getInstance(getContext()).get(Api.class)
                .exploreFriends(SharedPrefUtil.getFromPrefs(getContext(), "token", ""))
                .doOnSubscribe(disposable -> showProgressDialog())
                .doOnTerminate(() -> hideProgressDialog())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleExploreResults, this::handleError);
    }

    public void showProgressDialog() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getContext());
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setIndeterminate(true);
            }
            mProgressDialog.show();
        });
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void handleExploreResults(List<User> users) {
        if (users != null && !users.isEmpty()) {
            AddFriendAdapter adapter = new AddFriendAdapter(users, getContext());
            MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                    .title("Add Friends")
                    .adapter(adapter, null)
                    .show();
            dialog.setOnDismissListener(dialogInterface -> loadRecyclerViewData());
        } else
            Toast.makeText(getContext(), "No users left to add as friends", Toast.LENGTH_LONG).show();
    }

    private void loadRecyclerViewData() {
        RestClient.getInstance(getContext()).get(Api.class)
                .getFriends(SharedPrefUtil.getFromPrefs(getContext(), "token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mSwipeRefreshLayout.setRefreshing(true))
                .doOnTerminate(() -> mSwipeRefreshLayout.setRefreshing(false))
                .subscribe(this::handleResults, this::handleError);
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void handleResults(List<User> users) {
        adapter = new FriendRecyclerViewAdapter(users, mListener);
        recyclerView.setAdapter(adapter);
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

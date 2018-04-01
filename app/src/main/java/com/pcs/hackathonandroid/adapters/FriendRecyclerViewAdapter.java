package com.pcs.hackathonandroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.beans.User;
import com.pcs.hackathonandroid.fragments.FriendFragment.OnListFragmentInteractionListener;

import java.util.List;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;
    private final OnListFragmentInteractionListener mListener;

    public FriendRecyclerViewAdapter(List<User> users, OnListFragmentInteractionListener listener) {
        this.users = users;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = users.get(position);
        holder.user = user;
        holder.nameView.setText(user.fullName);
        holder.emailView.setText(user.email);
        if (user.isStreaming) {
            holder.mView.setClickable(true);
            holder.liveView.setVisibility(View.VISIBLE);
            holder.mView.setOnClickListener(v -> {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.user);
                }
            });
        } else {
            holder.mView.setClickable(false);
            holder.liveView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView emailView;
        public final TextView liveView;
        public User user;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = view.findViewById(R.id.name);
            emailView = view.findViewById(R.id.email);
            liveView = view.findViewById(R.id.live);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + emailView.getText() + "'";
        }
    }
}

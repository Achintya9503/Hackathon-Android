package com.pcs.hackathonandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.beans.Response;
import com.pcs.hackathonandroid.beans.User;
import com.pcs.hackathonandroid.interfaces.Api;
import com.pcs.hackathonandroid.rest.RestClient;
import com.pcs.hackathonandroid.util.SharedPrefUtil;

import java.util.List;

import github.ishaan.buttonprogressbar.ButtonProgressBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {

    public static final String TAG = AddFriendAdapter.class.getSimpleName();

    private final List<User> users;
    private final Context mContext;

    public AddFriendAdapter(List<User> users, Context context) {
        this.users = users;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_add_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = users.get(position);
        holder.user = user;
        holder.nameView.setText(user.fullName);
        holder.sendView.setOnClickListener(v -> exploreFriends(holder.sendView, user.uid));
    }

    private void exploreFriends(ButtonProgressBar sendView, String uid) {
        RestClient.getInstance(mContext).get(Api.class)
                .addFriend(SharedPrefUtil.getFromPrefs(mContext, "token", ""),
                        uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> sendView.startLoader())
                .doOnTerminate(() -> sendView.stopLoader())
                .subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(Response response) {
        Log.d(TAG, response.status);
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final ButtonProgressBar sendView;
        public User user;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = view.findViewById(R.id.name);
            sendView = view.findViewById(R.id.send);
        }
    }
}

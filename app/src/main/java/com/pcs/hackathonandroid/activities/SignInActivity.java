package com.pcs.hackathonandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.beans.Response;
import com.pcs.hackathonandroid.beans.User;
import com.pcs.hackathonandroid.interfaces.Api;
import com.pcs.hackathonandroid.rest.RestClient;
import com.pcs.hackathonandroid.util.SharedPrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends BaseActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private Api api;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.detail)
    TextView mDetailTextView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        api = RestClient.getInstance(this).get(Api.class);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(view -> signIn());

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (Task<AuthResult> login) -> {
                    if (login.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUser(new User(user.getEmail(), user.getDisplayName(), user.getUid()));
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", login.getException());
                        Snackbar.make(findViewById(R.id.main_layout),
                                "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    hideProgressDialog();
                });
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void handleResults(Response response) {
        Log.d(TAG, response.status);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            // get token
            user.getIdToken(true)
                    .addOnCompleteListener(getToken -> {
                        if (getToken.isSuccessful()) {
                            String token = getToken.getResult().getToken();
                            // Save token
                            Log.d(TAG, "token: " + token);
                            SharedPrefUtil.saveToPrefs(this, "token", token);
                        } else {
                            Log.e(TAG, getToken.getException().getMessage());
                        }
                    });

            SharedPrefUtil.saveToPrefs(this, "uid", user.getUid());
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("photo", user.getPhotoUrl());
            intent.putExtra("name", user.getDisplayName());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("uid", user.getUid());
            startActivity(intent);
        } else {
            mDetailTextView.setText(null);

            SharedPrefUtil.removeFromPrefs(this, "token");

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    private void saveUser(User user) {
        api.saveUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showProgressDialog())
                .doOnTerminate(() -> hideProgressDialog())
                .subscribe(this::handleResults, this::handleError);
    }
}


package com.pcs.hackathonandroid.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.beans.User;
import com.pcs.hackathonandroid.fragments.FriendFragment;
import com.pcs.hackathonandroid.fragments.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        FriendFragment.OnListFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Uri photo;
    private String name;
    private String email;

    @BindView(R.id.profile_image)
    ImageView profile;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_friends:
                fragment = new FriendFragment();
                break;
        }

        return loadFragment(fragment);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //loading the default fragment
        loadFragment(new HomeFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        this.photo = intent.getParcelableExtra("photo");
        this.name = intent.getStringExtra("name");
        this.email = intent.getStringExtra("email");

        Glide.with(this)
                .load(photo)
                .apply(RequestOptions.circleCropTransform())
                .into(profile);

        profile.setOnClickListener(view -> showAccountPopup());
    }

    public void showAccountPopup() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup);
        dialog.setCanceledOnTouchOutside(true);
        if (photo != null) {
            Glide.with(this)
                    .load(photo)
                    .into((ImageView) dialog.findViewById(R.id.profile_image));
        }
        if (name != null) {
            ((TextView) dialog.findViewById(R.id.name)).setText(name);
        }
        if (email != null) {
            ((TextView) dialog.findViewById(R.id.email)).setText(email);
        }

        dialog.findViewById(R.id.signOut).setOnClickListener(view -> {
            signOut();
            dialog.hide();
        });

        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.gravity = Gravity.TOP | Gravity.LEFT;
        attributes.x = (int) profile.getX();
        attributes.y = (int) profile.getY();

        dialog.show();
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> finish());
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onListFragmentInteraction(User user) {
        Intent intent = new Intent(this, LiveActivity.class);
        startActivity(intent);
    }
}

package com.perfect108.bighackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kunal on 30-03-2018.
 */

public class MainActivity extends AppCompatActivity{

    Button goLiveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goLiveBtn = (Button)findViewById(R.id.go_live_btn);
        goLiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,FullscreenActivity.class);
                startActivity(i);
            }
        });
    }
}

package com.example.woof.stinksquatcentral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final static String RESULT = "com.example.woof.stinksquatcentral.RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPastActivity(View view) {
        Intent intent = new Intent(this, PastLaunches.class);
        startActivity(intent);
    }

    public void startFutureActivity(View view) {
        Intent intent = new Intent(this,FutureMissions.class);
        startActivity(intent);
    }
}
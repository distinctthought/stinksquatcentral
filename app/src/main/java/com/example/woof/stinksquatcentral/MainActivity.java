package com.example.woof.stinksquatcentral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    // Ignore this, pretty sure we can delete it. Will investigate further.
    public final static String RESULT = "com.example.woof.stinksquatcentral.RESULT";

    //When the MainActivity is started, show activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //The function used to start the Past Launches activity.
    public void startPastActivity(View view) {
        Intent intent = new Intent(this, PastLaunches.class);
        startActivity(intent);
    }

    //The function used to start the Future Missions activity.
    public void startFutureActivity(View view) {
        Intent intent = new Intent(this,FutureMissions.class);
        startActivity(intent);
    }
}
package com.example.woof.stinksquatcentral;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Woof on 10/7/2016.
 */
public class FutureMissions extends AppCompatActivity {
    public final static String RESULT = "com.example.woof.stinksquatcentral.RESULT";

    //Haven't started on this yet. I just display past_launches.xml to make sure the button works.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_launches);
    }
}

package com.example.saikrishna.raven.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saikrishna.raven.Fragment.Fragment_Attachment;
import com.example.saikrishna.raven.Fragment.Fragment_ChatScreen;
import com.example.saikrishna.raven.Fragment.Fragment_ContactProfile;
import com.example.saikrishna.raven.Fragment.Fragment_Status;
import com.example.saikrishna.raven.Fragment.Fragment_contacts;
import com.example.saikrishna.raven.R;

/**
 * Created by Sai Krishna on 19-07-2015.
 */
public class Activity_ChatScreen extends AppCompatActivity {
    Toolbar mToolbar;
    public static String number;
    public static Boolean flag = false;
    public static String name;
    public static final String ARG_CONTACTS_INDEX = "argument_option";
    public static final String ARG_CONTACTS_INDEX_NAME = "argument_option_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatscreen);
        flag = true;
        number = getIntent().getStringExtra(ARG_CONTACTS_INDEX);
        name = getIntent().getStringExtra(ARG_CONTACTS_INDEX_NAME);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_cs, Fragment_ChatScreen.newInstance(number,name))
                    .commit();
        }
    }

    protected void onDestroy() {

        //option 1: callback before or ...
        flag = false;
        super.onDestroy();

        //option 2: callback after super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chatscreen, menu);
        Log.d("Revan ", "Info: " + "MENU INFLATED IN ACTIVITY");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            /*case android.R.id.home:
                onBackPressed();
                return false;*/
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void setProfileTouch(TextView name)
    {
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_cs, Fragment_ContactProfile.newInstance(number))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}

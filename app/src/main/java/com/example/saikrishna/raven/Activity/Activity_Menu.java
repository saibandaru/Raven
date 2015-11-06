package com.example.saikrishna.raven.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.saikrishna.raven.Fragment.Fragment_Profile;
import com.example.saikrishna.raven.Fragment.Fragment_Settings;
import com.example.saikrishna.raven.Fragment.Fragment_contacts;
import com.example.saikrishna.raven.R;

import java.io.IOException;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class Activity_Menu extends AppCompatActivity {
    Toolbar mToolbar;
    public int option;
    private static final int SELECT_SINGLE_PICTURE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        option=getIntent().getIntExtra(Fragment_contacts.ARG_CONTACTS_INDEX,0);
        this.invalidateOptionsMenu();
        mToolbar=(Toolbar)findViewById(R.id.toolbar_settings);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_settings, Fragment_Settings.newInstance())
                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                this.onBackPressed();
/*                if(Fragment_Settings.flag!=null) {
                    if(Fragment_Settings.flag==true)
                    this.finish();
                    else{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_settings, Fragment_Settings.newInstance())
                                .commit();
                    }
                }
                else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_settings, Fragment_Settings.newInstance())
                            .commit();
                }*/
                return true;
        }

        /*switch(id){
            case R.id.action_refresh:
                Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_edit:
                Toast.makeText(getApplicationContext(),"Clicked Edit",Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }*/
        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_cs, Fragment_Settings.newInstance())
                    .commit();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    public Toolbar getToolBar()
    {
        return mToolbar;
    }



    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Activity got back:", "onActivityResult"); // not printed
        Toast.makeText(getApplicationContext(), "aaaa"+requestCode+"     "+resultCode, Toast.LENGTH_SHORT).show();
        Log.d("Result Code", (String.valueOf(resultCode))); // not printed


        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };

        Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                *//*managedQuery(selectedImageUri, projection, null, null,
                null);*//*
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        if(Fragment_Profile.profile_pic!=null)
            Fragment_Profile.profile_pic.setImageBitmap(bm);
    }*/
}


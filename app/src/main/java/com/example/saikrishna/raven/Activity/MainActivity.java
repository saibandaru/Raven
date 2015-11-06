package com.example.saikrishna.raven.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.saikrishna.raven.Async.SyncPP_Async;
import com.example.saikrishna.raven.Async.SyncStatus_Async;
import com.example.saikrishna.raven.Fragment.Fragment_contacts;
import com.example.saikrishna.raven.Fragment.Fragment_register;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_Contacts;
import com.example.saikrishna.raven.R;
import com.example.saikrishna.raven.Extras.Utility;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public Toolbar mToolbar;


    //public static String mynumber_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        /*mToolbar.setLogo(R.drawable.icon_color64x64);*/
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        makeDir();

        // Enable Local Datastore.



        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });


        actionBar.setDisplayShowTitleEnabled(true);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, Fragment_register.newInstance(100))
                    .commit();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, Fragment_register.newInstance(100))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu_one, menu);
        SearchView searchItem=(SearchView)menu.findItem(R.id.action_search).getActionView();
        if(searchItem!=null){
            searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
               @Override
                public boolean onQueryTextSubmit(String query){
                   int position=0;
                   List<HashMap<String,String>> new_contacts=new ArrayList<>();
                   while (position!= Fragment_contacts.contacts.size()){
                       HashMap<String,String> person=Fragment_contacts.contacts.get(position);
                       if(((String)person.get("name")).contains(query))
                           new_contacts.add((HashMap<String,String>)person);
                       position++;
                   }
                   Fragment_contacts.contacts=new_contacts;
                   Fragment_contacts.myRecyclerViewAdapter.notifyDataSetChanged();
                   return true;
               }
                @Override
            public boolean onQueryTextChange(String query){
                    int position=0;
                    HashMap<String,String> person;
                    List<HashMap<String,String>> new_contacts=new ArrayList<>();
                    while (position!=Fragment_contacts.contacts_master.size()){
                        person=Fragment_contacts.contacts_master.get(position);
                        if(((String)person.get("name")).toLowerCase().contains(query.toLowerCase()))
                        {
                            new_contacts.add((HashMap<String, String>) person);
                        }
                        position++;
                    }
                   // MyRecyclerViewAdapter_Contacts.contacts.clear(); position=0;

                    /*while (new_contacts.size()!=position){person=new_contacts.get(position);
                        MyRecyclerViewAdapter_Contacts.contacts.add(person);
                    }*/
                    Fragment_contacts.contacts=new_contacts;
                    MyRecyclerViewAdapter_Contacts.contacts=new_contacts;
                    Fragment_contacts.myRecyclerViewAdapter.notifyDataSetChanged();
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,Activity_Menu.class);
            intent.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX,0);;
            startActivity(intent);
        }
        else if(id == R.id.Refresh){
            if(Utility.haveNetworkConnection(getApplicationContext())) {
                Toast.makeText(getApplication(),
                        "Refreshing Contacts.." + "",
                        Toast.LENGTH_LONG).show();
                SyncStatus_Async update_exec = new SyncStatus_Async(getApplicationContext());
                SyncPP_Async update_exec_pp = new SyncPP_Async(getApplicationContext());
                startMyTask(update_exec);
                startMyTask(update_exec_pp);
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Network Error:No Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
        else{}

        return super.onOptionsItemSelected(item);
    }
    public Toolbar getToolBar()
    {
        return mToolbar;
    }
    public void makeDir()
    {
        String path=getApplicationContext().getFilesDir().getAbsolutePath()+"/RevanProfilePics";
        File file = new File ( path );

        if ( file.exists() )
        {
            Log.d("User Profile Screen ", "Message: " + "REVANPROFILEPICS EXISTS");
        }
        else
        {
            File f = new File(getApplicationContext().getFilesDir().getAbsolutePath(),File.separator+"RevanProfilePics/");
            f.mkdirs();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(AsyncTask asyncTask) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});
        else
            asyncTask.execute(new String[]{"nothimg"});
    }
}

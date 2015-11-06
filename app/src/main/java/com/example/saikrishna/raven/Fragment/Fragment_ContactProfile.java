package com.example.saikrishna.raven.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saikrishna.raven.Async.LocalCImageLoad_Async;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ContactsDataSource;
import com.example.saikrishna.raven.R;
import com.pkmmte.view.CircularImageView;

import java.util.HashMap;

/**
 * Created by Sai Krishna on 8/2/2015.
 */
public class Fragment_ContactProfile extends Fragment {

    public Toolbar mToolbar;
    public String number;
    public Fragment_ContactProfile() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView  = inflater.inflate(R.layout.layout_profile, container, false);
        TextView name_b= setToolBar("Contact Information");
        Bundle bundle = this.getArguments();
        number= bundle.getSerializable("NUMBER").toString();
        popupateScreen(rootView);
        setProfileTouch(name_b);
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_chatscreen, menu);
        //super.onCreateOptionsMenu(menu,inflater);
        Log.d("Revan ", "Info: " + "MENU INFLATED IN FRAGMENT");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_attachments:
                /*getActivity().onBackPressed();*/
                return false;
            case android.R.id.home:
                getActivity().onBackPressed();
                return false;
        }

        return false;
    }
    public static Fragment_ContactProfile newInstance(String option) {
        Fragment_ContactProfile fragment = new Fragment_ContactProfile();
        Bundle args = new Bundle();
        args.putSerializable("NUMBER",option);
        fragment.setArguments(args);
        return fragment;
    }
    public TextView setToolBar(String name)
    {
        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar_chat);
        TextView name_b = (TextView) mToolbar.findViewById(R.id.name_button);
        name_b.setText(name);
        return name_b;
    }
    public void setProfileTouch(TextView name)
    {
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_cs, Fragment_ContactProfile.newInstance(0))
                        .addToBackStack(null)
                        .commit()*/
                ;
            }
        });
    }
    public void popupateScreen(View rootView)
    {
        ContactsDataSource database=new ContactsDataSource(getActivity().getApplicationContext());
        database.open();
        HashMap<String ,String > contact_info=database.retriveContact(number);
        database.close();
        if(contact_info.size()>0) {
            TextView status_cp = (TextView) rootView.findViewById(R.id.status_contact_profile);
            TextView number_cp = (TextView) rootView.findViewById(R.id.phonenumber_contact_profile);
            CircularImageView img_cont=(CircularImageView) rootView.findViewById(R.id.profile_pic);
            status_cp.setText(contact_info.get("status"));
            number_cp.setText(number);
            String path=contact_info.get("profile_pic");
            if(path!=null) {
                LocalCImageLoad_Async loadimage = new LocalCImageLoad_Async(img_cont, path, 150);
                loadimage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});
            }
        }


    }
}
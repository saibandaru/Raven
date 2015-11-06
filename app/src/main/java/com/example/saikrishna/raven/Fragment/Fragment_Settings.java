package com.example.saikrishna.raven.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saikrishna.raven.Activity.Activity_Menu;
import com.example.saikrishna.raven.Parallax.ParallaxToolbarScrollViewActivity;
import com.example.saikrishna.raven.R;

/**
 * Created by Sai Krishna on 25-07-2015.
 */
public class Fragment_Settings extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    Toolbar mToolbar;
    public static Boolean flag=false;
    public Fragment_Settings() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.layout_settings, container, false);
        mToolbar=(Toolbar)((Activity_Menu)getActivity()).getToolBar();
        mToolbar.setTitle("Settings");
        flag   =true;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView help=(TextView)rootView.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_settings, FlexibleSpaceWithImageGridViewActivity.newInstance())
                        .addToBackStack(null)
                        .commit();*/
                Intent intent=new Intent(getActivity(),ParallaxToolbarScrollViewActivity.class);
                startActivity(intent);
            }
        });
        TextView profile=(TextView)rootView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_settings, Fragment_Profile.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });
        TextView status=(TextView)rootView.findViewById(R.id.status);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_settings, Fragment_Status.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });
        TextView account=(TextView)rootView.findViewById(R.id.account);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* yet to implement*/
            }
        });
        return rootView;
    }
    @Override
    public void onDestroy() {
        flag=false;
        super.onDestroy();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_settings, menu);
        //fragment specific menu creation
    }
/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }*/

    public static Fragment_Settings newInstance() {
        Fragment_Settings fragment = new Fragment_Settings();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}

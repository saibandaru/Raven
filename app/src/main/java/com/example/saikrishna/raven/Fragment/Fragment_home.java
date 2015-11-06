package com.example.saikrishna.raven.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saikrishna.raven.Activity.Activity_Menu;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ChatDataSource;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatList;
import com.example.saikrishna.raven.R;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sai Krishna on 12-07-2015.
 */
public class Fragment_home extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    PagerAdapter myPagerAdapter;
    ViewPager myViewPager;
    SharedPreferences mynumber;
    public static final String MOBILE_NUMBER = "my_number";
    public static String mynumber_value;
    public Fragment_home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.layout_home, container, false);
        mynumber=getActivity().getSharedPreferences(MOBILE_NUMBER, 0);
        mynumber_value=mynumber.getString("mynumber", null);
        myViewPager=(ViewPager)rootView.findViewById(R.id.viewpager_recycle);
        myPagerAdapter=new PagerAdapter(getActivity().getSupportFragmentManager(),2);
        customizeViewPager();
        myViewPager.setAdapter(myPagerAdapter);
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){

                    Fragment_chats.myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    MyRecyclerViewAdapter_ChatList.contacts= retriveNewChat();
                    Fragment_chats.contacts_onRun= MyRecyclerViewAdapter_ChatList.contacts;
                    Fragment_chats.myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int option = getArguments().getInt(ARG_OPTION);
        if (option == R.id.action_settings) {
            Intent intent=new Intent(getActivity(),Activity_Menu.class);
            intent.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX,0);;
            startActivity(intent);
        }
        return rootView;
    }

    public static Fragment_home newInstance(int option) {
        Fragment_home fragment = new Fragment_home();
        Bundle args=new Bundle();
        args.putInt(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;

    }
    public class PagerAdapter extends FragmentPagerAdapter {
        int count;
        public PagerAdapter(FragmentManager fm,int size){
            super(fm);
            count=size;
        }
        @Override
        public Fragment getItem(int position){
            if (position==0)
                return Fragment_chats.newInstance(0);
            else
                return Fragment_contacts.newInstance(0);
        }
        @Override
        public int getCount(){return count;}
        @Override
        public  CharSequence getPageTitle(int position){
            Locale l=Locale.getDefault();
            String name;
            switch(position){
                case 0:
                    name="Chats";
                    break;
                case 1:
                    name="Contacts";
                    break;
                default:
                    name="Contacts";
            }
            return  name.toUpperCase(l);
        }
    }
    public void customizeViewPager()
    {
        myViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                //Scaling
                //float normalized_position = Math.abs(Math.abs(position) - 1);
                //page.setScaleX(normalized_position / 2 + 0.5f);
                //page.setScaleY(normalized_position / 2 + 0.5f);
                //Rotation
                //page.setRotationY(position * -30);
                //fading
                //page.setAlpha(normalized_position);
            }
        });
    }
    public List<HashMap<String,String>> retriveNewChat(){
        ChatDataSource database=new ChatDataSource(getActivity().getApplicationContext());
        database.open();
        List<HashMap<String,String>>list= database.getChats(Fragment_chats.contacts);
        database.close();
        return list;
    }
}

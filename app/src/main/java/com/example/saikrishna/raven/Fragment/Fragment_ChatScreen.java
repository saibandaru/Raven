package com.example.saikrishna.raven.Fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.saikrishna.raven.Async.LocalCImageLoad_Async;
import com.example.saikrishna.raven.Async.NetworkOnSend_Async;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ChatDataSource;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ContactsDataSource;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.MessageDataSource;
import com.example.saikrishna.raven.Extras.NetworkStateReceiver;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatScreen;
import com.example.saikrishna.raven.R;
import com.pkmmte.view.CircularImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 04-07-2015.
 */
public class Fragment_ChatScreen extends Fragment /*implements NetworkStateReceiver.NetworkStateReceiverListener*/ {
    public static List<HashMap<String,String>> messages;
    private static final String ARG_OPTION_NUMBER = "Receiver_Number";
    private static final String ARG_OPTION_NAME = "Receiver_Name";
    public static MessageDataSource database;
    public static MyRecyclerViewAdapter_ChatScreen myRecyclerViewAdapter;
    public static RecyclerView recyclerView;
    public static ChatDataSource chat_database;
    SharedPreferences mynumber;
    public static final String MOBILE_NUMBER = "my_number";
    public static String mynumber_value;
    Toolbar mToolbar;
    public String name;
    public static String number;
    /*private NetworkStateReceiver networkStateReceiver;*/
    public Fragment_ChatScreen() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mynumber=getActivity().getSharedPreferences(MOBILE_NUMBER, 0);
        mynumber_value=mynumber.getString("mynumber", null);
        number =(String)this.getArguments().getSerializable(ARG_OPTION_NUMBER);
        name =(String)this.getArguments().getSerializable(ARG_OPTION_NAME);
        TextView name_b= setToolBar(name,number);
        setProfileTouch(name_b);

       /* networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getActivity().getApplicationContext().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));*/

        View rootView  = inflater.inflate(R.layout.layout_chatscreen, container, false);
        Button send=(Button)rootView.findViewById(R.id.button);
        final EditText one_message=(EditText)rootView.findViewById(R.id.editText);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        setRetainInstance(true);        recyclerView.setHasFixedSize(true);        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());        recyclerView.setLayoutManager(mLayoutManager);
        database=new MessageDataSource(getActivity().getApplicationContext());
        database.open();
        messages=database.retriveMessageList_contact(number);
        database.close();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter_ChatScreen(getActivity(), messages,number);
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter_ChatScreen.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //select_movie_recycle(v, position);
                //HashMap<String,?> movie_new=(HashMap<String,?>)movieData.getItem(position);
            }
        });
        setFocusSoftKeypad(one_message);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        recyclerView.scrollToPosition(messages.size() - 1);
        //int option = this.getArguments().getInt(ARG_OPTION);
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.bf_final);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        send.requestFocus();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oneMsg_content = one_message.getText().toString();
                if(oneMsg_content.length()>0) {
                    database = new MessageDataSource(getActivity().getApplicationContext());
                    database.open();
                    database.insertMessage(oneMsg_content, number,mynumber_value);
                    database.close();
                    HashMap<String, String> msg = new HashMap<String, String>();
                    msg.put("message", oneMsg_content);
                    msg.put("sender_id", Fragment_home.mynumber_value);
                    msg.put("date", date());
                    msg.put("time",time());
                    msg.put("status","0");
                    msg.put("seen","0");
                    messages.add(msg);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                    one_message.setText(null);
                    insertChatItemIfNotExists(number);
                }

            }
        });
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
                getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_cs, Fragment_Attachment.newInstance())
                    .addToBackStack(null)
                    .commit();
                /*SampleDialogFragment fragment
                        = SampleDialogFragment.newInstance(
                        8,
                        4,
                        true,
                        false
                );
                fragment.show(getActivity().getFragmentManager(), "blur_sample");*/
                return false;
            case android.R.id.home:
                getActivity().onBackPressed();
            return false;
        }

        return false;
    }
    @Override
    public void onResume() {
        TextView name_b= setToolBar(name,number);
        setProfileTouch(name_b);
        recyclerView.scrollToPosition(messages.size() - 1);
        super.onResume();
    }
    /*@Override
    public void networkAvailable() {
        Log.d("tommydevall", "I'm in, baby!");
        NetworkOnSend_Async exe=new NetworkOnSend_Async(getActivity().getApplicationContext());
        exe.execute(new String[]{"nothimg"});
        *//*exe.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});*//*
    *//* TODO: Your connection-oriented stuff here *//*
    }*/
    /*@Override
    public void networkUnavailable() {
        Log.d("tommydevall", "I'm dancing with myself");
    *//* TODO: Your disconnection-oriented stuff here *//*
    }*/
    public static Fragment_ChatScreen newInstance(String number,String name) {
        Fragment_ChatScreen fragment = new Fragment_ChatScreen();
        Bundle args = new Bundle();
        args.putSerializable(ARG_OPTION_NUMBER, number);
        args.putSerializable(ARG_OPTION_NAME, name);
        fragment.setArguments(args);
        return fragment;

    }
    public void insertChatItemIfNotExists(String number){
        chat_database=new ChatDataSource(getActivity().getApplicationContext());
        chat_database.open();
        if(!chat_database.checkContactIfExists(number)) {
            chat_database.insertMessage(number);
        }
        chat_database.close();
    }
    private String time()
    {
        Calendar c = Calendar.getInstance();
        String time_v = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) +":"+String.valueOf( c.get(Calendar.MINUTE));
        return time_v;
    }
    private String date()
    {
        Calendar c = Calendar.getInstance();
        String date_v =String.valueOf(c.get(Calendar.YEAR) )+"-"+ String.valueOf(c.get(Calendar.MONTH)) +"-"+ String.valueOf(c.get(Calendar.DATE));
        return date_v;
    }
    public TextView setToolBar(String name,String number)
    {
        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar_chat);
        CircularImageView profile_pic=(CircularImageView)mToolbar.findViewById(R.id.profile_image);
        TextView name_b = (TextView) mToolbar.findViewById(R.id.name_button);
        ContactsDataSource dataSource=new ContactsDataSource(getActivity().getApplicationContext());
        String profile_pic_bath=dataSource.image_path(number);
        name_b.setText(name);
        if(profile_pic_bath!=null) {
            LocalCImageLoad_Async loadimage = new LocalCImageLoad_Async(profile_pic, profile_pic_bath, 75);
            loadimage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});
        }
        return name_b;
    }
    public void setProfileTouch(TextView name)
    {
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_cs, Fragment_ContactProfile.newInstance(number))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    public void setFocusSoftKeypad(final EditText msg)
    {
        /*msg.requestFocus();
        msg.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(msg, 0);
            }
        },200);*/
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(msg.getWindowToken(), 0);
    }
}

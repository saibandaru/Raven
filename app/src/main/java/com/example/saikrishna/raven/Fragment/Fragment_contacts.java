package com.example.saikrishna.raven.Fragment;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saikrishna.raven.Activity.Activity_ChatScreen;
import com.example.saikrishna.raven.Async.SyncPP_Async;
import com.example.saikrishna.raven.Async.SyncStatus_Async;
import com.example.saikrishna.raven.Async.UpdateStatus_Async;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ContactsDataSource;
import com.example.saikrishna.raven.Async.Installation_Async;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_Contacts;
import com.example.saikrishna.raven.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 05-07-2015.
 */
public class Fragment_contacts extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    public static final String ARG_CONTACTS_INDEX = "argument_option";
    public static final String ARG_CONTACTS_INDEX_NAME = "argument_option_name";
    public static List<HashMap<String,String>> contacts;
    public static List<HashMap<String,String>> contacts_master;
    public static List<HashMap<String,String>> contacts_phone;
    public static List<HashMap<String,String>> contacts_application;
    public static MyRecyclerViewAdapter_Contacts myRecyclerViewAdapter;
    public Fragment_contacts() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.contacts_o, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_contacts);
        setRetainInstance(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        contacts=contacts_master=contacts();
        Collections.sort(contacts, new Comparator<HashMap<String, String>>(){
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                return one.get("name").compareTo(two.get("name"));
            }
        });

        myRecyclerViewAdapter = new MyRecyclerViewAdapter_Contacts(getActivity(),contacts);
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter_Contacts.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String number=getContact(position);
                String name=getContactName(position);
                Intent intent=new Intent(getActivity(),Activity_ChatScreen.class);
                intent.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX,number);
                intent.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX_NAME,name);
                startActivity(intent);
                /*Information.Block_Contact person=info.getItem(position);
                Chat_Fragment fragment=Chat_Fragment.newInstance(person);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                        .addToBackStack(null).commit();*/
            }
        });
        recyclerView.setAdapter(myRecyclerViewAdapter);
        int option = this.getArguments().getInt(ARG_OPTION);
        return rootView;
    }

    public static Fragment_contacts newInstance(int option) {
        Fragment_contacts fragment = new Fragment_contacts();
        Bundle args = new Bundle();
        args.putInt(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }


    public List<HashMap<String,String>> contacts() {
        //final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
        final Uri uriContact = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(uriContact, null, null, null, null);

        List<HashMap<String,String>> contacts=new ArrayList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                String has_phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                HashMap<String,String> contact=new HashMap();
                if(!has_phone.endsWith("0"))
                {
                    contact.put("name", name);
                    String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.put("number",phoneNumber);
                    contacts.add(contact);
                }
            }
        }
        this.contacts_phone=contacts;
        contacts=filter_to_app(contacts);
        return contacts;
    }

    public List<HashMap<String ,String>> filter_to_app(List<HashMap<String,String>> system_contacts){
        int index=0;String number;
        List<HashMap<String ,String>> return_contacts=new ArrayList<HashMap<String, String>>();
        List<String > non_existing=new ArrayList<String >();
        ContactsDataSource database=new ContactsDataSource(getActivity().getApplicationContext());
        database.open();
        contacts_application=database.retriveContactList();
        database.close();

        while(system_contacts.size()!=index){
            HashMap<String,String> contact_one=system_contacts.get(index);
            number=contact_one.get("number");
            number=number.replace(" ","");
            number= number.replace("-","");
            number= number.replace("(","");
            number= number.replace(")","");
            int index2=0;boolean flag=false;
            while (contacts_application.size() != index2) {
                HashMap<String,String> contact_one2=contacts_application.get(index2);
                String con=contact_one2.get("number");if(number.equals(con) ){
                    contact_one.put("status",contact_one2.get("status"));
                    contact_one.put("profile_pic",contact_one2.get("profile_pic"));
                    return_contacts.add(contact_one);flag=true;break;
                }
                index2++;
            }
            if(!flag)non_existing.add(number);
            index++;
        }
        Context context_pass=getActivity().getApplicationContext();
        Installation_Async getAppContacts=new Installation_Async(non_existing,context_pass);
        /*getAppContacts.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[]{"nothimg"});*/
        startMyTask(getAppContacts);
        return return_contacts;
    }
    public String GetPhoneNumber(String id)
    {
        String number = "";
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID + " = " + id+1, null, null);

        if(phones.getCount() > 0)
        {
            while(phones.moveToNext())
            {
                number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
           // System.out.println(number);
        }

        phones.close();

        return number;
    }
    public String getContact(int position)
    {
        HashMap<String ,String > person= Fragment_contacts.contacts.get(position);
        return person.get("number");
    }
    public String getContactName(int position)
    {
        HashMap<String ,String > person= Fragment_contacts.contacts.get(position);
        return person.get("name");
    }
    public static void update_Async(List<HashMap<String,String >> contacts_application1){
        int index=0;String number;
        List<HashMap<String ,String>> return_contacts=new ArrayList<HashMap<String, String>>();;
        List<HashMap<String ,String>> system_contacts=contacts_phone;
        while(system_contacts.size()!=index){
            HashMap<String,String> contact_one=system_contacts.get(index);
            number=contact_one.get("number");
            number=number.replace(" ","");
            number= number.replace("-","");
            number= number.replace("(","");
            number= number.replace(")","");
            int index2=0;
            while (contacts_application1.size() != index2) {
                HashMap<String,String> contact_one2=contacts_application1.get(index2);
                String con=contact_one2.get("number");
                if(number.equals(con) ){
                    contact_one.put("status",contact_one2.get("status"));
                    contact_one.put("profile_pic",contact_one2.get("profile_pic"));
                    return_contacts.add(contact_one);break;
                }
                index2++;
            }
            index++;
        }
        contacts= return_contacts;
        MyRecyclerViewAdapter_Contacts.contacts=contacts;
        Fragment_contacts.myRecyclerViewAdapter.notifyDataSetChanged();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(AsyncTask asyncTask) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});
        else
            asyncTask.execute(new String[]{"nothimg"});
    }
}

package com.example.saikrishna.raven.Fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saikrishna.raven.DataBase.Tables.DataSource.MessageDataSource;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.StatusDataStore;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.YourDataSource;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Messages_Parse;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Profiles;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Repository_Parse;
import com.example.saikrishna.raven.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Krishna on 12-07-2015.
 */
public class Fragment_register extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    SharedPreferences mynumber;
    public SharedPreferences status;
    public static final String MOBILE_NUMBER = "my_number";
    public static final String REG_SKIP_FLAG = "RegisterationFlag";
    public static final String STATUS = "Status";
    public static String mynumber_value;
    public Fragment_register() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final SharedPreferences reg_flag = getActivity().getSharedPreferences(REG_SKIP_FLAG, 0);
        status = getActivity().getSharedPreferences(STATUS, 0);
        View rootView = inflater.inflate(R.layout.layout_setup, container, false);
        Boolean flag_value=reg_flag.getBoolean("registered", false);
        final int option= getArguments().getInt(ARG_OPTION);
        /*setUpRepository();*/
        if(flag_value==false) {
            final EditText code = (EditText) rootView.findViewById(R.id.country_code);
            final EditText number = (EditText) rootView.findViewById(R.id.number);
            Button next = (Button) rootView.findViewById(R.id.next_button);
            //final String fullnumber = code.getText().toString() + number.getText().toString();
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((code.getText().toString() != null)&&( number.getText().toString()!=null)) {
                         String fullnumber = code.getText().toString() + number.getText().toString();

                        Profiles new_profile = new Profiles();
                        new_profile.setProfile(fullnumber, fullnumber);
                        new_profile.saveInBackground();
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("username", fullnumber);
                        saveImage_PP(installation,fullnumber);
                        /*installation.saveInBackground();*/
                        SharedPreferences.Editor editor = reg_flag.edit();
                        editor.putBoolean("registered", true);
                        editor.commit();
                        /*setup Status table*/
                        /* END of setup*/
                        Bitmap pic=BitmapFactory.decodeResource(getResources(), R.drawable.contacticontwo);
                        setYourTable(fullnumber, bitmapToByteArray(pic));
                        mynumber=getActivity().getSharedPreferences(MOBILE_NUMBER, 0);
                        SharedPreferences.Editor editor_num = mynumber.edit();
                        editor_num.putString("mynumber", fullnumber);
                        editor_num.commit();
                        mynumber_value=mynumber.getString("mynumber",null);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, Fragment_home.newInstance(option))
                                .commit();
                    } else {
                    }

                }
            });
        }
        else{

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Fragment_home.newInstance(option))
                    .commit();

        }
        return rootView;
    }

    public static Fragment_register newInstance(int option) {
        Fragment_register fragment = new Fragment_register();
        Bundle args=new Bundle();
        args.putInt(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;

    }
    public void setupStatusTable()
    {
        List<String > list=new ArrayList<>();
        list.add("Hii, Revan is cool! lets try together");
        list.add("Available");
        list.add("Busy..ttyl");
        list.add("At Work");
        list.add("At Movies");
        list.add("Cant talk, lets chat");
        list.add("Not your hours");
        list.add("Call, if urgent");
        list.add("@ my workout");
        list.add("I have a really bad battery life");
        StatusDataStore dataStore=new StatusDataStore(getActivity().getApplicationContext());
        dataStore.open();
        dataStore.setupStatus(list);
        dataStore.close();
        SharedPreferences.Editor editor = status.edit();
        editor.putString(STATUS, list.get(0));
        editor.commit();
    }
    public void setYourTable(String number,byte[] pic)
    {
        YourDataSource database=new YourDataSource(getActivity().getApplicationContext());
        database.open();
        database.insertYour(number, pic);
        database.close();
    }
    public byte[] bitmapToByteArray(Bitmap photo) {
        /*Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.large_icon);*/
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

    public static Bitmap byteArrayToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public void saveImage_PP(final ParseInstallation installation,String number)
    {
       /* final Repository_Parse rep = new Repository_Parse();*/
        number=number.replace("+","");
        Bitmap image_bit=BitmapFactory.decodeResource(getResources(), R.drawable.contacticontwo);
        byte[] image=bitmapToByteArray(image_bit);
        final ParseFile photoFile = new ParseFile(number+".jpg", image);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    installation.put("PP",photoFile);
                    installation.saveEventually();
                }
            }

        });

    }
}

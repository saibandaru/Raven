package com.example.saikrishna.raven.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.saikrishna.raven.Activity.Activity_ChatScreen;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.MessageDataSource;
import com.example.saikrishna.raven.Extras.ImageFormatting;
import com.example.saikrishna.raven.R;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Sai Krishna on 8/2/2015.
 */
public class Fragment_Attachment extends Fragment {
    SharedPreferences sentnumber;
    public Toolbar mToolbar;
    public static final String SENT_IMAGE_NUMBER = "sentimagenumber";
    private static final int SELECT_SINGLE_PICTURE = 101;
    public static final String IMAGE_TYPE = "image/*";
    SharedPreferences mynumber;
    public static final String MOBILE_NUMBER = "my_number";
    public static String mynumber_value;

    public Fragment_Attachment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.layout_attachments, container, false);
        mynumber=getActivity().getSharedPreferences(MOBILE_NUMBER, 0);
        mynumber_value=mynumber.getString("mynumber", null);
        TextView name_b= setToolBar("Select to share");
        setProfileTouch(name_b);
        TextView shareImage=(TextView)rootView.findViewById(R.id.shareImage);
        setShareImage(shareImage);
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
        int id = item.getItemId();
        switch (id){
            case R.id.action_attachments:
                getActivity().onBackPressed();
                return false;
            case android.R.id.home:
                getActivity().onBackPressed();
                return false;
        }

        return false;
    }
    public static Fragment_Attachment newInstance() {
        Fragment_Attachment fragment = new Fragment_Attachment();
        Bundle args = new Bundle();
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
/*                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_cs, Fragment_ContactProfile.newInstance(0))
                        .addToBackStack(null)
                        .commit();*/
            }
        });
    }
    public void setShareImage(TextView shareImage)
    {
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "select picure"), SELECT_SINGLE_PICTURE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK) {
            ContentResolver cr = getActivity().getContentResolver();
            if (requestCode == SELECT_SINGLE_PICTURE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = cr.query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                saveImage(yourSelectedImage);
                getActivity().onBackPressed();
            }
        }
    }
    public void saveImage(Bitmap image)
    {
        String imagename=generateSINmae();
        makeDir("RevanSentPics");
        String path=getActivity().getApplicationContext().getFilesDir().getAbsolutePath()+File.separator+"RevanSentPics/"+ imagename;

        File file = new File(new File(getActivity().getApplicationContext().getFilesDir().getAbsolutePath(),File.separator+"RevanSentPics/"), imagename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            saveImagetoDatabase(path,image,imagename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String generateSINmae()
    {
        sentnumber = getActivity().getSharedPreferences(SENT_IMAGE_NUMBER, 0);
        int image_number=sentnumber.getInt("sentImage", 0);
        String name="sent_image_"+String.valueOf(image_number);
        SharedPreferences.Editor editor = sentnumber.edit();
        image_number+=1;
        editor.putInt("sentImage", image_number);
        editor.commit();
        return  name;
    }
    public void saveImagetoDatabase(String path,Bitmap image,String imagename)
    {
        byte[] image_byte= ImageFormatting.bitmapToByteArray(image);
        MessageDataSource database=new MessageDataSource(getActivity().getApplicationContext());
        database.open();
        database.insertImage(path,Activity_ChatScreen.number,mynumber_value,image_byte,imagename);
        database.close();
    }
    public void makeDir(String folder)
    {
        String path=getActivity().getApplicationContext().getFilesDir().getAbsolutePath()+"/"+folder;
        File file = new File ( path );

        if ( file.exists() )
        {
            Log.d("User Profile Screen ", "Message: " + "REVANPICS EXISTS");
        }
        else
        {
            File f = new File(getActivity().getApplicationContext().getFilesDir().getAbsolutePath(),File.separator+folder+"/");
            f.mkdirs();
        }
    }
}


package com.example.saikrishna.raven.Fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.saikrishna.raven.Activity.Activity_Menu;
import com.example.saikrishna.raven.R;
import com.example.saikrishna.raven.Extras.Utility;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class Fragment_Profile extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    public static final String IMAGE_TYPE = "image/*";
    private static final int SELECT_SINGLE_PICTURE = 101;
    private static final int PIC_CROP = 102;
    public static final String MOBILE_NUMBER = "my_number";
    Toolbar mToolbar;
    public String mynumber_value;
    public static ImageView profile_pic;
    SharedPreferences mynumber;
    public Fragment_Profile() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.layout_profileme, container, false);
        mToolbar=(Toolbar)((Activity_Menu)getActivity()).getToolBar();
        mToolbar.setTitle("Profile");
        mynumber=getActivity().getSharedPreferences(MOBILE_NUMBER, 0);
        mynumber_value=mynumber.getString("mynumber", null);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView edit=(ImageView)rootView.findViewById(R.id.edit_icon_img);
        profile_pic=(ImageView)rootView.findViewById(R.id.profile_pic);
        Bitmap pic=BitmapFactory.decodeResource(getResources(), R.drawable.contacticontwo);
        makeDir();
        setImage(profile_pic);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.haveNetworkConnection(getActivity().getApplicationContext())) {
                    Intent intent = new Intent();
                    intent.setType(IMAGE_TYPE);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "select picure"), SELECT_SINGLE_PICTURE);

                   /* Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_SINGLE_PICTURE);*/
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Internet connection not available please try later.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    public static Fragment_Profile newInstance() {
        Fragment_Profile fragment = new Fragment_Profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK) {
            ContentResolver cr = getActivity().getContentResolver();
            Bitmap bitmap = null;
            if (requestCode == SELECT_SINGLE_PICTURE) {
                Uri selectedImage = data.getData();
                perform_Capture(selectedImage);
            }
            else if(requestCode == PIC_CROP) {
                /*Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = cr.query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);*/
                //get the returned data
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap yourSelectedImage = extras.getParcelable("data");
                if(Fragment_Profile.profile_pic!=null){
                    Fragment_Profile.profile_pic.setImageBitmap(yourSelectedImage);//ImageFormatting.ShrinkBitmap(filePath, 100, 100));//
                    saveImage("profile",yourSelectedImage);
                }
            } /*else if (requestCode == CAMERA_PICTURE) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = getUri(bitmap);
                }*/
        }
    }
    public void makeDir()
    {
        String path=getActivity().getApplicationContext().getFilesDir().getAbsolutePath()+"/RevanPics";
        File file = new File ( path );

        if ( file.exists() )
        {
            Log.d("User Profile Screen ", "Message: " + "REVANPICS EXISTS");
        }
        else
        {
            File f = new File(getActivity().getApplicationContext().getFilesDir().getAbsolutePath(),File.separator+"RevanPics/");
            f.mkdirs();
        }
    }
    public void saveImage(String imagename,Bitmap image)
    {
        File file = new File(new File(getActivity().getApplicationContext().getFilesDir().getAbsolutePath(),File.separator+"RevanPics/"), imagename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            updateprofile_pic_parse(bitmapToByteArray(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setImage(ImageView profile)
    {
        String photoPath=getActivity().getApplicationContext().getFilesDir().getAbsolutePath()+File.separator+"RevanPics/"+"profile";
        File file = new File(new File(getActivity().getApplicationContext().getFilesDir().getAbsolutePath(),File.separator+"RevanPics/"), "profile");
        if (file.exists()) {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
            profile.setImageBitmap(bitmap);
        }
    }
    public void updateprofile_pic_parse(byte[] image)
    {
        String number=number=mynumber_value.replace(" ","");
        number= number.replace("-","");
        number= number.replace("(","");
        number= number.replace(")","");
        number= number.replace("+", "");
        final ParseFile photoFile = new ParseFile(number+".jpg", image);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
                    query.whereEqualTo("username", mynumber_value);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> userList, ParseException e) {
                            if (e == null) {
                                int index = 0;
                                while (userList.size() != index) {
                                    ParseObject obj = userList.get(index);
                                    obj.put("profile_pic", photoFile);
                                    obj.saveEventually();
                                    index++;
                                }
                            } else {
                                Log.d("User Parse ", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }

        });
    }
    public byte[] bitmapToByteArray(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        return imageBytes;
        /*  OR
        * int bytes = bitmap.getByteCount();
        * ByteBuffer buffer = ByteBuffer.allocate(bytes);
        * bitmap.copyPixelsToBuffer(buffer);
        * byte[] array = buffer.array();*/
    }
    public static Bitmap byteArrayToBitmap(byte[] image) {

        InputStream inputStream = new ByteArrayInputStream(image);
        BitmapFactory.Options o = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream, null, o);
        /*return BitmapFactory.decodeByteArray(image, 0, image.length);*/
    }
    public void perform_Capture(Uri picUri)
    {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(getActivity(),
                    "Error downloading the image: " + errorMessage,
                    Toast.LENGTH_LONG).show();

        }
    }
}


package com.example.saikrishna.raven.Extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.saikrishna.raven.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sai Krishna on 8/4/2015.
 */
public class ImageFormatting
{
    public static Bitmap ShrinkBitmap(String file, int width, int height){

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

        if (heightRatio > 1 || widthRatio > 1)
        {
            if (heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }
    public static Bitmap ShrinkBitmap_bit(Bitmap image, int width, int height){

        return Bitmap.createScaledBitmap(image, width, height,
                true);
    }
    public static Bitmap ShrinkBitmap_f(String file, float width, float height){

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

        if (heightRatio > 1 || widthRatio > 1)
        {
            if (heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }
    public static byte[] bitmapToByteArray(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        return imageBytes;
    }
    public static String download(ParseFile profile_pic,String name_pic,final Context context, final String directory)
    {

        final String number_f=name_pic;
        makeDir(context, directory);
        final HashMap<String,String> path_o=new HashMap<>();
        if(profile_pic!=null){
            profile_pic.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap profile_pic = byteArrayToBitmap(data);
                        String path=saveImage(number_f, profile_pic,directory, context);
                        path_o.put("path",path);

                    } else {
                        Toast.makeText(context,
                                "Error downloading the image: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }else
        {
            Log.v("Profile Picture null:", number_f);}
        return path_o.get("path");
    }
    public static Bitmap byteArrayToBitmap(byte[] image) {

        InputStream inputStream = new ByteArrayInputStream(image);
        BitmapFactory.Options o = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream, null, o);
        /*return BitmapFactory.decodeByteArray(image, 0, image.length);*/
    }
    public static String saveImage(String imagename,Bitmap image,String directory,Context context)
    {
        File file = new File(new File(context.getFilesDir().getAbsolutePath(),File.separator+directory+"/"), imagename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getFilesDir().getAbsolutePath()+File.separator+directory+"/"+imagename;
    }
    public static void makeDir(Context context,String directory)
    {
        String path=context.getFilesDir().getAbsolutePath()+File.separator+directory+"/";
        File file = new File ( path );

        if ( file.exists() )
        {
            Log.d("Download Parse image", "Message: " + directory+" EXISTS");
        }
        else
        {
            File f = new File(context.getFilesDir().getAbsolutePath(),File.separator+directory+"/");
            f.mkdirs();
        }
    }
    public static Bitmap shrinkBitmap_seen(String seen, Context context){
        Bitmap seen_bit;
        switch (seen){
            case "0":seen_bit= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.time), 25, 25,
                    true);
                break;
            case "1":seen_bit= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.singletick), 25, 25,
                    true);
                break;
            case "2":seen_bit= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.tick_double), 25, 25,
                    true);
                break;
            default: seen_bit= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.time), 25, 25,
                true);}
        return seen_bit;
    }

}

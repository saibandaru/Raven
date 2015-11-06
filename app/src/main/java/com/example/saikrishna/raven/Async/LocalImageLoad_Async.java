package com.example.saikrishna.raven.Async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.saikrishna.raven.Extras.ImageFormatting;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Sai Krishna on 8/5/2015.
 */
public class LocalImageLoad_Async extends AsyncTask<String,Void,Bitmap> {
    //private final WeakReference<MyRecyclerViewAdapter> adapterReference;

    private final WeakReference<ImageView> imageView;
    private String path;
    private int side;
    Context context;
    public LocalImageLoad_Async(final ImageView imageView,String path,int side){
        this.imageView=new WeakReference<ImageView>(imageView);
        this.path=path;
        this.side=side;
    }
    @Override
    protected Bitmap doInBackground(String... ifNeeded){
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = ImageFormatting.ShrinkBitmap(path, side, side);

           /* BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);*/
            return bitmap;
        }
        return null;
    }
    @Override
    protected void onPostExecute(Bitmap image){
        if (imageView != null&&image!=null) {
            final ImageView imageView_final=imageView.get();
            if(imageView_final!=null)
                imageView_final.setImageBitmap(image);
        }
    }
    }

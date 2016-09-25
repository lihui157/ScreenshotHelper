package com.lihui.screenshot.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

/**
 * 描 述:
 *
 * @author: lihui
 * @date: 2016-09-13 13:57
 */
public class Screenshot {

    MediaProjectionManager mMediaProjectionManager;
    MediaProjection mMediaProjection;
    VirtualDisplay mVirtualDisplay;
    WindowManager mWindowManager;
    ImageReader mImageReader;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

    public Screenshot(Activity context){
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1);
        mMediaProjectionManager = (MediaProjectionManager)
                context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    public MediaProjectionManager getMediaProjectionManager(){
        return mMediaProjectionManager;
    }

    public void processScreenshot(final Context context,Intent data){
        mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, data);
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
        new Handler(context.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getImage(context,mImageReader);
            }
        },1000);
    }

    private void getImage(Context context,ImageReader imageReader){
        Image image = imageReader.acquireLatestImage();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int offset = 0;
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * mScreenWidth;
        Bitmap bitmap = Bitmap.createBitmap(mScreenWidth+rowPadding/pixelStride, mScreenHeight, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String strDate = dateFormat.format(new java.util.Date());
        String pathImage = Environment.getExternalStorageDirectory().getPath()+"/Pictures/";
        String nameImage = pathImage+strDate+".png";
        if(bitmap != null) {
            try{
                File fileImage = new File(nameImage);
                if(!fileImage.exists()){
                    fileImage.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(fileImage);
                if(out != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Toast.makeText(context,"get phone's screen succeed",Toast.LENGTH_SHORT).show();
                    Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(fileImage);
                    media.setData(contentUri);
                    context.sendBroadcast(media);
                }
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(context,"cannot get phone's screen",Toast.LENGTH_SHORT).show();
        }
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }

    public void release(){
        stopVirtual();
        tearDownMediaProjection();
    }
}

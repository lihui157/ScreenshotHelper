package com.lihui.screenshot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 描 述:
 *
 * @author: lihui
 * @date: 2016-09-27 13:51
 */

public class Utils {

    private BitmapFactory.Options getBitmapOption(int inSampleSize){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }


    //Bitmap对象保存味图片文件
    public static void saveBitmapFile(String savePath,Bitmap bitmap){
        File file=new File(savePath);//将要保存图片的路径
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

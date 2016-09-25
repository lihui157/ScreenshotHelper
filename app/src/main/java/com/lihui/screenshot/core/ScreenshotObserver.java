package com.lihui.screenshot.core;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.lihui.screenshot.EditScreenshotActivity;

/**
 * 通过监听系统截屏在媒体库中响应的事件，获取截屏数据
 */
public class ScreenshotObserver extends ContentObserver {

    private static final String LOG_TAG = ScreenshotObserver.class.getSimpleName();

    private static Context mContext;
    //记录最后一次使用过的图片id
    private long lastId;

    private static ScreenshotObserver instance;

    private ScreenshotObserver(Context context) {
        super(null);
        mContext = context;
    }

    /**
     * 启动监听
     * @param context
     */
    public static void startObserve(Context context) {
        if (instance == null) {
            instance = new ScreenshotObserver(context);
        }
        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, instance);
        Log.d(LOG_TAG,"监听启动");
    }

    /**
     * 结束监听
     */
    public static void stopObserve() {
        mContext.getContentResolver().unregisterContentObserver(instance);
        Log.d(LOG_TAG,"监听关闭");
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        /**
         * 查询字段
         */
        String[] columns = {
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns._ID,
        };
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.MediaColumns.DATE_ADDED + " desc");
            if (cursor == null) {
                return;
            }
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                long addTime = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                if (checkTime(addTime)
                        && checkPath(filePath)
                        && checkSize(filePath)
                        && !checkId(id)) {
                    Log.e("test",filePath);
                    //记录id，避免下次重复获取相同文件
                    lastId = id;
                    EditScreenshotActivity.toActivity(mContext,filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 验证时间，创建时间太久忽略
     */
    private boolean checkTime(long addTime) {
        return System.currentTimeMillis() - addTime * 1000 <= 1500;
    }

    private boolean checkId(long id){
        return (lastId == id);
    }

    /**
     * 验证尺寸
     */
    private boolean checkSize(String filePath) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metrics);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        return metrics.widthPixels >= options.outWidth && metrics.heightPixels >= options.outHeight;
    }

    /**
     * 验证图片路径中的标志，用以区分截屏
     */
    private boolean checkPath(String filePath) {
        return filePath.toLowerCase().contains("screenshot");
    }

}
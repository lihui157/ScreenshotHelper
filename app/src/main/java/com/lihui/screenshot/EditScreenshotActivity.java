package com.lihui.screenshot;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;

import com.lihui.screenshot.view.BoardView;

public class EditScreenshotActivity extends AppCompatActivity {

    private static final String KEY_PATH = "key_path";

    private BoardView ivBoard;

    public static void toActivity(Context context,String imgPath){
        Intent intent = new Intent();
        intent.setClass(context,EditScreenshotActivity.class);
        intent.putExtra(KEY_PATH,imgPath);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screenshot);
        ivBoard = (BoardView) findViewById(R.id.ivBoard);
        ivBoard.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra(KEY_PATH)));
    }
}

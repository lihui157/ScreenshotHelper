package com.lihui.screenshot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.lihui.screenshot.view.BoardView;

import java.io.File;

public class EditScreenshotActivity extends AppCompatActivity {

    private static final String KEY_PATH = "key_path";

    private BoardView ivBoard;
    private TextView tvSave,tvCancle,tvBackHistory,tvText,tvDraw;

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
        initUI();
    }

    private void initUI(){
        ivBoard = (BoardView) findViewById(R.id.ivBoard);
        ivBoard.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra(KEY_PATH)));
        tvBackHistory = (TextView) findViewById(R.id.tvBackHistory);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvCancle = (TextView) findViewById(R.id.tvCancle);
        tvText = (TextView) findViewById(R.id.tvText);
        tvDraw = (TextView) findViewById(R.id.tvDraw);

        tvBackHistory.setOnClickListener(onClickListener);
        tvSave.setOnClickListener(onClickListener);
        tvCancle.setOnClickListener(onClickListener);
        tvText.setOnClickListener(onClickListener);
        tvDraw.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvBackHistory:
                    break;
                case R.id.tvSave:
                    String file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath()+"/helper/"+System.currentTimeMillis()+".png";
                    Utils.saveBitmapFile(file,ivBoard.getCurrentBitmap());
                    Intent sendIntent =new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(file)));
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "发送给"));
                    break;
                case R.id.tvCancle:
                    break;
                case R.id.tvText:
                    break;
                case R.id.tvDraw:
                    break;
            }
        }
    };
}

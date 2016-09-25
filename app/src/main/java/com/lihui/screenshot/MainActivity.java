package com.lihui.screenshot;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lihui.screenshot.core.Screenshot;
import com.lihui.screenshot.core.ScreenshotService;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MEDIA_PROJECTION = 1001;
    Button btScreenshot;
    Screenshot screenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btScreenshot = (Button) findViewById(R.id.btScreenshot);
        btScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenshot = new Screenshot(MainActivity.this);
                startActivityForResult(screenshot.getMediaProjectionManager().createScreenCaptureIntent(),REQUEST_MEDIA_PROJECTION);
            }
        });
        Intent intent_ = new Intent();
        intent_.setAction(ScreenshotService.ACTION);
        intent_.setPackage(getPackageName());
        startService(intent_);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:

                if (resultCode == RESULT_OK && data != null) {
                    if(screenshot != null)
                    screenshot.processScreenshot(this,data);
                }
                break;
        }

    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(screenshot != null){
            screenshot.release();
            screenshot = null;
        }
    }
}

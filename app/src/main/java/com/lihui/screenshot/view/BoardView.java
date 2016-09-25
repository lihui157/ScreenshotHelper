package com.lihui.screenshot.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lihui.screenshot.R;

import static com.lihui.screenshot.R.color.colorBlack;

/**
 * 描 述:
 *
 * @author: lihui
 * @date: 2016-09-18 14:07
 */
public class BoardView extends AppCompatImageView  {

    private static final String TAG = BoardView.class.getSimpleName();

    protected Paint mPaint = null;
    protected Canvas mCanvas = null;
    protected int mPaintColor = android.R.color.black ; //画笔颜色
    protected int mPaintSize = 2; //画笔大小
    protected Bitmap mBitmap; //画布Bitmap

    private Path mPath;
    private float mX, mY;

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public LineDrawView(Context context, int width, int height, int size, int color) {
//        super(context, width, height, size, color);
//        // TODO Auto-generated constructor stub
//
//        mPath = new Path();
//    }
    private void initData(){
        mPath = new Path();
        //创建位图
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);

//        mCanvas.drawColor(mBitmapBackground);

        mPaint=new Paint(Paint.DITHER_FLAG);//创建一个画笔
        mPaint.setStyle(Paint.Style.STROKE);//设置非填充
        mPaint.setStrokeWidth(mPaintSize); //设置默认笔宽
        mPaint.setColor(getResources().getColor(mPaintColor));//设置画笔默认颜色
        mPaint.setAntiAlias(true);//锯齿不显示
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if(mBitmap == null)
        initData();
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(x, y);
                invalidate();
                break;
            default:
        }
        return true;
    }

    private void onTouchDown(float x, float y) {
//        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void onTouchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx > 0 || dy > 0) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        } else if (dx == 0 || dy == 0) {
            mPath.quadTo(mX, mY, (x + 1 + mX) / 2, (y + 1 + mY) / 2);
            mX = x + 1;
            mY = y + 1;
        }
        mCanvas.drawPath(mPath, mPaint);
    }


    private void onTouchUp(float x, float y) {
//        mPath.reset();

    }

    public void setPaint(int size, int color) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(size);
        mPaint.setColor(color);
    }

    public void setPaintColor(int color){
        mPaintColor = color;
        mPaint.setColor(color);
    }

    public void setPaintSize(int size){
        mPaintSize = size;
        mPaint.setStrokeWidth(size);
    }

    public void freeBitmaps() {
        mBitmap.recycle();
    }
}

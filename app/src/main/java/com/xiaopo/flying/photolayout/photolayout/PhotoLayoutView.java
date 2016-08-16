package com.xiaopo.flying.photolayout.photolayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-8-9.
 */
public class PhotoLayoutView extends View {
    private static final String TAG = "PhotoLayoutView";

    private enum Mode {
        NONE,   //
        DRAG,
        ZOOM,
        MOVE
    }

    private Mode mCurrentMode = Mode.NONE;

    private Paint mBitmapPaint;
    private Paint mBorderPaint;

    private RectF mBorderRect;

    private PhotoLayout mPhotoLayout;

    private float mBorderWidth = 3;

    private float mDownX;
    private float mDownY;

    private List<LayoutPhoto> mLayoutPhotos = new ArrayList<>();
    private Matrix mSizeMatrix;
    private Matrix mDownMatrix;

    private Line mHandlingLine;

    public PhotoLayoutView(Context context) {
        this(context, null, 0);
    }

    public PhotoLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderRect = new RectF();

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setFilterBitmap(true);

        mBorderPaint = new Paint();

        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mSizeMatrix = new Matrix();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPhotoLayout.update();


        for (int i = 0; i < mPhotoLayout.getBorders().size(); i++) {
            Border border = mPhotoLayout.getBorders().get(i);
            canvas.save();
            canvas.clipRect(border.getRect());
            mLayoutPhotos.get(i).draw(canvas, mBitmapPaint);
            canvas.restore();
            Log.d(TAG, "\n");
            Log.d(TAG, "onDraw: " + border.toString());
        }

        for (Line line : mPhotoLayout.getLines()) {
            drawLine(canvas, line);
        }

        for (Line line : mPhotoLayout.getOuterLines()) {
            drawLine(canvas, line);
        }


//        for (LayoutPhoto photo : mLayoutPhotos) {
//            photo.draw(canvas, mBitmapPaint);
//        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();

                mHandlingLine = findHandlingLine();

                if (mHandlingLine != null) {
                    mCurrentMode = Mode.MOVE;
                } else {
                    //TODO
                }

                break;

            case MotionEvent.ACTION_MOVE:
                switch (mCurrentMode) {
                    case NONE:
                        break;
                    case DRAG:
                        break;
                    case ZOOM:
                        break;
                    case MOVE:
                        moveLine(event);
                        break;
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                mHandlingLine = null;
                mCurrentMode = Mode.NONE;
                break;
        }


        return true;

    }

    private void moveLine(MotionEvent event) {
        if (mHandlingLine == null) {
            return;
        }

        if (mHandlingLine.getDirection() == Line.Direction.HORIZONTAL) {
            mHandlingLine.moveTo(event.getY(), 20);
        } else if (mHandlingLine.getDirection() == Line.Direction.VERTICAL) {
            mHandlingLine.moveTo(event.getX(), 20);
        }
    }

    private Line findHandlingLine() {
        for (Line line : mPhotoLayout.getLines()) {
            if (line.contains(mDownX, mDownY, 20)) {
                return line;
            }
        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBorderRect.left = 0;
        mBorderRect.top = 0;
        mBorderRect.right = w;
        mBorderRect.bottom = h;

        if (mPhotoLayout == null) {
            mPhotoLayout = new PhotoLayout(mBorderRect);
        } else {
            mPhotoLayout.setOuterBorder(mBorderRect);
        }

        //TODO delete
        mPhotoLayout.cutBorderEqualPart(mPhotoLayout.getOuterBorder(), 3, Line.Direction.VERTICAL);
//        mPhotoLayout.addLine(mPhotoLayout.getOuterBorder(), Line.Direction.VERTICAL, 1f / 3);
//        mPhotoLayout.addCross(mPhotoLayout.getBorders().get(1), 1f / 2);

    }


    public void addPhoto(final Bitmap bitmap) {
        int index = mLayoutPhotos.size();

        Matrix matrix = BorderUtil.createMatrix(mPhotoLayout.getBorder(index), bitmap);

        LayoutBitmap layoutPhoto = new LayoutBitmap(bitmap, matrix);

        mLayoutPhotos.add(layoutPhoto);

        invalidate();
    }

    private void drawLine(Canvas canvas, Line line) {
        canvas.drawLine(line.start.x, line.start.y, line.end.x, line.end.y, mBorderPaint);
    }
}

package com.xiaopo.flying.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * the puzzle view , the number of puzzle piece due to PuzzleLayout
 *
 * @see PuzzleLayout
 * Created by snowbean on 16-8-9.
 */
public class PuzzleView extends View {
    private static final String TAG = "PhotoLayoutView";
    private float mExtraBorderSize = 50f;

    private enum Mode {
        NONE,
        DRAG,
        ZOOM,
        MOVE
    }

    private Mode mCurrentMode = Mode.NONE;

    private Paint mBitmapPaint;
    private Paint mBorderPaint;

    private RectF mBorderRect;

    private PuzzleLayout mPuzzleLayout;

    private float mBorderWidth = 3;

    private float mDownX;
    private float mDownY;

    private float mOldDistance;
    private PointF mMidPoint;

    private List<PuzzlePiece> mPuzzlePieces = new ArrayList<>();

    private Line mHandlingLine;
    private PuzzlePiece mHandlingPiece;
    private List<PuzzlePiece> mChangedPhotos = new ArrayList<>();

    private boolean mNeedDrawBorder = false;

    public PuzzleView(Context context) {
        this(context, null, 0);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderRect = new RectF();

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setFilterBitmap(true);

        mBorderPaint = new Paint();

        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth(mBorderWidth);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPuzzleLayout == null) {
            Log.e(TAG, "the puzzle layout can not be null");
            return;
        }

        for (int i = 0; i < mPuzzleLayout.getBorderSize(); i++) {
            Border border = mPuzzleLayout.getBorder(i);
            canvas.save();
            canvas.clipRect(border.getRect());
            if (mPuzzlePieces.size() > i)
                mPuzzlePieces.get(i).draw(canvas, mBitmapPaint);
            canvas.restore();
        }

        if (mNeedDrawBorder) {
            for (Line line : mPuzzleLayout.getLines()) {
                drawLine(canvas, line);
            }
        }

        //draw outer line
//        for (Line line : mPuzzleLayout.getOuterLines()) {
//            drawLine(canvas, line);
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
                    mChangedPhotos.clear();
                    mChangedPhotos.addAll(findChangedPhoto());

                    for (int i = 0; i < mChangedPhotos.size(); i++) {
                        mChangedPhotos.get(i).getDownMatrix().set(mChangedPhotos.get(i).getMatrix());
                    }

                } else {
                    mHandlingPiece = findHandlingPhoto();
                    if (mHandlingPiece != null) {
                        mCurrentMode = Mode.DRAG;
                        mHandlingPiece.getDownMatrix().set(mHandlingPiece.getMatrix());
                    }
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                mOldDistance = calculateDistance(event);
                mMidPoint = calculateMidPoint(event);

                if (mHandlingPiece != null &&
                        isInPhotoArea(mHandlingPiece, event.getX(1), event.getY(1))) {
                    mCurrentMode = Mode.ZOOM;
                }
                break;


            case MotionEvent.ACTION_MOVE:
                switch (mCurrentMode) {
                    case NONE:
                        break;
                    case DRAG:
                        if (mHandlingPiece != null) {
                            mHandlingPiece.getMatrix().set(mHandlingPiece.getDownMatrix());
                            mHandlingPiece.getMatrix().postTranslate(event.getX() - mDownX, event.getY() - mDownY);
                        }
                        break;
                    case ZOOM:

                        if (mHandlingPiece != null && event.getPointerCount() >= 2) {
                            float newDistance = calculateDistance(event);

                            mHandlingPiece.getMatrix().set(mHandlingPiece.getDownMatrix());
                            mHandlingPiece.getMatrix().postScale(
                                    newDistance / mOldDistance, newDistance / mOldDistance, mMidPoint.x, mMidPoint.y);

                            mHandlingPiece.setScaleFactor(mHandlingPiece.getMappedWidth() / mHandlingPiece.getWidth());
                        }

                        break;
                    case MOVE:
                        moveLine(event);
                        updatePhotoInBorder(event);
                        break;
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                mHandlingLine = null;

                if (mCurrentMode == Mode.DRAG || mCurrentMode == Mode.ZOOM) {
                    if (!mHandlingPiece.isFilledBorder()) {
                        fillBorder(mHandlingPiece);
                        mHandlingPiece.setScaleFactor(0f);
                    }
                }

                mCurrentMode = Mode.NONE;
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_UP:

                break;
        }
        return true;

    }


    /**
     * let piece fill with its border
     *
     * @param piece puzzle piece which can not be null
     * @return the scale factor to fill with border
     */
    private void fillBorder(PuzzlePiece piece) {
        piece.getMatrix().reset();

        final RectF rectF = piece.getBorder().getRect();

        float offsetX = rectF.centerX() - piece.getWidth() / 2;
        float offsetY = rectF.centerY() - piece.getHeight() / 2;

        piece.getMatrix().postTranslate(offsetX, offsetY);
        float scale = calculateFillScaleFactor(piece);

        piece.getMatrix().postScale(scale, scale, rectF.centerX(), rectF.centerY());
    }

    private float calculateFillScaleFactor(PuzzlePiece piece) {
        final RectF rectF = piece.getBorder().getRect();
        float scale;
        if (piece.getWidth() * rectF.height() > rectF.width() * piece.getHeight()) {
            scale = (rectF.height() + mExtraBorderSize) / piece.getHeight();
        } else {
            scale = (rectF.width() + mExtraBorderSize) / piece.getWidth();
        }
        return scale;
    }

    private float calculateFillScaleFactor(PuzzlePiece piece, Border border) {
        final RectF rectF = border.getRect();
        float scale;
        if (piece.getWidth() * rectF.height() > rectF.width() * piece.getHeight()) {
            scale = rectF.height() / piece.getHeight();
        } else {
            scale = rectF.width() / piece.getWidth();
        }
        return scale;
    }


    //TODO
    private void updatePhotoInBorder(MotionEvent event) {
        for (PuzzlePiece piece : mChangedPhotos) {
            float scale = calculateFillScaleFactor(piece, mPuzzleLayout.getOuterBorder());

            if (piece.getScaleFactor() > scale && piece.getScaleFactor() > 1 && piece.isFilledBorder()) {
                piece.getMatrix().set(piece.getDownMatrix());

                if (mHandlingLine.getDirection() == Line.Direction.HORIZONTAL) {
                    piece.getMatrix().postTranslate(0, (event.getY() - mDownY) / 2);
                } else if (mHandlingLine.getDirection() == Line.Direction.VERTICAL) {
                    piece.getMatrix().postTranslate((event.getX() - mDownX) / 2, 0);
                }
            } else {
                fillBorder(piece);
            }
        }

    }

    private List<PuzzlePiece> findChangedPhoto() {
        if (mHandlingLine == null) return new ArrayList<>();

        List<PuzzlePiece> puzzlePieces = new ArrayList<>();

        for (PuzzlePiece photo : mPuzzlePieces) {
            if (photo.getBorder().contains(mHandlingLine)) {
                puzzlePieces.add(photo);
            }
        }

        return puzzlePieces;
    }


    private void moveLine(MotionEvent event) {
        if (mHandlingLine == null) {
            return;
        }

        mPuzzleLayout.update();

        if (mHandlingLine.getDirection() == Line.Direction.HORIZONTAL) {
            mHandlingLine.moveTo(event.getY(), 20);
        } else if (mHandlingLine.getDirection() == Line.Direction.VERTICAL) {
            mHandlingLine.moveTo(event.getX(), 20);
        }
    }

    private Line findHandlingLine() {
        for (Line line : mPuzzleLayout.getLines()) {
            if (line.contains(mDownX, mDownY, 20)) {
                return line;
            }
        }
        return null;
    }

    private PuzzlePiece findHandlingPhoto() {
        for (PuzzlePiece photo : mPuzzlePieces) {
            if (photo.contains(mDownX, mDownY)) {
                return photo;
            }
        }
        return null;
    }

    private boolean isInPhotoArea(PuzzlePiece handlingPhoto, float x, float y) {
        return handlingPhoto.contains(x, y);
    }

    private float calculateDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    private PointF calculateMidPoint(MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        return new PointF(x, y);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBorderRect.left = getPaddingLeft();
        mBorderRect.top = getPaddingTop();
        mBorderRect.right = w - getPaddingRight();
        mBorderRect.bottom = h - getPaddingBottom();

        if (mPuzzleLayout == null) {
            mPuzzleLayout = new PuzzleLayout(mBorderRect);
        } else {
            mPuzzleLayout.setOuterBorder(mBorderRect);
        }

        //TODO delete
        mPuzzleLayout.cutBorderEqualPart(mPuzzleLayout.getOuterBorder(), 3, Line.Direction.VERTICAL);
//        mPuzzleLayout.addLine(mPuzzleLayout.getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 2);
//        mPuzzleLayout.addLine(mPuzzleLayout.getBorder(1), Line.Direction.VERTICAL, 1f / 2);
//        mPuzzleLayout.addCross(mPuzzleLayout.getBorders().get(1), 1f / 2);

    }


    public void addPiece(final Bitmap bitmap) {
        int index = mPuzzlePieces.size();

        Matrix matrix = BorderUtil.createMatrix(mPuzzleLayout.getBorder(index), bitmap, mExtraBorderSize);

        BitmapPiece layoutPhoto = new BitmapPiece(bitmap, mPuzzleLayout.getBorder(index), matrix);

        mPuzzlePieces.add(layoutPhoto);

        invalidate();
    }

    private void drawLine(Canvas canvas, Line line) {
        canvas.drawLine(line.start.x, line.start.y, line.end.x, line.end.y, mBorderPaint);
    }


    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = borderWidth;
    }

    public boolean isNeedDrawBorder() {
        return mNeedDrawBorder;
    }

    public void setNeedDrawBorder(boolean needDrawBorder) {
        mNeedDrawBorder = needDrawBorder;
    }

    public float getExtraBorderSize() {
        return mExtraBorderSize;
    }

    public void setExtraBorderSize(float extraBorder) {
        mExtraBorderSize = extraBorder;
    }
}

package com.xiaopo.flying.puzzle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * the puzzle view , the number of puzzle piece due to PuzzleLayout
 *
 * @see PuzzleLayout
 * Created by snowbean on 16-8-16.
 */
public class PuzzleView extends View {
  private static final String TAG = "PuzzleView";

  private enum Mode {
    NONE, DRAG, ZOOM, MOVE, SWAP
  }

  private Mode mCurrentMode = Mode.NONE;

  private Paint mBitmapPaint;
  private Paint mBorderPaint;
  private Paint mSelectedBorderPaint;

  private RectF mBorderRect;
  private RectF mSelectedRect;

  private PuzzleLayout mPuzzleLayout;

  private float mBorderWidth = 4;
  private float mExtraSize = 100;
  private int defaultPiecePadding = 0;

  private float mDownX;
  private float mDownY;

  private float mOldDistance;
  private PointF mMidPoint;

  private List<PuzzlePiece> mPuzzlePieces = new ArrayList<>();

  private Line mHandlingLine;
  private PuzzlePiece mHandlingPiece;
  private PuzzlePiece mPreviewHandlingPiece;
  private PuzzlePiece mReplacePiece;

  private List<PuzzlePiece> mChangedPieces = new ArrayList<>();

  private boolean mNeedDrawBorder = false;
  private boolean mMoveLineEnable = true;
  private boolean mNeedDrawOuterBorder = false;

  private Handler mHandler;

  public PuzzleView(Context context) {
    this(context, null, 0);
  }

  public PuzzleView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mBorderRect = new RectF();
    mSelectedRect = new RectF();

    mBitmapPaint = new Paint();
    mBitmapPaint.setAntiAlias(true);
    mBitmapPaint.setFilterBitmap(true);

    mBorderPaint = new Paint();

    mBorderPaint.setAntiAlias(true);
    mBorderPaint.setColor(Color.WHITE);
    mBorderPaint.setStrokeWidth(mBorderWidth);

    mSelectedBorderPaint = new Paint();

    mSelectedBorderPaint.setAntiAlias(true);
    mSelectedBorderPaint.setStyle(Paint.Style.STROKE);
    mSelectedBorderPaint.setColor(Color.parseColor("#99BBFB"));
    mSelectedBorderPaint.setStrokeWidth(mBorderWidth);

    mHandler = new Handler();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (mPuzzleLayout == null || mPuzzleLayout.getBorderSize() == 0) {
      Log.e(TAG, "the puzzle layout or its border can not be null");
      return;
    }

    //draw piece
    for (int i = 0; i < mPuzzleLayout.getBorderSize(); i++) {
      Border border = mPuzzleLayout.getBorder(i);

      if (i >= mPuzzlePieces.size()) {
        break;
      }

      PuzzlePiece piece = mPuzzlePieces.get(i);
      canvas.save();
      canvas.clipRect(border.getRect());
      if (mPuzzlePieces.size() > i) {
        piece.draw(canvas, mBitmapPaint);
      }
      canvas.restore();
    }

    //draw divide line
    if (mNeedDrawBorder) {
      for (Line line : mPuzzleLayout.getLines()) {
        drawLine(canvas, line);
      }
    }

    //draw outer line
    if (mNeedDrawOuterBorder) {
      for (Line line : mPuzzleLayout.getOuterLines()) {
        drawLine(canvas, line);
      }
    }

    //draw selected border
    if (mHandlingPiece != null && mCurrentMode != Mode.SWAP) {
      drawSelectedBorder(canvas, mHandlingPiece);
    }

    if (mHandlingPiece != null && mCurrentMode == Mode.SWAP) {
      mHandlingPiece.draw(canvas, mBitmapPaint, 128);

      if (mReplacePiece != null) {
        drawSelectedBorder(canvas, mReplacePiece);
      }
    }
  }

  private void drawSelectedBorder(Canvas canvas, PuzzlePiece piece) {
    mSelectedRect.set(piece.getPaddingBorderRect());

    mSelectedRect.left += mBorderWidth / 2f;
    mSelectedRect.top += mBorderWidth / 2f;
    mSelectedRect.right -= mBorderWidth / 2f;
    mSelectedRect.bottom -= mBorderWidth / 2f;

    canvas.drawRect(mSelectedRect, mSelectedBorderPaint);

    mSelectedBorderPaint.setStyle(Paint.Style.FILL);
    for (Line line : piece.getBorder().getLines()) {
      if (mPuzzleLayout.getLines().contains(line)) {
        if (line.getDirection() == Line.Direction.HORIZONTAL) {
          canvas.drawRoundRect(
              line.getCenterBound(mSelectedRect.centerX(), mSelectedRect.width(), mBorderWidth,
                  line == piece.getBorder().lineTop), mBorderWidth * 2, mBorderWidth * 2,
              mSelectedBorderPaint);
        } else if (line.getDirection() == Line.Direction.VERTICAL) {
          canvas.drawRoundRect(
              line.getCenterBound(mSelectedRect.centerY(), mSelectedRect.height(), mBorderWidth,
                  line == piece.getBorder().lineLeft), mBorderWidth * 2, mBorderWidth * 2,
              mSelectedBorderPaint);
        }
      }
    }
    mSelectedBorderPaint.setStyle(Paint.Style.STROKE);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (!mMoveLineEnable) {
      return super.onTouchEvent(event);
    }

    switch (event.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
        mDownX = event.getX();
        mDownY = event.getY();

        mHandlingLine = findHandlingLine();

        if (mHandlingLine != null) {
          mCurrentMode = Mode.MOVE;
          mChangedPieces.clear();
          mChangedPieces.addAll(findChangedPiece());

          for (int i = 0; i < mChangedPieces.size(); i++) {
            mChangedPieces.get(i).getDownMatrix().set(mChangedPieces.get(i).getMatrix());
          }
        } else {
          mHandlingPiece = findHandlingPiece();
          if (mHandlingPiece != null) {
            mCurrentMode = Mode.DRAG;
            mHandlingPiece.getDownMatrix().set(mHandlingPiece.getMatrix());

            mHandler.postDelayed(new Runnable() {
              @Override public void run() {
                mCurrentMode = Mode.SWAP;
                invalidate();
                Log.d(TAG, "run: long pressed");
              }
            }, 1000);
          }
        }

        break;

      case MotionEvent.ACTION_POINTER_DOWN:

        mOldDistance = calculateDistance(event);
        mMidPoint = calculateMidPoint(event);

        if (mHandlingPiece != null
            && isInPhotoArea(mHandlingPiece, event.getX(1), event.getY(1))
            && mCurrentMode != Mode.MOVE) {
          mCurrentMode = Mode.ZOOM;
        }
        break;

      case MotionEvent.ACTION_MOVE:
        switch (mCurrentMode) {
          case NONE:
            break;
          case DRAG:
            dragPiece(mHandlingPiece, event);
            break;
          case ZOOM:
            zoomPiece(mHandlingPiece, event);
            break;
          case MOVE:
            moveLine(event);
            mPuzzleLayout.update();
            updatePieceInBorder(event);
            break;

          case SWAP:
            mReplacePiece = findReplacePiece(event);
            dragPiece(mHandlingPiece, event);

            Log.d(TAG, "onTouchEvent: replace");
            break;
        }

        if ((Math.abs(event.getX() - mDownX) > 10 || Math.abs(event.getY() - mDownY) > 10)
            && mCurrentMode != Mode.SWAP) {
          mHandler.removeCallbacksAndMessages(null);
        }

        invalidate();
        break;

      case MotionEvent.ACTION_UP:
        mHandlingLine = null;
        switch (mCurrentMode) {
          case DRAG:
            if (!mHandlingPiece.isFilledBorder()) {
              moveToFillBorder(mHandlingPiece);
            }

            if (mPreviewHandlingPiece == mHandlingPiece
                && Math.abs(mDownX - event.getX()) < 3
                && Math.abs(mDownY - event.getY()) < 3) {
              mHandlingPiece = null;
            }

            mPreviewHandlingPiece = mHandlingPiece;
            break;
          case ZOOM:
            if (!mHandlingPiece.isFilledBorder()) {
              fillBorder(mHandlingPiece);
              mHandlingPiece.setScaleFactor(0f);
            }
            break;

          case SWAP:
            if (mHandlingPiece != null && mReplacePiece != null) {
              Drawable temp = mHandlingPiece.getDrawable();

              mHandlingPiece.setDrawable(mReplacePiece.getDrawable());
              mReplacePiece.setDrawable(temp);

              fillBorder(mHandlingPiece);
              fillBorder(mReplacePiece);
            }

            mHandlingPiece = null;
            mReplacePiece = null;
            break;
        }

        mCurrentMode = Mode.NONE;

        mHandler.removeCallbacksAndMessages(null);

        invalidate();
        break;

      case MotionEvent.ACTION_POINTER_UP:

        break;
    }
    return true;
  }

  private void zoomPiece(PuzzlePiece piece, MotionEvent event) {
    if (piece != null && event.getPointerCount() >= 2) {
      float newDistance = calculateDistance(event);

      piece.getMatrix().set(piece.getDownMatrix());
      piece.getMatrix()
          .postScale(newDistance / mOldDistance, newDistance / mOldDistance, mMidPoint.x,
              mMidPoint.y);

      piece.setScaleFactor(piece.getMappedWidth() / piece.getWidth());
    }
  }

  private void dragPiece(PuzzlePiece piece, MotionEvent event) {
    if (piece != null) {
      piece.getMatrix().set(piece.getDownMatrix());
      piece.getMatrix().postTranslate(event.getX() - mDownX, event.getY() - mDownY);

      piece.setTranslateX(piece.getMappedCenterPoint().x - piece.getBorder().centerX());

      piece.setTranslateY(piece.getMappedCenterPoint().y - piece.getBorder().centerY());
    }
  }

  private void moveToFillBorder(final PuzzlePiece piece) {
    Border border = piece.getBorder();
    RectF rectF = piece.getMappedBound();
    float offsetX = 0f;
    float offsetY = 0f;

    if (rectF.left > border.left()) {
      offsetX = border.left() - rectF.left;
    }

    if (rectF.top > border.top()) {
      offsetY = border.top() - rectF.top;
    }

    if (rectF.right < border.right()) {
      offsetX = border.right() - rectF.right;
    }

    if (rectF.bottom < border.bottom()) {
      offsetY = border.bottom() - rectF.bottom;
    }

    piece.getMatrix().postTranslate(offsetX, offsetY);

    piece.setTranslateX(border.centerX() - piece.getMappedCenterPoint().x);
    piece.setTranslateY(border.centerY() - piece.getMappedCenterPoint().y);

    if (!piece.isFilledBorder()) {
      fillBorder(piece);
    }
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

    if (piece.getRotation() != 0) {
      rotate(piece, piece.getRotation(), false);
    }

    if (piece.isNeedHorizontalFlip()) {
      flipHorizontally(piece, false);
    }

    if (piece.isNeedVerticalFlip()) {
      flipVertically(piece, false);
    }

    piece.setTranslateX(0f);
    piece.setTranslateY(0f);
    piece.setScaleFactor(0f);
  }

  private float calculateFillScaleFactor(PuzzlePiece piece) {
    final RectF rectF = piece.getBorder().getRect();
    float scale;
    if (piece.getRotation() == 90 || piece.getRotation() == 270) {
      if (piece.getHeight() * rectF.height() > rectF.width() * piece.getWidth()) {
        scale = (rectF.height() + mExtraSize) / piece.getWidth();
      } else {
        scale = (rectF.width() + mExtraSize) / piece.getHeight();
      }
    } else {
      if (piece.getWidth() * rectF.height() > rectF.width() * piece.getHeight()) {
        scale = (rectF.height() + mExtraSize) / piece.getHeight();
      } else {
        scale = (rectF.width() + mExtraSize) / piece.getWidth();
      }
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

  private void updatePieceInBorder(MotionEvent event) {
    for (PuzzlePiece piece : mChangedPieces) {
      float scale = calculateFillScaleFactor(piece, mPuzzleLayout.getOuterBorder());

      if (piece.getScaleFactor() > scale && piece.isFilledBorder()) {
        piece.getMatrix().set(piece.getDownMatrix());

        if (mHandlingLine.getDirection() == Line.Direction.HORIZONTAL) {
          piece.getMatrix().postTranslate(0, (event.getY() - mDownY) / 2);
        } else if (mHandlingLine.getDirection() == Line.Direction.VERTICAL) {
          piece.getMatrix().postTranslate((event.getX() - mDownX) / 2, 0);
        }
      } else if (piece.isFilledBorder() && (piece.getTranslateX() != 0f
          || piece.getTranslateY() != 0f)) {
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

  private void moveLine(MotionEvent event) {
    if (mHandlingLine == null) {
      return;
    }

    if (mHandlingLine.getDirection() == Line.Direction.HORIZONTAL) {
      mHandlingLine.moveTo(event.getY(), 40);
    } else if (mHandlingLine.getDirection() == Line.Direction.VERTICAL) {
      mHandlingLine.moveTo(event.getX(), 40);
    }
  }

  private List<PuzzlePiece> findChangedPiece() {
    if (mHandlingLine == null) return new ArrayList<>();

    List<PuzzlePiece> puzzlePieces = new ArrayList<>();

    for (PuzzlePiece piece : mPuzzlePieces) {
      if (piece.getBorder().contains(mHandlingLine)) {
        puzzlePieces.add(piece);
      }
    }

    return puzzlePieces;
  }

  private Line findHandlingLine() {
    for (Line line : mPuzzleLayout.getLines()) {
      if (line.contains(mDownX, mDownY, 20)) {
        return line;
      }
    }
    return null;
  }

  private PuzzlePiece findHandlingPiece() {
    for (PuzzlePiece piece : mPuzzlePieces) {
      if (piece.contains(mDownX, mDownY)) {
        return piece;
      }
    }
    return null;
  }

  private PuzzlePiece findReplacePiece(MotionEvent event) {
    for (PuzzlePiece piece : mPuzzlePieces) {
      if (piece.contains(event.getX(), event.getY()) && piece != mHandlingPiece) {
        return piece;
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

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mBorderRect.left = getPaddingLeft();
    mBorderRect.top = getPaddingTop();
    mBorderRect.right = w - getPaddingRight();
    mBorderRect.bottom = h - getPaddingBottom();

    if (mPuzzleLayout != null) {
      mPuzzleLayout.setOuterBorder(mBorderRect);
      mPuzzleLayout.layout();
    }

    if (mPuzzlePieces.size() != 0) {
      for (int i = 0; i < mPuzzlePieces.size(); i++) {
        PuzzlePiece piece = mPuzzlePieces.get(i);
        piece.setBorder(mPuzzleLayout.getBorder(i));
        piece.getMatrix()
            .set(BorderUtil.createMatrix(mPuzzleLayout.getBorder(i), piece.getWidth(),
                piece.getHeight(), mExtraSize));
      }
    }

    invalidate();
  }

  public void replace(Bitmap bitmap) {
    replace(new BitmapDrawable(getResources(), bitmap));
  }

  public void replace(Drawable bitmapDrawable) {
    if (mHandlingPiece == null) {
      return;
    }

    mHandlingPiece.setDrawable(bitmapDrawable);
    fillBorder(mHandlingPiece);

    invalidate();
  }

  public void flipHorizontally() {
    flipHorizontally(mHandlingPiece, true);
  }

  public void flipVertically() {
    flipVertically(mHandlingPiece, true);
  }

  private void flipHorizontally(PuzzlePiece piece, boolean needChangeStatus) {
    if (piece == null) return;
    if (needChangeStatus) {
      piece.setNeedHorizontalFlip(!piece.isNeedHorizontalFlip());
    }
    piece.getMatrix()
        .postScale(-1, 1, piece.getMappedCenterPoint().x, piece.getMappedCenterPoint().y);

    invalidate();
  }

  private void flipVertically(PuzzlePiece piece, boolean needChangeStatus) {
    if (piece == null) return;
    if (needChangeStatus) {
      piece.setNeedVerticalFlip(!piece.isNeedVerticalFlip());
    }

    piece.getMatrix()
        .postScale(1, -1, piece.getMappedCenterPoint().x, piece.getMappedCenterPoint().y);

    invalidate();
  }

  public void rotate(float rotate) {
    rotate(mHandlingPiece, rotate, true);
  }

  private void rotate(PuzzlePiece piece, float rotate, boolean needChangeStatus) {
    if (piece == null) return;
    if (needChangeStatus) {
      piece.setRotation((piece.getRotation() + rotate) % 360f);
    }

    if (needChangeStatus) {
      piece.getMatrix()
          .postRotate(rotate, piece.getMappedCenterPoint().x, piece.getMappedCenterPoint().y);
      fillBorder(piece);
    } else {
      piece.getMatrix()
          .postRotate(piece.getRotation(), piece.getMappedCenterPoint().x,
              piece.getMappedCenterPoint().y);
    }

    invalidate();
  }

  public void addPieces(final List<Bitmap> bitmaps) {
    for (Bitmap bitmap : bitmaps) {
      addPiece(bitmap);
    }

    invalidate();
  }

  public void addPiece(final Bitmap bitmap) {
    addPiece(new BitmapDrawable(getResources(), bitmap));
  }

  public void addPiece(final Bitmap bitmap, int padding) {
    addPiece(new BitmapDrawable(getResources(), bitmap), padding);
  }

  public void addPiece(final Bitmap bitmap, int paddingLeft, int paddingTop, int paddingRight,
      int paddingBottom) {
    addPiece(new BitmapDrawable(getResources(), bitmap), paddingLeft, paddingTop, paddingRight,
        paddingBottom);
  }

  public void addPiece(final Drawable drawable) {
    addPiece(drawable, defaultPiecePadding);
  }

  public void addPiece(final Drawable drawable, int padding) {
    addPiece(drawable, padding, padding, padding, padding);
  }

  public void addPiece(final Drawable drawable, int paddingLeft, int paddingTop, int paddingRight,
      int paddingBottom) {
    int index = mPuzzlePieces.size();

    if (index >= mPuzzleLayout.getBorderSize()) {
      Log.e(TAG, "addPiece: can not add more. the current puzzle layout can contains "
          + mPuzzleLayout.getBorderSize()
          + " puzzle piece.");
      return;
    }

    Matrix matrix = BorderUtil.createMatrix(mPuzzleLayout.getBorder(index), drawable, mExtraSize);

    PuzzlePiece layoutPhoto = new PuzzlePiece(drawable, mPuzzleLayout.getBorder(index), matrix);
    layoutPhoto.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

    mPuzzlePieces.add(layoutPhoto);

    invalidate();
  }

  private void drawLine(Canvas canvas, Line line) {
    canvas.drawLine(line.start.x, line.start.y, line.end.x, line.end.y, mBorderPaint);
  }

  public void reset() {
    mHandlingLine = null;
    mHandlingPiece = null;

    if (mPuzzleLayout != null) {
      mPuzzleLayout.reset();
    }
    mPuzzlePieces.clear();
    mChangedPieces.clear();

    invalidate();
  }

  public PuzzleLayout getPuzzleLayout() {
    return mPuzzleLayout;
  }

  public void setPuzzleLayout(PuzzleLayout puzzleLayout) {
    reset();

    mPuzzleLayout = puzzleLayout;
    mPuzzleLayout.setOuterBorder(mBorderRect);
    mPuzzleLayout.layout();

    invalidate();
  }

  public float getBorderWidth() {
    return mBorderWidth;
  }

  public void setBorderWidth(float borderWidth) {
    mBorderWidth = borderWidth;
    mBorderPaint.setStrokeWidth(borderWidth);
    invalidate();
  }

  public boolean isNeedDrawBorder() {
    return mNeedDrawBorder;
  }

  public void setNeedDrawBorder(boolean needDrawBorder) {
    mNeedDrawBorder = needDrawBorder;
    mHandlingPiece = null;
    mPreviewHandlingPiece = null;
    invalidate();
  }

  public boolean isMoveLineEnable() {
    return mMoveLineEnable;
  }

  public void setMoveLineEnable(boolean moveLineEnable) {
    mMoveLineEnable = moveLineEnable;
  }

  public float getExtraSize() {
    return mExtraSize;
  }

  public void setExtraSize(float extraSize) {
    if (extraSize < 0) {
      Log.e(TAG, "setExtraSize: the extra size must be greater than 0");
      mExtraSize = 0;
    } else {
      mExtraSize = extraSize;
    }
  }

  public boolean isNeedDrawOuterBorder() {
    return mNeedDrawOuterBorder;
  }

  public void setNeedDrawOuterBorder(boolean needDrawOuterBorder) {
    mNeedDrawOuterBorder = needDrawOuterBorder;
  }

  public void setBorderColor(int color) {
    mBorderPaint.setColor(color);
    invalidate();
  }

  public void setSelectedBorderColor(int color) {
    mSelectedBorderPaint.setColor(color);
    invalidate();
  }

  public int getDefaultPiecePadding() {
    return defaultPiecePadding;
  }

  public void setDefaultPiecePadding(int defaultPiecePadding) {
    this.defaultPiecePadding = defaultPiecePadding;
  }

  public Bitmap createBitmap() {
    mHandlingPiece = null;

    invalidate();

    Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    this.draw(canvas);

    return bitmap;
  }

  public void save(File file) {
    save(file, 100, null);
  }

  public void save(File file, Callback callback) {
    save(file, 100, callback);
  }

  public void save(File file, int quality, Callback callback) {
    Bitmap bitmap = null;
    FileOutputStream outputStream = null;

    try {
      bitmap = createBitmap();
      outputStream = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

      if (!file.exists()) {
        Log.e(TAG, "notifySystemGallery: the file do not exist.");
        return;
      }

      try {
        MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
            file.getAbsolutePath(), file.getName(), null);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      getContext().sendBroadcast(
          new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

      if (callback != null) {
        callback.onSuccess();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      if (callback != null) {
        callback.onFailed();
      }
    } finally {
      if (bitmap != null) {
        bitmap.recycle();
      }

      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public interface Callback {
    void onSuccess();

    void onFailed();
  }

  public interface OnPieceSelectedListener {
    void onPieceSelected(PuzzlePiece piece);
  }
}
